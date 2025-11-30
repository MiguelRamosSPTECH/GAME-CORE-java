package gamecore.project.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Entidade que representa os dados de coleta de um processo em um momento específico.
 */

public class ColetaProcesso {

    private String timestamp;
    private String pid;
    private String ppid;
    private String nome_processo;
    private String status;
    private Double cpu_porcentagem;
    private Double ram_porcentagem;
    private Double total_threads;
    private Double tempo_execucao;
    private Double throughput_mbs;
    private Double throughput_gbs;

    public ColetaProcesso(String timestamp, String pid, String ppid, String nome_processo, String status, Double cpu_porcentagem, Double ram_porcentagem, Double total_threads, Double tempo_execucao, Double throughput_mbs, Double throughput_gbs) {
        this.timestamp = timestamp;
        this.pid = pid;
        this.ppid = ppid;
        this.nome_processo = nome_processo;
        this.status = status;
        this.cpu_porcentagem = cpu_porcentagem;
        this.ram_porcentagem = ram_porcentagem;
        this.total_threads = total_threads;
        this.tempo_execucao = tempo_execucao;
        this.throughput_mbs = throughput_mbs;
        this.throughput_gbs = throughput_gbs;
    }

    public ColetaProcesso() {
    }

    /**
     * Converte os detalhes do processo (excluindo timestamp e nome_processo) para um Map.
     * Este Map será o valor do objeto aninhado no JSON final.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("pid", this.pid);
        map.put("ppid", this.ppid);
        map.put("status", this.status);
        map.put("cpu_porcentagem", this.cpu_porcentagem);
        map.put("ram_porcentagem", this.ram_porcentagem);
        map.put("total_threads", this.total_threads);
        map.put("tempo_execucao", this.tempo_execucao);
        map.put("throughput_mbs", this.throughput_mbs);
        map.put("throughput_gbs", this.throughput_gbs);
        return map;
    }

    // getters e setters
    public String getTimestamp() { return timestamp; }
    public String getNome_processo() { return nome_processo; }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPpid() {
        return ppid;
    }

    public void setPpid(String ppid) {
        this.ppid = ppid;
    }

    public void setNome_processo(String nome_processo) {
        this.nome_processo = nome_processo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCpu_porcentagem() {
        return cpu_porcentagem;
    }

    public void setCpu_porcentagem(Double cpu_porcentagem) {
        this.cpu_porcentagem = cpu_porcentagem;
    }

    public Double getRam_porcentagem() {
        return ram_porcentagem;
    }

    public void setRam_porcentagem(Double ram_porcentagem) {
        this.ram_porcentagem = ram_porcentagem;
    }

    public Double getTotal_threads() {
        return total_threads;
    }

    public void setTotal_threads(Double total_threads) {
        this.total_threads = total_threads;
    }

    public Double getTempo_execucao() {
        return tempo_execucao;
    }

    public void setTempo_execucao(Double tempo_execucao) {
        this.tempo_execucao = tempo_execucao;
    }

    public Double getThroughput_mbs() {
        return throughput_mbs;
    }

    public void setThroughput_mbs(Double throughput_mbs) {
        this.throughput_mbs = throughput_mbs;
    }

    public Double getThroughput_gbs() {
        return throughput_gbs;
    }

    public void setThroughput_gbs(Double throughput_gbs) {
        this.throughput_gbs = throughput_gbs;
    }
}