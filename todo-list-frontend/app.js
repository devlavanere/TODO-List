// REQUISITO: Salvar dados recebidos (ainda sem persistência definitiva)
let tasks = [];
let currentFilter = 'ALL';

// Elementos do DOM
const form = document.getElementById('task-form');
const taskList = document.getElementById('task-list');
const btnCancel = document.getElementById('btn-cancel');
const formTitle = document.getElementById('form-title');
const filterButtons = document.querySelectorAll('.btn-filter');

// Gerador de ID simples
const generateId = () => Math.random().toString(36).substr(2, 6);

// REQUISITO - C e U: Receber dados de todos os parâmetros e salvar (Criar ou Editar)
form.addEventListener('submit', function(e) {
    e.preventDefault(); // Impede a página de recarregar

    const idInput = document.getElementById('task-id').value;
    const taskData = {
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        dataTermino: document.getElementById('data').value,
        prioridade: parseInt(document.getElementById('prioridade').value),
        categoria: document.getElementById('categoria').value,
        status: document.getElementById('status').value
    };

    if (idInput) {
        // REQUISITO - U: Salvar a edição de uma tarefa já criada
        const index = tasks.findIndex(t => t.id === idInput);
        if (index !== -1) {
            tasks[index] = { id: idInput, ...taskData };
        }
        resetForm();
    } else {
        // REQUISITO - C: Criar nova tarefa
        taskData.id = generateId();
        tasks.push(taskData);
        form.reset();
    }

    // Balancea por prioridade (mantendo a regra de negócio do Java)
    tasks.sort((a, b) => a.prioridade - b.prioridade);
    renderTasks();
});

// Botão de cancelar edição
btnCancel.addEventListener('click', resetForm);

function resetForm() {
    form.reset();
    document.getElementById('task-id').value = '';
    formTitle.textContent = 'Nova Tarefa';
    btnCancel.style.display = 'none';
    document.getElementById('btn-submit').textContent = 'Salvar Tarefa';
}

// REQUISITO - U: Permite que uma tarefa já criada seja editada
function editTask(id) {
    const task = tasks.find(t => t.id === id);
    if (!task) return;

    // Preenche o formulário com os dados existentes
    document.getElementById('task-id').value = task.id;
    document.getElementById('nome').value = task.nome;
    document.getElementById('descricao').value = task.descricao;
    document.getElementById('data').value = task.dataTermino;
    document.getElementById('prioridade').value = task.prioridade;
    document.getElementById('categoria').value = task.categoria;
    document.getElementById('status').value = task.status;

    formTitle.textContent = 'Editar Tarefa';
    btnCancel.style.display = 'inline-block';
    document.getElementById('btn-submit').textContent = 'Atualizar Tarefa';
}

// REQUISITO - D: Inserir uma opção para excluir Tarefa
function deleteTask(id) {
    if(confirm('Tem certeza que deseja excluir esta tarefa?')) {
        tasks = tasks.filter(t => t.id !== id);
        renderTasks();
    }
}

// REQUISITO OPCIONAL: Filtra a lista de tarefas quanto ao status (TODO, DOING, DONE)
filterButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        filterButtons.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        currentFilter = btn.getAttribute('data-filter');
        renderTasks();
    });
});

// REQUISITO - R: Campo na página para listagem das tarefas
function renderTasks() {
    // Atualiza dashboard dinamicamente
    document.getElementById('count-todo').textContent = tasks.filter(t => t.status === 'TODO').length;
    document.getElementById('count-doing').textContent = tasks.filter(t => t.status === 'DOING').length;
    document.getElementById('count-done').textContent = tasks.filter(t => t.status === 'DONE').length;

    // Aplica o filtro atual antes de desenhar
    let filteredTasks = tasks;
    if (currentFilter !== 'ALL') {
        filteredTasks = tasks.filter(t => t.status === currentFilter);
    }

    // Desenha a lista
    taskList.innerHTML = '';
    if (filteredTasks.length === 0) {
        taskList.innerHTML = '<p style="text-align:center; color:#666;">Nenhuma tarefa encontrada.</p>';
        return;
    }

    filteredTasks.forEach(task => {
        const card = document.createElement('div');
        card.className = `task-card ${task.status.toLowerCase()}`;
        card.innerHTML = `
            <div class="task-info">
                <h4>${task.nome} <small>(${task.status})</small></h4>
                <p>${task.descricao}</p>
                <p><strong>Prazo:</strong> ${task.dataTermino} | <strong>Prio:</strong> ${task.prioridade} | <strong>Cat:</strong> ${task.categoria}</p>
            </div>
            <div class="task-actions">
                <!-- Aciona os métodos de Update e Delete -->
                <button class="btn-edit" onclick="editTask('${task.id}')">Editar</button>
                <button class="btn-delete" onclick="deleteTask('${task.id}')">Excluir</button>
            </div>
        `;
        taskList.appendChild(card);
    });
}