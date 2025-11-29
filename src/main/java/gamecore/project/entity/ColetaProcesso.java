package gamecore.project.entity;


import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        // Note que 'timestamp' e 'nome_processo' são omitidos pois virarão chaves no JSON final.
        map.put("pid", this.pid);
        map.put("ppid", this.ppid);
        map.put("status", this.status);
        map.put("cpu_porcentagem", this.cpu_porcentagem);
        map.put("ram_porcentagem", this.ram_porcentagem);
        map.put("total_threads", this.total_threads);
        map.put("tempo_execucao", this.tempo_execucao);
        map.put("throughput_mbs", this.throughput_mbs);
        map.put("throughput_gbs", this.throughput_gbs); // Usando 'throughput_gbs' para corresponder ao JSON
        return map;
    }



    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getPpid() {
        return ppid;
    }

    public void setPpid(Integer ppid) {
        this.ppid = ppid;
    }

    public String getNome_processo() {
        return nome_processo;
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

    public Integer getTotal_threads() {
        return total_threads;
    }

    public void setTotal_threads(Integer total_threads) {
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

    public void setThroughput_gbs(Double throughput_gbx) {
        this.throughput_gbs = throughput_gbx;
    }

    @Override
    public String toString() {
        return "ColetaProcesso{" +
                "timestamp='" + timestamp + '\'' +
                ", pid=" + pid +
                ", ppid=" + ppid +
                ", nome_processo='" + nome_processo + '\'' +
                ", status='" + status + '\'' +
                ", cpu_porcentagem=" + cpu_porcentagem +
                ", ram_porcentagem=" + ram_porcentagem +
                ", total_threads=" + total_threads +
                ", tempo_execucao=" + tempo_execucao +
                ", throughput_mbs=" + throughput_mbs +
                ", throughput_gbx=" + throughput_gbs +
                '}';
    }
}
