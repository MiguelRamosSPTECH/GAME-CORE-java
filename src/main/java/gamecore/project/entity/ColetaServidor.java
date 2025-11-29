package gamecore.project.entity;

public class ColetaServidor {


    private String macAddress;
    private String timestamp;
    private Double cpu_porcentagem;
    private Double cpuOciosa;
    private Double cpuUsuario;
    private Double cpuSistema;
    private String cpuLoadAvg;
    private Double cpuFrequencia;
    private Double cpuFrequenciaMax;
    private Double cpuFrequenciaMin;
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

    public ColetaServidor() {
    }

    public ColetaServidor(String macAddress, String timestamp, Double cpu_porcentagem, Double cpuOciosa, Double cpuUsuario, Double cpuSistema, String cpuLoadAvg, Double cpuFrequencia, Double cpuFrequenciaMax, Double cpuFrequenciaMin, Double ram, Double ramMb, Double ramGb, Double ramDisponivel, Double ramDisponivelMb, Double ramDisponivelGb, Double ramSwap, Double ramSwapMb, Double ramSwapGb, Double disco, Double discoMb, Double discoGb, Double discoDisponivel, Double discoDisponivelMb, Double discoDisponivelGb, Double discoThroughputMbs, Double discoThroughputGbs, Double mbEnviados, Double mbRecebidos) {
        this.macAddress = macAddress;
        this.timestamp = timestamp;
        this.cpu_porcentagem = cpu_porcentagem;
        this.cpuOciosa = cpuOciosa;
        this.cpuUsuario = cpuUsuario;
        this.cpuSistema = cpuSistema;
        this.cpuLoadAvg = cpuLoadAvg;
        this.cpuFrequencia = cpuFrequencia;
        this.cpuFrequenciaMax = cpuFrequenciaMax;
        this.cpuFrequenciaMin = cpuFrequenciaMin;
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
                ", cpuFrequencia=" + cpuFrequencia +
                ", cpuFrequenciaMax=" + cpuFrequenciaMax +
                ", cpuFrequenciaMin=" + cpuFrequenciaMin +
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

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Double getCpu_porcentagem() {
        return cpu_porcentagem;
    }

    public void setCpu_porcentagem(Double cpu_porcentagem) {
        this.cpu_porcentagem = cpu_porcentagem;
    }

    public Double getCpuOciosa() {
        return cpuOciosa;
    }

    public void setCpuOciosa(Double cpuOciosa) {
        this.cpuOciosa = cpuOciosa;
    }

    public Double getCpuUsuario() {
        return cpuUsuario;
    }

    public void setCpuUsuario(Double cpuUsuario) {
        this.cpuUsuario = cpuUsuario;
    }

    public Double getCpuSistema() {
        return cpuSistema;
    }

    public void setCpuSistema(Double cpuSistema) {
        this.cpuSistema = cpuSistema;
    }

    public String getCpuLoadAvg() {
        return cpuLoadAvg;
    }

    public void setCpuLoadAvg(String cpuLoadAvg) {
        this.cpuLoadAvg = cpuLoadAvg;
    }

    public Double getCpuFrequencia() {
        return cpuFrequencia;
    }

    public void setCpuFrequencia(Double cpuFrequencia) {
        this.cpuFrequencia = cpuFrequencia;
    }

    public Double getCpuFrequenciaMax() {
        return cpuFrequenciaMax;
    }

    public void setCpuFrequenciaMax(Double cpuFrequenciaMax) {
        this.cpuFrequenciaMax = cpuFrequenciaMax;
    }

    public Double getCpuFrequenciaMin() {
        return cpuFrequenciaMin;
    }

    public void setCpuFrequenciaMin(Double cpuFrequenciaMin) {
        this.cpuFrequenciaMin = cpuFrequenciaMin;
    }

    public Double getRam() {
        return ram;
    }

    public void setRam(Double ram) {
        this.ram = ram;
    }

    public Double getRamMb() {
        return ramMb;
    }

    public void setRamMb(Double ramMb) {
        this.ramMb = ramMb;
    }

    public Double getRamGb() {
        return ramGb;
    }

    public void setRamGb(Double ramGb) {
        this.ramGb = ramGb;
    }

    public Double getRamDisponivel() {
        return ramDisponivel;
    }

    public void setRamDisponivel(Double ramDisponivel) {
        this.ramDisponivel = ramDisponivel;
    }

    public Double getRamDisponivelMb() {
        return ramDisponivelMb;
    }

    public void setRamDisponivelMb(Double ramDisponivelMb) {
        this.ramDisponivelMb = ramDisponivelMb;
    }

    public Double getRamDisponivelGb() {
        return ramDisponivelGb;
    }

    public void setRamDisponivelGb(Double ramDisponivelGb) {
        this.ramDisponivelGb = ramDisponivelGb;
    }

    public Double getRamSwap() {
        return ramSwap;
    }

    public void setRamSwap(Double ramSwap) {
        this.ramSwap = ramSwap;
    }

    public Double getRamSwapMb() {
        return ramSwapMb;
    }

    public void setRamSwapMb(Double ramSwapMb) {
        this.ramSwapMb = ramSwapMb;
    }

    public Double getRamSwapGb() {
        return ramSwapGb;
    }

    public void setRamSwapGb(Double ramSwapGb) {
        this.ramSwapGb = ramSwapGb;
    }

    public Double getDisco() {
        return disco;
    }

    public void setDisco(Double disco) {
        this.disco = disco;
    }

    public Double getDiscoMb() {
        return discoMb;
    }

    public void setDiscoMb(Double discoMb) {
        this.discoMb = discoMb;
    }

    public Double getDiscoGb() {
        return discoGb;
    }

    public void setDiscoGb(Double discoGb) {
        this.discoGb = discoGb;
    }

    public Double getDiscoDisponivel() {
        return discoDisponivel;
    }

    public void setDiscoDisponivel(Double discoDisponivel) {
        this.discoDisponivel = discoDisponivel;
    }

    public Double getDiscoDisponivelMb() {
        return discoDisponivelMb;
    }

    public void setDiscoDisponivelMb(Double discoDisponivelMb) {
        this.discoDisponivelMb = discoDisponivelMb;
    }

    public Double getDiscoDisponivelGb() {
        return discoDisponivelGb;
    }

    public void setDiscoDisponivelGb(Double discoDisponivelGb) {
        this.discoDisponivelGb = discoDisponivelGb;
    }

    public Double getDiscoThroughputMbs() {
        return discoThroughputMbs;
    }

    public void setDiscoThroughputMbs(Double discoThroughputMbs) {
        this.discoThroughputMbs = discoThroughputMbs;
    }

    public Double getDiscoThroughputGbs() {
        return discoThroughputGbs;
    }

    public void setDiscoThroughputGbs(Double discoThroughputGbs) {
        this.discoThroughputGbs = discoThroughputGbs;
    }

    public Double getMbEnviados() {
        return mbEnviados;
    }

    public void setMbEnviados(Double mbEnviados) {
        this.mbEnviados = mbEnviados;
    }

    public Double getMbRecebidos() {
        return mbRecebidos;
    }

    public void setMbRecebidos(Double mbRecebidos) {
        this.mbRecebidos = mbRecebidos;
    }
}
