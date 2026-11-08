# Plano de Implementação: Alarmes de Tarefas

- [x] **Tarefa 1: Modelo Principal** - Alterar `dataTermino` de `LocalDate` para `LocalDateTime` em `Task` e arrumar `TaskService` (ajustar os filtros que usam data).
- [x] **Tarefa 2: Entidade Alarme** - Criar a classe `Alarme` e adicionar `List<Alarme>` na classe `Task`.
- [x] **Tarefa 3: Persistência** - Atualizar `saveToFile` e `loadFromFile` em `TaskService` para salvar e ler os novos campos do CSV.
- [x] **Tarefa 4: Menu do Usuário** - Atualizar `Main.java` (Criar Tarefa) para perguntar a hora da tarefa e se ele deseja adicionar alarmes em minutos de antecedência.
- [ ] **Tarefa 5: Gatilho de Alarme** - Criar um método `verificarAlarmes()` em `TaskService` e chamá-lo no topo do laço `while(true)` do `Main.java`.