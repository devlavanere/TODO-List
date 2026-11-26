# ZG Hero - TODO List (Frontend MVP)

Aplicação web de gerenciamento de tarefas desenvolvida durante a trilha **Acelera ZG (ACZG 10)**, com foco em operações CRUD completas, persistência de dados no navegador via `localStorage` e atualização simultânea de status em lote (**Desafio K1-T6**).

---

## Funcionalidades

### 1. CRUD Completo de Tarefas
- **Criação e Edição:** Cadastro de tarefas contendo **Nome**, **Descrição**, **Data de Término**, **Prioridade** (1 a 5), **Categoria** e **Status** (`TODO`, `DOING`, `DONE`).
- **Ordenação Automática:** As tarefas são automaticamente ordenadas por nível de prioridade a cada inserção ou atualização.
- **Exclusão:** Remoção individual de tarefas com diálogo de confirmação.

### 2. Persistência no `localStorage` (Desafio K1-T6)
- Todas as alterações (criação, edição, exclusão e mudança de status em lote) são serializadas em JSON e armazenadas sob a chave `zg_todo_tasks` no `localStorage` do navegador.
- Ao recarregar ou fechar a página, o estado completo da lista e os indicadores do dashboard são restaurados automaticamente.

### 3. Atualização de Status em Lote
- **Seleção Individual e Global:** Checkboxes individuais por card de tarefa e opção **"Selecionar todas"** sincronizada com o filtro ativo.
- **Execução Simultânea:** Barra de ações em lote com seletor de status de destino (`TODO`, `DOING`, `DONE`) e botão **Executar**, que localiza os itens marcados no `localStorage`, atualiza seus status simultaneamente e persiste o novo estado.

### 4. Dashboard e Filtros Dinâmicos
- Contadores em tempo real no cabeçalho exibindo o total de tarefas em `TODO`, `DOING` e `DONE`.
- Filtros rápidos para visualizar **Todas** as tarefas ou filtrar por status específico.

---

## Estrutura de Arquivos

```text
todo-list-frontend/
├── index.html    # Estrutura semântica, formulário, barra de ações em lote e listagem
├── style.css     # Estilização responsiva com Flexbox e indicadores visuais por status
└── app.js        # Lógica de CRUD, seleção múltipla e sincronização com localStorage

```
***Autor: Michel Lavanere Sampaio***