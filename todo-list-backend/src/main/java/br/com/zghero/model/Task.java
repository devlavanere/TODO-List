package br.com.zghero.model;

import java.time.LocalDate;
import java.util.UUID;

public class Task {
    private String id;
    private String nome;
    private String descricao;
    private LocalDate dataTermino;
    private int prioridade;
    private String categoria;
    private Status status;

    // Construtor para nova tarefa
    public Task(String nome, String descricao, LocalDate dataTermino, int prioridade, String categoria, Status status) {
        this.id = UUID.randomUUID().toString().substring(0,6);
        this.nome = nome;
        this.descricao = descricao;
        this.dataTermino = dataTermino;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.status = status;
    }

    // Construtor para carregar do arquivo
    public Task(String id, String nome, String descricao, LocalDate dataTermino, int prioridade, String categoria, Status status) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataTermino = dataTermino;
        this.prioridade = prioridade;
        this.categoria = categoria;
        this.status = status;
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

    public LocalDate getDataTermino() {
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

    public String toString() {
        return String.format("[ID: %S] Prio: %d | Cat: %s | Status: %s | %s - %s",
                id, prioridade, categoria, status);
    }
}
