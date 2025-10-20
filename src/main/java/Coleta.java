public class Coleta {
    private String user;
    private String timestamp;
    private Double cpu_porcentagem;
    private Double cpuOciosa;
    private Double cpuUsuario;
    private Double cpuSistema;
    private String cpuLoadAvg;
    private Double ram;
    private Double ramMb;
    private Double ramGb;
    private Double ramDisponivel;
    private Double ramDisponivelMb;
    private Double ramDisponivelGb;
    private Double ramSwap;
    private Double ramSwapMb;
    private Double ramSwapGb;
    private Double disco;
    private Double discoMb;
    private Double discoGb;
    private Double discoDisponivel;
    private Double discoDisponivelMb;
    private Double discoDisponivelGb;
    private Double discoThroughputMbs;
    private Double discoThroughputGbs;

    public Coleta(String user, String timestamp, Double cpu_porcentagem, Double cpuOciosa, Double cpuUsuario, Double cpuSistema, String cpuLoadAvg, Double ram, Double ramMb, Double ramGb, Double ramDisponivel, Double ramDisponivelMb, Double ramDisponivelGb, Double ramSwap, Double ramSwapMb, Double ramSwapGb, Double disco, Double discoMb, Double discoGb, Double discoDisponivel, Double discoDisponivelMb, Double discoDisponivelGb, Double discoThroughputMbs, Double discoThroughputGbs) {
        this.user = user;
        this.timestamp = timestamp;
        this.cpu_porcentagem = cpu_porcentagem;
        this.cpuOciosa = cpuOciosa;
        this.cpuUsuario = cpuUsuario;
        this.cpuSistema = cpuSistema;
        this.cpuLoadAvg = cpuLoadAvg;
        this.ram = ram;
        this.ramMb = ramMb;
        this.ramGb = ramGb;
        this.ramDisponivel = ramDisponivel;
        this.ramDisponivelMb = ramDisponivelMb;
        this.ramDisponivelGb = ramDisponivelGb;
        this.ramSwap = ramSwap;
        this.ramSwapMb = ramSwapMb;
        this.ramSwapGb = ramSwapGb;
        this.disco = disco;
        this.discoMb = discoMb;
        this.discoGb = discoGb;
        this.discoDisponivel = discoDisponivel;
        this.discoDisponivelMb = discoDisponivelMb;
        this.discoDisponivelGb = discoDisponivelGb;
        this.discoThroughputMbs = discoThroughputMbs;
        this.discoThroughputGbs = discoThroughputGbs;
    }

    @Override
    public String toString() {
        return "Coleta{" +
                "user='" + user + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", cpu_porcentagem=" + cpu_porcentagem +
                ", cpuOciosa=" + cpuOciosa +
                ", cpuUsuario=" + cpuUsuario +
                ", cpuSistema=" + cpuSistema +
                ", cpuLoadAvg=" + cpuLoadAvg +
                ", ram=" + ram +
                ", ramMb=" + ramMb +
                ", ramGb=" + ramGb +
                ", ramDisponivel=" + ramDisponivel +
                ", ramDisponivelMb=" + ramDisponivelMb +
                ", ramDisponivelGb=" + ramDisponivelGb +
                ", ramSwap=" + ramSwap +
                ", ramSwapMb=" + ramSwapMb +
                ", ramSwapGb=" + ramSwapGb +
                ", disco=" + disco +
                ", discoMb=" + discoMb +
                ", discoGb=" + discoGb +
                ", discoDisponivel=" + discoDisponivel +
                ", discoDisponivelMb=" + discoDisponivelMb +
                ", discoDisponivelGb=" + discoDisponivelGb +
                ", discoThroughputMbs=" + discoThroughputMbs +
                ", discoThroughputGbs=" + discoThroughputGbs +
                '}';
    }
}
