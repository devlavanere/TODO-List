# Especificação: Alarmes de Tarefas

## Contexto e Objetivo
Permitir que os usuários do TODO List configurem alarmes para suas tarefas. O sistema deverá notificar o usuário (na interface CLI) quando o horário da tarefa atrelado à antecedência chegar.

## Regras de Negócio
1. A data da tarefa muda de `LocalDate` para `LocalDateTime` (exigindo data e hora).
2. Uma tarefa terá uma lista de alarmes (uma classe separada `Alarme` contendo a antecedência em minutos, o momento do disparo em LocalDateTime e um status boolean `notificado`).
3. O sistema verificará alarmes sempre que o Menu/Dashboard for impresso na tela.
4. Se o momento atual for igual ou superior à data/hora de disparo de um alarme não notificado (e a tarefa não for `DONE`), um aviso é exibido e o alarme é marcado como `notificado = true`.

## Critérios de Aceite
- Ao abrir a aplicação, se houver um alarme pendente para aquele momento, o sistema deve imprimir o aviso na tela.
- Após exibir o alarme uma vez, se o usuário atualizar a tela, o alarme não deve "apitar" novamente.
- Tarefas concluídas (Status.DONE) não disparam alarmes, mesmo que o horário chegue.