public class ColetaContainer {
    String identificacao_container;
    String timestamp;
    Double cpu_container;
    Double throughput_container;
    Double ram_container;
    Double throttled_time_container;
    Double tps_container;

    public ColetaContainer(String identificacao_container, String timestamp, Double cpu_container, Double throughput_container, Double ram_container, Double throttled_time_container, Double tps_container) {
        this.identificacao_container = identificacao_container;
        this.timestamp = timestamp;
        this.cpu_container = cpu_container;
        this.throughput_container = throughput_container;
        this.ram_container = ram_container;
        this.throttled_time_container = throttled_time_container;
        this.tps_container = tps_container;
    }

    @Override
    public String toString() {
        return "ColetaContainer{" +
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
