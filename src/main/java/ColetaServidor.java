public class ColetaServidor {
    private String macAddress;
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
    private Double mbEnviados;
    private Double mbRecebidos;


    public ColetaServidor(String macAddress, String timestamp, Double cpu_porcentagem, Double cpuOciosa, Double cpuUsuario, Double cpuSistema, String cpuLoadAvg, Double ram, Double ramMb, Double ramGb, Double ramDisponivel, Double ramDisponivelMb, Double ramDisponivelGb, Double ramSwap, Double ramSwapMb, Double ramSwapGb, Double disco, Double discoMb, Double discoGb, Double discoDisponivel, Double discoDisponivelMb, Double discoDisponivelGb, Double discoThroughputMbs, Double discoThroughputGbs, Double mbEnviados, Double mbRecebidos ) {
        this.macAddress = macAddress;
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
        this.mbEnviados = mbEnviados;
        this.mbRecebidos = mbRecebidos;
    }

    @Override
    public String toString() {
        return "ColetaServidor{" +
                "macAddress='" + macAddress + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", cpu_porcentagem=" + cpu_porcentagem +
                ", cpuOciosa=" + cpuOciosa +
                ", cpuUsuario=" + cpuUsuario +
                ", cpuSistema=" + cpuSistema +
                ", cpuLoadAvg='" + cpuLoadAvg + '\'' +
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
                ", mbEnviados=" + mbEnviados +
                ", mbRecebidos=" + mbRecebidos +
                '}';
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Double getCpu_porcentagem() {
        return cpu_porcentagem;
    }

    public Double getCpuOciosa() {
        return cpuOciosa;
    }

    public Double getCpuUsuario() {
        return cpuUsuario;
    }

    public Double getCpuSistema() {
        return cpuSistema;
    }

    public String getCpuLoadAvg() {
        return cpuLoadAvg;
    }

    public Double getRam() {
        return ram;
    }

    public Double getRamMb() {
        return ramMb;
    }

    public Double getRamGb() {
        return ramGb;
    }

    public Double getRamDisponivel() {
        return ramDisponivel;
    }

    public Double getRamDisponivelMb() {
        return ramDisponivelMb;
    }

    public Double getRamDisponivelGb() {
        return ramDisponivelGb;
    }

    public Double getRamSwap() {
        return ramSwap;
    }

    public Double getRamSwapMb() {
        return ramSwapMb;
    }

    public Double getRamSwapGb() {
        return ramSwapGb;
    }

    public Double getDisco() {
        return disco;
    }

    public Double getDiscoMb() {
        return discoMb;
    }

    public Double getDiscoGb() {
        return discoGb;
    }

    public Double getDiscoDisponivel() {
        return discoDisponivel;
    }

    public Double getDiscoDisponivelMb() {
        return discoDisponivelMb;
    }

    public Double getDiscoDisponivelGb() {
        return discoDisponivelGb;
    }

    public Double getDiscoThroughputMbs() {
        return discoThroughputMbs;
    }

    public Double getDiscoThroughputGbs() {
        return discoThroughputGbs;
    }

    public Double getMbEnviados() {
        return mbEnviados;
    }

    public Double getMbRecebidos() {
        return mbRecebidos;
    }
}
