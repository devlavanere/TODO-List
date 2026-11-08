package br.com.zghero.service

import br.com.zghero.model.Status
import br.com.zghero.model.Task
import spock.lang.Specification

import java.time.LocalDateTime

class TaskServiceSpec extends Specification {

    def setup() {
        // Limpa o CSV antes de cada teste para isolar os dados
        def file = new File("tasks.csv")
        if (file.exists()) {
            file.text = ""
        }
    }

    def "filterByPriority deve retornar apenas tarefas com a prioridade especificada"() {
        given: "um serviço com tarefas de diferentes prioridades"
        TaskService service = new TaskService()

        LocalDateTime dt = LocalDateTime.of(2026, 11, 10, 10, 0, 0)
        service.addTask(new Task("Tarefa Alta", "Prioridade 5", dt, 5, "Trabalho", Status.TODO))
        service.addTask(new Task("Tarefa Baixa", "Prioridade 1", dt, 1, "Pessoal", Status.TODO))
        service.addTask(new Task("Tarefa Média", "Prioridade 3", dt, 3, "Estudo", Status.TODO))
        service.addTask(new Task("Outra Alta", "Prioridade 5", dt, 5, "Urgente", Status.DOING))

        when: "filtro por prioridade 5"
        def resultado = service.filterByPriority(5)

        then: "apenas as duas tarefas de prioridade 5 são retornadas"
        resultado.size() == 2
        resultado.every { it.getPrioridade() == 5 }
        resultado.any { it.getNome() == "Tarefa Alta" }
        resultado.any { it.getNome() == "Outra Alta" }
    }

    def "filterByPriority deve retornar lista vazia quando não há tarefas com a prioridade"() {
        given: "um serviço com tarefas de prioridades conhecidas"
        TaskService service = new TaskService()

        LocalDateTime dt = LocalDateTime.of(2026, 11, 10, 10, 0, 0)
        service.addTask(new Task("Tarefa 1", "Desc", dt, 1, "Geral", Status.TODO))
        service.addTask(new Task("Tarefa 2", "Desc", dt, 2, "Geral", Status.TODO))

        when: "filtro por prioridade 99 que não existe"
        def resultado = service.filterByPriority(99)

        then: "a lista retornada está vazia"
        resultado.isEmpty()
    }

    def "filterByCategory deve retornar apenas tarefas da categoria especificada"() {
        given: "um serviço com tarefas de diferentes categorias"
        TaskService service = new TaskService()

        LocalDateTime dt = LocalDateTime.of(2026, 11, 10, 10, 0, 0)
        service.addTask(new Task("Reunião", "Alinhamento", dt, 3, "Trabalho", Status.TODO))
        service.addTask(new Task("Academia", "Treino de perna", dt, 2, "Saúde", Status.TODO))
        service.addTask(new Task("Projeto", "Estudar Java", dt, 4, "Trabalho", Status.DOING))
        service.addTask(new Task("Compras", "Supermercado", dt, 1, "Pessoal", Status.TODO))

        when: "filtro por categoria Trabalho"
        def resultado = service.filterByCategory("Trabalho")

        then: "apenas as duas tarefas da categoria Trabalho são retornadas"
        resultado.size() == 2
        resultado.every { it.getCategoria().equalsIgnoreCase("Trabalho") }
        resultado.any { it.getNome() == "Reunião" }
        resultado.any { it.getNome() == "Projeto" }
    }

    def "filterByCategory deve ser insensível a maiúsculas e minúsculas"() {
        given: "um serviço com uma tarefa na categoria Trabalho"
        TaskService service = new TaskService()

        LocalDateTime dt = LocalDateTime.of(2026, 11, 10, 10, 0, 0)
        service.addTask(new Task("Reunião", "Alinhamento", dt, 3, "Trabalho", Status.TODO))

        when: "filtro por categoria com letras minúsculas"
        def resultado = service.filterByCategory("trabalho")

        then: "a tarefa é encontrada mesmo com a diferença de caso"
        resultado.size() == 1
        resultado.get(0).getNome() == "Reunião"
    }

    def "filterByCategory deve retornar lista vazia para categoria inexistente"() {
        given: "um serviço com tarefas de categorias conhecidas"
        TaskService service = new TaskService()

        LocalDateTime dt = LocalDateTime.of(2026, 11, 10, 10, 0, 0)
        service.addTask(new Task("Tarefa 1", "Desc", dt, 1, "Geral", Status.TODO))

        when: "filtro por categoria que não existe"
        def resultado = service.filterByCategory("Financeiro")

        then: "a lista retornada está vazia"
        resultado.isEmpty()
    }
}
