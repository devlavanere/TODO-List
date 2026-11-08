package br.com.zghero.model

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class TaskSpec extends Specification {

    def "adicionarAlarme deve calcular o momentoDisparo corretamente"() {
        given: "uma tarefa com data de término definida"
        LocalDateTime dataTermino = LocalDateTime.of(2026, 11, 10, 14, 0, 0)
        Task task = new Task("Reunião", "Alinhamento semanal", dataTermino, 1, "Trabalho", Status.TODO)

        when: "adiciono um alarme com 30 minutos de antecedência"
        task.adicionarAlarme(30)

        then: "o alarme é adicionado e o momentoDisparo é calculado corretamente"
        task.getAlarmes().size() == 1
        def alarme = task.getAlarmes().get(0)
        alarme.getMomentoDisparo() == LocalDateTime.of(2026, 11, 10, 13, 30, 0)
        alarme.getMinutosAntecedencia() == 30
    }

    def "adicionarAlarme deve criar alarme com notificado igual a false"() {
        given: "uma tarefa com data de término definida"
        LocalDateTime dataTermino = LocalDateTime.of(2026, 11, 10, 10, 0, 0)
        Task task = new Task("Entrega", "Entregar relatório", dataTermino, 2, "Trabalho", Status.TODO)

        when: "adiciono um alarme com 60 minutos de antecedência"
        task.adicionarAlarme(60)

        then: "o alarme nasce com notificado como false"
        task.getAlarmes().size() == 1
        !task.getAlarmes().get(0).isNotificado()
    }

    def "adicionarAlarme deve suportar múltiplos alarmes com diferentes antecedentes"() {
        given: "uma tarefa com data de término definida"
        LocalDateTime dataTermino = LocalDateTime.of(2026, 11, 10, 12, 0, 0)
        Task task = new Task("Apresentação", "Apresentar projeto", dataTermino, 3, "Trabalho", Status.TODO)

        when: "adiciono três alarmes com diferentes antecedentes"
        task.adicionarAlarme(15)
        task.adicionarAlarme(30)
        task.adicionarAlarme(60)

        then: "todos os alarmes são adicionados com os momentos de disparo corretos"
        task.getAlarmes().size() == 3

        task.getAlarmes().get(0).getMomentoDisparo() == LocalDateTime.of(2026, 11, 10, 11, 45, 0)
        task.getAlarmes().get(0).getMinutosAntecedencia() == 15
        !task.getAlarmes().get(0).isNotificado()

        task.getAlarmes().get(1).getMomentoDisparo() == LocalDateTime.of(2026, 11, 10, 11, 30, 0)
        task.getAlarmes().get(1).getMinutosAntecedencia() == 30
        !task.getAlarmes().get(1).isNotificado()

        task.getAlarmes().get(2).getMomentoDisparo() == LocalDateTime.of(2026, 11, 10, 11, 0, 0)
        task.getAlarmes().get(2).getMinutosAntecedencia() == 60
        !task.getAlarmes().get(2).isNotificado()
    }

    def "adicionarAlarme deve lidar com antecedência de zero minutos"() {
        given: "uma tarefa com data de término definida"
        LocalDateTime dataTermino = LocalDateTime.of(2026, 11, 10, 15, 0, 0)
        Task task = new Task("Prazo Final", "Prazo limite", dataTermino, 5, "Urgente", Status.TODO)

        when: "adiciono um alarme com 0 minutos de antecedência"
        task.adicionarAlarme(0)

        then: "o momentoDisparo é igual à data de término"
        task.getAlarmes().size() == 1
        task.getAlarmes().get(0).getMomentoDisparo() == dataTermino
        !task.getAlarmes().get(0).isNotificado()
    }

    def "nova tarefa deve nascer com lista de alarmes vazia"() {
        given: "uma nova tarefa criada"
        LocalDateTime dataTermino = LocalDateTime.of(2026, 11, 10, 10, 0, 0)
        Task task = new Task("Tarefa Simples", "Descrição", dataTermino, 1, "Geral", Status.TODO)

        expect: "a lista de alarmes está vazia"
        task.getAlarmes().isEmpty()
    }
}
