package org.example;

import java.sql.Timestamp;
import java.time.Instant;

public class Logs {

    private String nome;
    private Timestamp dataHora;
    private Double cpu;
    private Double ram;
    private Double disco;

    public Logs(String nome) {
        this.nome = nome;
        this.dataHora = Timestamp.from(Instant.now());
        this.cpu = 0.0;
        this.ram = 0.0;
        this.disco = 0.0;
    }

    public void aumentarCpu(Double valor) {
        if (valor == null || valor <= 0) return;
        this.cpu = Math.min(100.0, this.cpu + valor);
    }

    public void diminuirCpu(Double valor) {
        if (valor == null || valor <= 0) return;
        this.cpu = Math.max(0.0, this.cpu - valor);
    }

    // RAM
    public void aumentarRam(Double valor) {
        if (valor == null || valor <= 0) return;
        this.ram = Math.min(100.0, this.ram + valor);
    }

    public void diminuirRam(Double valor) {
        if (valor == null || valor <= 0) return;
        this.ram = Math.max(0.0, this.ram - valor);
    }

    // Disco
    public void aumentarDisco(Double valor) {
        if (valor == null || valor <= 0) return;
        this.disco = Math.min(100.0, this.disco + valor);
    }

    public void diminuirDisco(Double valor) {
        if (valor == null || valor <= 0) return;
        this.disco = Math.max(0.0, this.disco - valor);
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public double getCpu() {
        return cpu;
    }

    public double getRam() {
        return ram;
    }

    public double getDisco() {
        return disco;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDataHora(Timestamp dataHora) {
        this.dataHora = dataHora;
    }
}
