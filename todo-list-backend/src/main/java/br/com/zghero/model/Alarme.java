package br.com.zghero.model;

import java.time.LocalDateTime;

public class Alarme {
    private int minutosAntecedencia;
    private LocalDateTime momentoDisparo;
    private boolean notificado;

    // Construtor principal para novos alarmes
    public Alarme(int minutosAntecedencia, LocalDateTime momentoDisparo) {
        this.minutosAntecedencia = minutosAntecedencia;
        this.momentoDisparo = momentoDisparo;
        this.notificado = false;
    }

    // Construtor completo para recuperar dados do arquivo CSV
    public Alarme(int minutosAntecedencia, LocalDateTime momentoDisparo, boolean notificado) {
        this.minutosAntecedencia = minutosAntecedencia;
        this.momentoDisparo = momentoDisparo;
        this.notificado = notificado;
    }

    public int getMinutosAntecedencia() {
        return minutosAntecedencia;
    }

    public LocalDateTime getMomentoDisparo() {
        return momentoDisparo;
    }

    public boolean isNotificado() {
        return notificado;
    }

    public void setNotificado(boolean notificado) {
        this.notificado = notificado;
    }

    // Método auxiliar para ajudar na formatação do CSV (Tarefa 3)
    public String toCsvFormat() {
        return minutosAntecedencia + "," + momentoDisparo.toString() + "," + notificado;
    }
}