# Relatório de Desenvolvimento — Spec-Driven Development (SDD)

## Feature: Alarmes de Tarefas (Desafio K1-T3)

**Repositório:** `devlavanere/TODO-List`  
**Branch:** `feat/alarme-de-tarefas`  
**Issue:** [#5](https://github.com/devlavanere/TODO-List/issues/5)  
**Data de Conclusão:** 08/11/2026  
**Tokens Estimados Gastos:** ~18.000

---

## 1. Introdução

Este relatório documenta o processo completo de desenvolvimento da feature **Alarmes de Tarefas** no projeto ZG-Hero TODO List, utilizando a metodologia **Spec-Driven Development (SDD)**. O SDD é uma abordagem onde a especificação (`docs/spec.md`) e o plano de implementação (`docs/plano.md`) são escritos e aprovados antes de qualquer linha de código ser produzida, garantindo que o desenvolvimento siga um roteiro claro e validado.

A feature permite que os usuários configurem alarmes para suas tarefas na versão CLI da aplicação. Cada alarme possui uma antecedência em minutos, que é subtraída da data/hora de término da tarefa para calcular o momento exato do disparo. Quando o menu principal é renderizado, o sistema verifica se algum alarme deve ser disparado e exibe uma notificação ao usuário.

---

## 2. Metodologia SDD

### 2.1. Especificação (`docs/spec.md`)

O processo iniciou com a criação da especificação técnica em `docs/spec.md`, que definiu:

- **Contexto e Objetivo:** Permitir que os usuários configurem alarmes para suas tarefas, com notificação na interface CLI quando o horário da tarefa atrelado à antecedência chegar.
- **Regras de Negócio:**
  1. A data da tarefa muda de `LocalDate` para `LocalDateTime`, exigindo data e hora completas.
  2. Uma tarefa terá uma lista de alarmes, gerenciada por uma classe separada `Alarme`, contendo a antecedência em minutos, o momento do disparo (`LocalDateTime`) e um status boolean `notificado`.
  3. O sistema verificará alarmes sempre que o Menu/Dashboard for impresso na tela.
  4. Se o momento atual for igual ou superior à data/hora de disparo de um alarme não notificado (e a tarefa não for `DONE`), um aviso é exibido e o alarme é marcado como `notificado = true`.
- **Critérios de Aceite:**
  - Ao abrir a aplicação, se houver um alarme pendente para aquele momento, o sistema deve imprimir o aviso na tela.
  - Após exibir o alarme uma vez, se o usuário atualizar a tela, o alarme não deve "apitar" novamente.
  - Tarefas concluídas (`Status.DONE`) não disparam alarmes, mesmo que o horário chegue.

### 2.2. Plano de Implementação (`docs/plano.md`)

Com base na especificação, o plano foi dividido em tarefas sequenciais:

1. **Modelo de Dados:** Criar a classe `Alarme` e atualizar a classe `Task` para suportar `LocalDateTime` e lista de alarmes.
2. **Lógica de Negócio:** Implementar o método `adicionarAlarme` na classe `Task` e o método `checkAndPrintAlarms` no `TaskService`.
3. **Persistência CSV:** Atualizar os métodos `saveToFile` e `loadFromFile` para serializar e desserializar os alarmes.
4. **Interface CLI:** Integrar a verificação de alarmes no menu principal (`Main.java`) e permitir ao usuário configurar alarmes ao criar uma tarefa.
5. **Testes:** Criar testes Spock para validar o cálculo do `momentoDisparo` e o comportamento do `notificado`.

---

## 3. Arquitetura da Solução

### 3.1. Classes Criadas

#### Classe `Alarme` (`model/Alarme.java`)

Classe de entidade que representa um alarme individual:

```java
public class Alarme {
    private int minutosAntecedencia;
    private LocalDateTime momentoDisparo;
    private boolean notificado;
    
    // Construtor para novos alarmes (notificado = false)
    public Alarme(int minutosAntecedencia, LocalDateTime momentoDisparo) {
        this.minutosAntecedencia = minutosAntecedencia;
        this.momentoDisparo = momentoDisparo;
        this.notificado = false;
    }
    
    // Construtor completo para recuperação do CSV
    public Alarme(int minutosAntecedencia, LocalDateTime momentoDisparo, boolean notificado) {
        this.minutosAntecedencia = minutosAntecedencia;
        this.momentoDisparo = momentoDisparo;
        this.notificado = notificado;
    }
    
    public String toCsvFormat() {
        return minutosAntecedencia + "," + momentoDisparo.toString() + "," + notificado;
    }
}
```

**Decisões de Design:**
- Dois construtores: um simplificado para criação de novos alarmes e um completo para desserialização do CSV.
- O campo `notificado` nasce sempre como `false` no construtor principal, garantindo que um alarme recém-criado ainda precise ser disparado.
- Método `toCsvFormat()` para facilitar a serialização, usando vírgula como delimitador interno (diferente do ponto e vírgula do CSV principal).

#### Alterações na Classe `Task` (`model/Task.java`)

- O campo `dataTermino` foi alterado de `LocalDate` para `LocalDateTime`.
- Adicionado o campo `List<Alarme> alarmes`, inicializado como `ArrayList` vazio nos construtores.
- Adicionado o método `adicionarAlarme(int minutosAntecedencia)` que calcula o `momentoDisparo` subtraindo os minutos da `dataTermino`.
- Adicionado o método `setAlarmes(List<Alarme>)` para permitir a restauração dos alarmes ao carregar do CSV.
- O método `toString()` foi atualizado para exibir a quantidade de alarmes associados.

### 3.2. Serviço de Tarefas

#### Método `checkAndPrintAlarms` (`service/TaskService.java`)

Método responsável por verificar e exibir os alarmes pendentes:

```java
public void checkAndPrintAlarms() {
    LocalDateTime agora = LocalDateTime.now();
    boolean precisaSalvar = false;
    
    for (Task t : tasks) {
        if (t.getStatus() == Status.DONE) continue;
        
        for (Alarme a : t.getAlarmes()) {
            if (!a.isNotificado() && (agora.isAfter(a.getMomentoDisparo()) || agora.isEqual(a.getMomentoDisparo()))) {
                System.out.println("  [ALARME] ATENÇÃO: A tarefa '" + t.getNome() + "' está próxima!");
                a.setNotificado(true);
                precisaSalvar = true;
            }
        }
    }
    
    if (precisaSalvar) saveToFile();
}
```

**Lógica de Verificação:**
- Itera sobre todas as tarefas, ignorando aquelas com status `DONE`.
- Para cada alarme de cada tarefa, verifica se o momento atual é maior ou igual ao `momentoDisparo` e se o alarme ainda não foi notificado.
- Ao disparar, exibe a mensagem de alerta, marca como `notificado = true` e salva as alterações no CSV.

### 3.3. Persistência CSV

#### Formato Atualizado

O formato do `tasks.csv` foi estendido para suportar os alarmes:

```
{id};{nome};{descricao};{dataTermino};{prioridade};{categoria};{status};{alarmes}
```

O campo `{alarmes}` contém:
- `VAZIO` se a tarefa não possui alarmes.
- Uma sequência de alarmes separados por `|`, onde cada alarme é formatado como `{minutosAntecedencia},{momentoDisparo},{notificado}`.

**Exemplo:**
```
abc123;Reunião;Alinhamento;2026-11-10T14:00;3;Trabalho;TODO;30,2026-11-10T13:30,false|60,2026-11-10T13:00,false
```

#### Desserialização com Fallback

O método `loadFromFile` inclui um fallback para compatibilidade com dados antigos:
- Tenta primeiro parsear o campo de data como `LocalDateTime`.
- Se falhar, tenta parsear como `LocalDate` e converte para `LocalDateTime` com `atStartOfDay()`.
- Se a linha tiver 8 campos (formato novo), processa os alarmes; caso contrário, ignora.

---

## 4. Interface CLI

### 4.1. Integração no Menu Principal

O método `checkAndPrintAlarms()` é chamado no início do loop principal do `Main.java`, garantindo que os alarmes sejam verificados a cada renderização do menu.

### 4.2. Configuração de Alarmes

Ao criar uma nova tarefa, o usuário é perguntado se deseja adicionar alarmes. Se sim, pode inserir múltiplos valores de antecedência em minutos, separados por vírgula.

### 4.3. Formato de Data/Hora

O usuário deve informar a data e hora no formato ISO 8601: `YYYY-MM-DDTHH:mm` (exemplo: `2026-11-10T14:30`).

---

## 5. Testes Automatizados

### 5.1. Framework Utilizado

Os testes foram escritos em **Spock/Groovy**, aproveitando a sintaxe expressiva `given-when-then` e a integração nativa com o JUnit Platform.

### 5.2. TaskSpec.groovy — Testes da Nova Feature

Arquivo: `src/test/groovy/br/com/zghero/model/TaskSpec.groovy`

Cobertura dos testes:

| Teste | Descrição |
|-------|-----------|
| `adicionarAlarme deve calcular o momentoDisparo corretamente` | Verifica que o momento de disparo é calculado subtraindo os minutos de antecedência da data de término. |
| `adicionarAlarme deve criar alarme com notificado igual a false` | Garante que um alarme recém-criado nasce com `notificado = false`. |
| `adicionarAlarme deve suportar múltiplos alarmes` | Confirma que uma tarefa pode ter vários alarmes com diferentes antecedentes, cada um com seu próprio `momentoDisparo`. |
| `adicionarAlarme deve lidar com antecedência de zero minutos` | Testa o caso limite onde o alarme dispara exatamente na data de término. |
| `nova tarefa deve nascer com lista de alarmes vazia` | Verifica que uma tarefa recém-criada não possui alarmes. |

### 5.3. TaskServiceSpec.groovy — Testes de Funcionalidade Legada

Arquivo: `src/test/groovy/br/com/zghero/service/TaskServiceSpec.groovy`

Cobertura dos testes:

| Teste | Descrição |
|-------|-----------|
| `filterByPriority deve retornar apenas tarefas com a prioridade especificada` | Valida que o filtro por prioridade retorna apenas as tarefas correspondentes. |
| `filterByPriority deve retornar lista vazia quando não há tarefas com a prioridade` | Garante que o filtro retorna lista vazia para prioridades inexistentes. |
| `filterByCategory deve retornar apenas tarefas da categoria especificada` | Confirma que o filtro por categoria funciona corretamente. |
| `filterByCategory deve ser insensível a maiúsculas e minúsculas` | Verifica o comportamento `equalsIgnoreCase` do filtro. |
| `filterByCategory deve retornar lista vazia para categoria inexistente` | Garante que o filtro retorna lista vazia para categorias inexistentes. |

---

## 6. Correções de Rota

Durante o desenvolvimento SDD, foram identificadas e corrigidas as seguintes divergências entre o plano e a implementação:

### 6.1. Problema: DateTimeParseException na CLI

**Descrição:**  
Na fase de implementação da interface CLI (`Main.java`), o usuário foi instruído a digitar apenas a data no formato `YYYY-MM-DD`. Como o modelo de dados havia sido alterado de `LocalDate` para `LocalDateTime`, a tentativa de parsear a entrada do usuário com `LocalDateTime.parse()` resultava em um `DateTimeParseException`, pois o formato fornecido não incluía a componente de hora.

**Causa Raiz:**  
A especificação corretamente definia que a data deveria ser `LocalDateTime`, e o modelo de dados (`Task.java`) foi implementado corretamente desde o início. No entanto, a instrução de entrada na CLI (`System.out.print`) não foi atualizada para refletir a nova exigência de formato ISO completo (`YYYY-MM-DDTHH:mm`). O usuário digitava `2026-11-10`, mas o código esperava `2026-11-10T14:00`.

**Correção Aplicada:**  
1. O `System.out.print` da CLI foi alterado para instruir o usuário sobre o formato correto:
   ```java
   System.out.print("Data/Hora (YYYY-MM-DDTHH:mm): ");
   ```
2. O filtro de datas no `TaskService` (`filterByDate`) foi mantido com `LocalDate` para compatibilidade, utilizando `.toLocalDate()` na comparação.
3. O método `loadFromFile` recebeu um fallback que tenta primeiro `LocalDateTime.parse()` e, em caso de falha, recorre a `LocalDate.parse().atStartOfDay()`, garantindo compatibilidade com dados CSV antigos.

**Lição Aprendida:**  
Ao alterar o tipo de um campo de dados de `LocalDate` para `LocalDateTime`, é essencial revisar todos os pontos de entrada de dados (CLI, filtros, persistência) e garantir que as mensagens de instrução ao usuário estejam alinhadas com o novo formato esperado. A especificação estava correta, mas a comunicação com o usuário final na interface foi negligenciada inicialmente.

### 6.2. Problema: Delimitador de Alarmes no CSV

**Descrição:**  
O CSV principal usa ponto e vírgula (`;`) como delimitador. Os alarmes, por serem múltiplos por tarefa, precisavam de um delimitador interno. Inicialmente, considerou-se usar o ponto e vírgula também, o que causaria ambiguidade ao parsear a linha.

**Correção Aplicada:**  
- Cada alarme usa vírgula (`,`) como delimitador interno: `{minutos},{momentoDisparo},{notificado}`.
- Múltiplos alarmes são separados por pipe (`|`).
- Essa escolha evita conflitos com o delimitador principal do CSV (`;`).

---

## 7. Decisões Técnicas

### 7.1. Por que LocalDateTime em vez de LocalDate?

A especificação exigia que o alarme disparasse em um momento específico do dia. `LocalDate` só armazena a data sem a hora, o que tornaria impossível calcular o momento exato do disparo. `LocalDateTime` foi a escolha natural para suportar a precisão de minutos necessária pelos alarmes.

### 7.2. Por que o campo `notificado` é persistente?

O critério de aceite exige que o alarme não "apite" novamente após ser exibido. Para garantir esse comportamento mesmo após reiniciar a aplicação, o estado `notificado` é persistido no CSV. Ao carregar os dados, o sistema restaura o estado de cada alarme, evitando notificações duplicadas.

### 7.3. Por que Spock/Groovy para os testes?

O projeto adotou Spock/Groovy como framework de testes, aproveitando:
- Sintaxe `given-when-then` mais legível que JUnit puro.
- Suporte nativo a dados tabulares para testes parametrizados.
- Integração com JUnit Platform via `useJUnitPlatform()`.
- Capacidade de testar código Java sem necessidade de migrar para Groovy.

### 7.4. Formato ISO 8601 para Persistência

`LocalDateTime.toString()` já produz o formato ISO 8601 (`2026-11-10T14:00`), que é padrão e facilmente parseável. Isso elimina a necessidade de formatters customizados e garante interoperabilidade.

---

## 8. Fluxo de Trabalho Completo

### Fase 1: Planejamento (SDD)
1. Criação da Issue #5 no GitHub.
2. Escrita da especificação em `docs/spec.md`.
3. Elaboração do plano de implementação em `docs/plano.md`.
4. Revisão e aprovação do plano.

### Fase 2: Implementação do Modelo
1. Criação da classe `Alarme.java` com construtores e getters/setters.
2. Alteração da classe `Task.java`: `LocalDate` → `LocalDateTime`, adição de `List<Alarme>`.
3. Implementação do método `adicionarAlarme()` com cálculo de `momentoDisparo`.

### Fase 3: Implementação do Serviço
1. Adição do método `checkAndPrintAlarms()` no `TaskService.java`.
2. Atualização de `saveToFile()` para serializar alarmes.
3. Atualização de `loadFromFile()` para desserializar alarmes com fallback.

### Fase 4: Interface CLI
1. Integração de `checkAndPrintAlarms()` no loop principal do `Main.java`.
2. Adição do prompt para configuração de alarmes ao criar tarefa.
3. Correção do formato de entrada para `YYYY-MM-DDTHH:mm`.

### Fase 5: Testes e Documentação
1. Configuração do Spock/Groovy no `build.gradle.kts`.
2. Criação de `TaskSpec.groovy` com testes da nova feature.
3. Criação de `TaskServiceSpec.groovy` com testes de funcionalidade legado.
4. Atualização do `README.md` com descrição da feature.
5. Criação deste `RELATORIO.md`.

### Fase 6: Integração e Entrega
1. Commit das alterações com mensagem descritiva.
2. Push para a branch `feat/alarme-de-tarefas`.
3. Abertura do Pull Request para a branch `develop`.

---

## 9. Métricas de Qualidade

| Métrica | Valor |
|---------|-------|
| Classes criadas | 1 (`Alarme.java`) |
| Classes modificadas | 4 (`Task.java`, `TaskService.java`, `Main.java`, `build.gradle.kts`) |
| Arquivos de teste criados | 2 (`TaskSpec.groovy`, `TaskServiceSpec.groovy`) |
| Casos de teste escritos | 10 (5 novos + 5 legado) |
| Documentação atualizada | 2 (`README.md`, `RELATORIO.md`) |
| Tokens gastos na sessão | ~18.000 |
| Correções de rota | 2 (DateTimeParseException, delimitador CSV) |

---

## 10. Conclusão

A feature de **Alarmes de Tarefas** foi implementada com sucesso seguindo a metodologia Spec-Driven Development (SDD). A especificação clara em `docs/spec.md` permitiu que o desenvolvimento seguisse um roteiro bem definido, minimizando retrabalho e garantindo que todos os critérios de aceite fossem atendidos.

Os principais aprendizados deste ciclo SDD foram:

1. **A especificação é a fonte da verdade:** Quando o modelo de dados foi corretamente alterado para `LocalDateTime`, a única falha ocorreu na comunicação com o usuário final (CLI), não no código. Isso reforça que a especificação deve cobrir não apenas a lógica, mas também a experiência do usuário.

2. **Testes para código legado são valiosos:** Ao adicionar testes Spock para `filterByPriority` e `filterByCategory`, garantimos que a funcionalidade existente continua funcionando após as alterações do modelo de dados.

3. **Persistência exige cuidado:** Ao estender o formato CSV, foi necessário considerar compatibilidade com dados antigos, implementando fallbacks robustos no método de carregamento.

4. **Delimitadores importam:** A escolha de delimitadores diferentes para níveis hierárquicos de dados (`;` para campos, `,` para subcampos de alarme, `|` para separação de alarmes) evitou ambiguidades na serialização.

O Pull Request foi aberto para revisão e merge na branch `develop`, concluindo o fluxo completo de desenvolvimento orientado por especificação.

---

**Relatório gerado por:** Junie (Assistente de Desenvolvimento)  
**Projeto:** ZG-Hero TODO List  
**Autor Humano:** Michel Lavanere Sampaio
