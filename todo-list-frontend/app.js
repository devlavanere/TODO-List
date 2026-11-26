const STORAGE_KEY = 'zg_todo_tasks';

// Estado da aplicação
let tasks = [];
let currentFilter = 'ALL';
const selectedTaskIds = new Set();

// Elementos do DOM
const form = document.getElementById('task-form');
const taskList = document.getElementById('task-list');
const btnCancel = document.getElementById('btn-cancel');
const formTitle = document.getElementById('form-title');
const filterButtons = document.querySelectorAll('.btn-filter');
const selectAllCheckbox = document.getElementById('select-all');
const selectedCountSpan = document.getElementById('selected-count');
const bulkStatusSelect = document.getElementById('bulk-status');
const btnBulkExecute = document.getElementById('btn-bulk-execute');

// Gerador de ID simples
const generateId = () => Math.random().toString(36).substring(2, 8);

// REQUISITO K1-T6: Persistência no Local Storage
function loadTasks() {
    try {
        const stored = localStorage.getItem(STORAGE_KEY);
        tasks = stored ? JSON.parse(stored) : [];
        tasks.sort((a, b) => a.prioridade - b.prioridade);
    } catch (error) {
        console.error('Erro ao ler tarefas do localStorage:', error);
        tasks = [];
    }
}

function saveTasks() {
    tasks.sort((a, b) => a.prioridade - b.prioridade);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
}

// REQUISITO - C e U: Receber dados de todos os parâmetros e salvar (Criar ou Editar)
form.addEventListener('submit', function (e) {
    e.preventDefault();

    const idInput = document.getElementById('task-id').value;
    const taskData = {
        nome: document.getElementById('nome').value.trim(),
        descricao: document.getElementById('descricao').value.trim(),
        dataTermino: document.getElementById('data').value,
        prioridade: parseInt(document.getElementById('prioridade').value, 10),
        categoria: document.getElementById('categoria').value.trim(),
        status: document.getElementById('status').value
    };

    if (idInput) {
        const index = tasks.findIndex(t => t.id === idInput);
        if (index !== -1) {
            tasks[index] = { id: idInput, ...taskData };
        }
        resetForm();
    } else {
        taskData.id = generateId();
        tasks.push(taskData);
        form.reset();
    }

    saveTasks();
    renderTasks();
});

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
    if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
        tasks = tasks.filter(t => t.id !== id);
        selectedTaskIds.delete(id);
        saveTasks();
        renderTasks();
    }
}

// Seleção individual de tarefa
function toggleTaskSelection(id, isChecked) {
    if (isChecked) {
        selectedTaskIds.add(id);
    } else {
        selectedTaskIds.delete(id);
    }
    updateBulkControlsState();
}

// Selecionar/desmarcar todas as tarefas visíveis no filtro atual
selectAllCheckbox.addEventListener('change', (e) => {
    const visibleTasks = getFilteredTasks();
    visibleTasks.forEach(task => {
        if (e.target.checked) {
            selectedTaskIds.add(task.id);
        } else {
            selectedTaskIds.delete(task.id);
        }
    });
    renderTasks();
});

// REQUISITO K1-T6: Alteração de status de múltiplas tarefas em simultâneo
btnBulkExecute.addEventListener('click', () => {
    if (selectedTaskIds.size === 0) return;

    const targetStatus = bulkStatusSelect.value;

    // Busca as tarefas persistidas no localStorage e atualiza o status das selecionadas
    const storedTasks = JSON.parse(localStorage.getItem(STORAGE_KEY) || '[]');
    tasks = storedTasks.map(task =>
        selectedTaskIds.has(task.id)
            ? { ...task, status: targetStatus }
            : task
    );

    saveTasks();
    selectedTaskIds.clear();
    renderTasks();
});

function getFilteredTasks() {
    if (currentFilter === 'ALL') return tasks;
    return tasks.filter(t => t.status === currentFilter);
}

function updateBulkControlsState() {
    const visibleTasks = getFilteredTasks();
    const totalSelected = selectedTaskIds.size;

    selectedCountSpan.textContent = totalSelected;
    btnBulkExecute.disabled = totalSelected === 0;

    if (visibleTasks.length === 0) {
        selectAllCheckbox.checked = false;
        selectAllCheckbox.indeterminate = false;
        return;
    }

    const visibleSelectedCount = visibleTasks.filter(t => selectedTaskIds.has(t.id)).length;
    selectAllCheckbox.checked = visibleSelectedCount === visibleTasks.length;
    selectAllCheckbox.indeterminate = visibleSelectedCount > 0 && visibleSelectedCount < visibleTasks.length;
}

// Filtro por status (ALL, TODO, DOING, DONE)
filterButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        filterButtons.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        currentFilter = btn.getAttribute('data-filter');
        renderTasks();
    });
});

// REQUISITO - R: Renderiza dashboard e listagem das tarefas
function renderTasks() {
    document.getElementById('count-todo').textContent = tasks.filter(t => t.status === 'TODO').length;
    document.getElementById('count-doing').textContent = tasks.filter(t => t.status === 'DOING').length;
    document.getElementById('count-done').textContent = tasks.filter(t => t.status === 'DONE').length;

    const filteredTasks = getFilteredTasks();
    updateBulkControlsState();

    taskList.innerHTML = '';
    if (filteredTasks.length === 0) {
        taskList.innerHTML = '<p style="text-align:center; color:#666;">Nenhuma tarefa encontrada.</p>';
        return;
    }

    filteredTasks.forEach(task => {
        const isSelected = selectedTaskIds.has(task.id);
        const card = document.createElement('div');
        card.className = `task-card ${task.status.toLowerCase()} ${isSelected ? 'selected' : ''}`;
        card.innerHTML = `
            <div class="task-select">
                <input
                    type="checkbox"
                    class="task-checkbox"
                    aria-label="Selecionar tarefa ${task.nome}"
                    ${isSelected ? 'checked' : ''}
                    onchange="toggleTaskSelection('${task.id}', this.checked)"
                >
            </div>
            <div class="task-info">
                <h4>${task.nome} <small>(${task.status})</small></h4>
                <p>${task.descricao}</p>
                <p><strong>Prazo:</strong> ${task.dataTermino} | <strong>Prio:</strong> ${task.prioridade} | <strong>Cat:</strong> ${task.categoria}</p>
            </div>
            <div class="task-actions">
                <button class="btn-edit" onclick="editTask('${task.id}')">Editar</button>
                <button class="btn-delete" onclick="deleteTask('${task.id}')">Excluir</button>
            </div>
        `;
        taskList.appendChild(card);
    });
}

// Inicialização da aplicação
document.addEventListener('DOMContentLoaded', () => {
    loadTasks();
    renderTasks();
});