# ZG-Hero TODO List

Aplicação de gerenciamento de tarefas com suporte a **Alarmes de Tarefas** com antecedência customizada, persistidos em CSV. Os alarmes são verificados a cada renderização do menu CLI e notificam o usuário quando o horário de disparo (data da tarefa menos a antecedência em minutos) é atingido.

## Funcionalidades
- **CRUD de Tarefas:** Criar, listar, atualizar status e excluir tarefas.
- **Filtros:** Filtrar por categoria, prioridade, status e data.
- **Dashboard Estatístico:** Visão geral das tarefas por status (TODO, DOING, DONE).
- **Alarmes de Tarefas:** Configure múltiplos alarmes com antecedência em minutos para cada tarefa. O sistema notifica no CLI quando o momento de disparo chega e persiste o estado de notificação no CSV.

## Tecnologias Utilizadas
- **Backend:** Java 8, Gradle (Arquitetura MVC, I/O para persistência em arquivo CSV).
- **Testes:** Spock/Groovy para testes de unidade e integração.
- **Frontend:** HTML5, CSS3, JavaScript Vanilla (Manipulação de DOM, array methods, Event Listeners).

**Autor:** Michel Lavanere Sampaio