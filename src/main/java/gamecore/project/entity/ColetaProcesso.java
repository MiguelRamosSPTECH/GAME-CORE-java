package gamecore.project.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Entidade que representa os dados de coleta de um processo em um momento específico.
 */
public class ColetaProcesso {

    private String timestamp;
    private Integer pid;
    private Integer ppid;
    private String nome_processo;
    private String status;
    private Double cpu_porcentagem;
    private Double ram_porcentagem;
    private Integer total_threads;
    private Double tempo_execucao;
    private Double throughput_mbs;
    private Double throughput_gbs;

    public ColetaProcesso(String timestamp, Integer pid, Integer ppid, String nome_processo, String status, Double cpu_porcentagem, Double ram_porcentagem, Integer total_threads, Double tempo_execucao, Double throughput_mbs, Double throughput_gbs) {
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

    // Getters e Setters (omitidos para brevidade, mas presentes no seu código)
    public String getTimestamp() { return timestamp; }
    public String getNome_processo() { return nome_processo; }
    // ...
    // (Demais getters e setters)
    // ...
}