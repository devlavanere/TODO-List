package br.com.zghero.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Task {
    private String id;
    private String nome;
    private String descricao;
    private LocalDateTime dataTermino;
    private int prioridade;
    private String categoria;
    private Status status;
    private List<Alarme> alarmes;

    public Task(String nome, String descricao, LocalDateTime dataTermino, int prioridade, String categoria, Status status) {
        this.id = UUID.randomUUID().toString().substring(0,6);
        this.nome = nome;
        this.descricao = descricao;
        this.dataTermino = dataTermino;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.status = status;
        this.alarmes = new ArrayList<>();
    }

    // Construtor para carregar do arquivo
    public Task(String id, String nome, String descricao, LocalDateTime dataTermino, int prioridade, String categoria, Status status) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataTermino = dataTermino;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.status = status;
        this.alarmes = new ArrayList<>();
    }

    public String getId() {

        return id;
    }

    public String getNome() {

        return nome;
    }

    public String getDescricao() {

        return descricao;
    }

    public LocalDateTime getDataTermino() {

        return dataTermino;
    }

    public int getPrioridade() {

        return prioridade;
    }

    public String getCategoria() {

        return categoria;
    }

    public Status getStatus() {

        return status;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public List<Alarme> getAlarmes() {
        return alarmes;
    }

    public void adicionarAlarme(int minutosAntecedencia) {
        // Calcula o momento do disparo subtraindo os minutos da data de término
        LocalDateTime momentoDisparo = this.dataTermino.minusMinutes(minutosAntecedencia);
        this.alarmes.add(new Alarme(minutosAntecedencia, momentoDisparo));
    }

    // Método auxiliar para setar a lista de alarmes (usado ao ler do CSV na Tarefa 3)
    public void setAlarmes(List<Alarme> alarmes) {
        this.alarmes = alarmes;
    }

    // Atualiza para ver tem alarme
    public String toString() {
        String infoAlarme = alarmes.isEmpty() ? "" : " [Alarmes: " + alarmes.size() + "]";
        return "[ID: " + id + "] Prio: " + prioridade + " | Cat: " + categoria +
                " | Data: " + dataTermino.toLocalDate() + infoAlarme + " | Status: " + status + " | " + nome + " - " + descricao;
    }

}
