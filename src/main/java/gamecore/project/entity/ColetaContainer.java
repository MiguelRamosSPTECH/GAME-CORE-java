package gamecore.project.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Entidade que representa os dados de coleta de um Container em um momento específico.
 */
public class ColetaContainer {
    private String identificacao_container;
    private String timestamp;
    private Double cpu_container;
    private Double throughput_container;
    private Double ram_container;
    private Double throttled_time_container;
    private Double tps_container;

    // Construtor padrão (NECESSÁRIO para Jackson/CsvMapper)
    public ColetaContainer() {}

    // Construtor completo
    public ColetaContainer(String identificacao_container, String timestamp, Double cpu_container, Double throughput_container, Double ram_container, Double throttled_time_container, Double tps_container) {
        this.identificacao_container = identificacao_container;
        this.timestamp = timestamp;
        this.cpu_container = cpu_container;
        this.throughput_container = throughput_container;
        this.ram_container = ram_container;
        this.throttled_time_container = throttled_time_container;
        this.tps_container = tps_container;
    }

    /**
     * Converte os detalhes do container (excluindo timestamp) para um Map.
     * Útil para aninhamento no JSON final.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("identificacao_container", this.identificacao_container);
        map.put("cpu_container", this.cpu_container);
        map.put("throughput_container", this.throughput_container);
        map.put("ram_container", this.ram_container);
        map.put("throttled_time_container", this.throttled_time_container);
        map.put("tps_container", this.tps_container);
        return map;
    }

    // --- Getters e Setters ---

    public String getIdentificacao_container() {
        return identificacao_container;
    }

    public void setIdentificacao_container(String identificacao_container) {
        this.identificacao_container = identificacao_container;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getCpu_container() {
        return cpu_container;
    }

    public void setCpu_container(Double cpu_container) {
        this.cpu_container = cpu_container;
    }

    public Double getThroughput_container() {
        return throughput_container;
    }

    public void setThroughput_container(Double throughput_container) {
        this.throughput_container = throughput_container;
    }

    public Double getRam_container() {
        return ram_container;
    }

    public void setRam_container(Double ram_container) {
        this.ram_container = ram_container;
    }

    public Double getThrottled_time_container() {
        return throttled_time_container;
    }

    public void setThrottled_time_container(Double throttled_time_container) {
        this.throttled_time_container = throttled_time_container;
    }

    public Double getTps_container() {
        return tps_container;
    }

    public void setTps_container(Double tps_container) {
        this.tps_container = tps_container;
    }

    @Override
    public String toString() {
        return "gamecore.project.Entity.ColetaContainer{" +
                "identificacao_container='" + identificacao_container + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", cpu_container=" + cpu_container +
                ", throughput_container=" + throughput_container +
                ", ram_container=" + ram_container +
                ", throttled_time_container=" + throttled_time_container +
                ", tps_container=" + tps_container +
                '}';
    }
}