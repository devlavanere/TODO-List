# Contexto do Agente - ZG-Hero TODO List

## Visão Geral
Aplicação de TODO List (MVP) para gerenciamento de tarefas. Permite operações de CRUD, listagem, filtros (por categoria, prioridade, status, data) e exibe um dashboard estatístico. Atualmente, o frontend (JS) e o backend (Java) operam de forma separada. A feature atual (Alarmes) será focada no Backend (CLI).

## Stack e Estrutura
- **Linguagem:** Java 8
- **Build Tool:** Gradle
- **Testes:** JUnit 5
- **Persistência:** Arquivo de texto simples (`tasks.csv`) gerido via I/O nativo.
- **Arquitetura:** Padrão MVC simplificado (`model`, `service`, `ui`).

## Como Executar e Testar
- **Executar a aplicação CLI:** `./gradlew run`
- **Rodar os testes:** `./gradlew test`

## Convenções
- **Nomenclatura:** Padrão CamelCase para variáveis, PascalCase para classes.
- **CSV:** O delimitador utilizado no salvamento é o ponto e vírgula (`;`).

## Restrições
- Não escrever código sem a aprovação prévia do `docs/spec.md` e do `docs/plano.md`.
- Uma tarefa do plano por vez, com um respectivo commit.
- Testes automatizados são obrigatórios para novas funcionalidades.