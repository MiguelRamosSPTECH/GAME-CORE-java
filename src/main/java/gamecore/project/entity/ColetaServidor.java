package gamecore.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColetaServidor {

    @JsonProperty("macadress")
    private String macAddress;

    @JsonProperty("timestamp")
    private String timestamp;

    // Campos de Métricas (Mapeados do CSV)
    @JsonProperty("cpu_porcentagem")
    private Double cpu;
    @JsonProperty("cpu_ociosa_porcentagem")
    private Double cpuOciosaPorcentagem;
    @JsonProperty("cpu_usuarios_porcentagem")
    private Double cpuUsuariosPorcentagem;
    @JsonProperty("cpu_sistema_porcentagem")
    private Double cpuSistemaPorcentagem;
    @JsonProperty("cpu_loadavg")
    private String cpuLoadAvg;
    @JsonProperty("cpu_freq_mhz")
    private Double cpuFrequencia;
    @JsonProperty("cpu_freq_max_mhz")
    private Double cpuFrequenciaMax;
    @JsonProperty("cpu_freq_min_mhz")
    private Double cpuFrequenciaMin;
    @JsonProperty("ram_porcentagem")
    private Double ramPorcentagem;
    @JsonProperty("ram_mb")
    private Double ramMb;
    @JsonProperty("ram_gb")
    private Double ramGb;
    @JsonProperty("ram_disp_porcentagem")
    private Double ramDisponivel; // Recebe ram_disp_porcentagem
    @JsonProperty("ram_disp_mb")
    private Double ramDisponivelMb;
    @JsonProperty("ram_disp_gb")
    private Double ramDisponivelGb;
    @JsonProperty("ram_swap_porcentagem")
    private Double ramSwapPorcentagem;
    @JsonProperty("ram_swap_mb")
    private Double ramSwapMb;
    @JsonProperty("ram_swap_gb")
    private Double ramSwapGb;
    @JsonProperty("disco_porcentagem")
    private Double disco; // % utilizada
    @JsonProperty("disco_gb")
    private Double discoGb;
    @JsonProperty("disco_livre_porcentagem")
    private Double discoDisponivel; // % livre
    @JsonProperty("disco_livre_mb")
    private Double discoDisponivelMb;
    @JsonProperty("disco_livre_gb")
    private Double discoDisponivelGb;
    @JsonProperty("disco_throughput_mbs")
    private Double discoThroughputMbs;
    @JsonProperty("disco_throughput_gbs")
    private Double discoThroughputGbs;
    @JsonProperty("rede_enviados_mb_")
    private Double mbEnviados;
    @JsonProperty("rede_recebidos_mb")
    private Double mbRecebidos;
    @JsonProperty("temperatura_cpu")
    private Double temperaturaCpu;
    @JsonProperty("total_processos_ativos")
    private Double totalProcessosAtivos;

    //@JsonProperty("total_processos_ativos")
    //private Integer totalProcessosAtivos;
//
    //private Integer totalProcessos;

    public ColetaServidor() {
    }

    public ColetaServidor(String macAddress, String timestamp, Double cpu, Double cpuOciosaPorcentagem, Double cpuUsuariosPorcentagem, Double cpuSistemaPorcentagem, String cpuLoadAvg, Double cpuFrequencia, Double cpuFrequenciaMax, Double cpuFrequenciaMin, Double ramPorcentagem, Double ramMb, Double ramGb, Double ramDisponivel, Double ramDisponivelMb, Double ramDisponivelGb, Double ramSwapPorcentagem, Double ramSwapMb, Double ramSwapGb, Double disco, Double discoMb, Double discoGb, Double discoDisponivel, Double discoDisponivelMb, Double discoDisponivelGb, Double discoThroughputMbs, Double discoThroughputGbs, Double mbEnviados, Double mbRecebidos, Double temperaturaCpu, Double totalProcessosAtivos) {
        this.macAddress = macAddress;
        this.timestamp = timestamp;
        this.cpu = cpu;
        this.cpuOciosaPorcentagem = cpuOciosaPorcentagem;
        this.cpuUsuariosPorcentagem = cpuUsuariosPorcentagem;
        this.cpuSistemaPorcentagem = cpuSistemaPorcentagem;
        this.cpuLoadAvg = cpuLoadAvg;
        this.cpuFrequencia = cpuFrequencia;
        this.cpuFrequenciaMax = cpuFrequenciaMax;
        this.cpuFrequenciaMin = cpuFrequenciaMin;
        this.ramPorcentagem = ramPorcentagem;
        this.ramMb = ramMb;
        this.ramGb = ramGb;
        this.ramDisponivel = ramDisponivel;
        this.ramDisponivelMb = ramDisponivelMb;
        this.ramDisponivelGb = ramDisponivelGb;
        this.ramSwapPorcentagem = ramSwapPorcentagem;
        this.ramSwapMb = ramSwapMb;
        this.ramSwapGb = ramSwapGb;
        this.disco = disco;
        this.discoGb = discoGb;
        this.discoDisponivel = discoDisponivel;
        this.discoDisponivelMb = discoDisponivelMb;
        this.discoDisponivelGb = discoDisponivelGb;
        this.discoThroughputMbs = discoThroughputMbs;
        this.discoThroughputGbs = discoThroughputGbs;
        this.mbEnviados = mbEnviados;
        this.mbRecebidos = mbRecebidos;
        this.temperaturaCpu = temperaturaCpu;
        this.totalProcessosAtivos = totalProcessosAtivos;
        //this.totalProcessos = totalProcessos;
    }

    @Override
    public String toString() {
        return "ColetaServidor{" +
                "macAddress='" + macAddress + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", cpu=" + cpu +
                ", cpuOciosaPorcentagem=" + cpuOciosaPorcentagem +
                ", cpuUsuariosPorcentagem=" + cpuUsuariosPorcentagem +
                ", cpuSistemaPorcentagem=" + cpuSistemaPorcentagem +
                ", cpuLoadAvg='" + cpuLoadAvg + '\'' +
                ", cpuFrequencia=" + cpuFrequencia +
                ", cpuFrequenciaMax=" + cpuFrequenciaMax +
                ", cpuFrequenciaMin=" + cpuFrequenciaMin +
                ", ramPorcentagem=" + ramPorcentagem +
                ", ramMb=" + ramMb +
                ", ramGb=" + ramGb +
                ", ramDisponivel=" + ramDisponivel +
                ", ramDisponivelMb=" + ramDisponivelMb +
                ", ramDisponivelGb=" + ramDisponivelGb +
                ", ramSwapPorcentagem=" + ramSwapPorcentagem +
                ", ramSwapMb=" + ramSwapMb +
                ", ramSwapGb=" + ramSwapGb +
                ", disco=" + disco +
                ", discoGb=" + discoGb +
                ", discoDisponivel=" + discoDisponivel +
                ", discoDisponivelMb=" + discoDisponivelMb +
                ", discoDisponivelGb=" + discoDisponivelGb +
                ", discoThroughputMbs=" + discoThroughputMbs +
                ", discoThroughputGbs=" + discoThroughputGbs +
                ", mbEnviados=" + mbEnviados +
                ", mbRecebidos=" + mbRecebidos +
                ", temperaturaCpu=" + temperaturaCpu +
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

    public Double getCpu() {
        return cpu;
    }

    public void setCpu(Double cpu) {
        this.cpu = cpu;
    }

    public Double getCpuOciosaPorcentagem() {
        return cpuOciosaPorcentagem;
    }

    public void setCpuOciosaPorcentagem(Double cpuOciosaPorcentagem) {
        this.cpuOciosaPorcentagem = cpuOciosaPorcentagem;
    }

    public Double getCpuUsuariosPorcentagem() {
        return cpuUsuariosPorcentagem;
    }

    public void setCpuUsuariosPorcentagem(Double cpuUsuariosPorcentagem) {
        this.cpuUsuariosPorcentagem = cpuUsuariosPorcentagem;
    }

    public Double getCpuSistemaPorcentagem() {
        return cpuSistemaPorcentagem;
    }

    public void setCpuSistemaPorcentagem(Double cpuSistemaPorcentagem) {
        this.cpuSistemaPorcentagem = cpuSistemaPorcentagem;
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

    public Double getRamPorcentagem() {
        return ramPorcentagem;
    }

    public void setRamPorcentagem(Double ramPorcentagem) {
        this.ramPorcentagem = ramPorcentagem;
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

    public Double getRamSwapPorcentagem() {
        return ramSwapPorcentagem;
    }

    public void setRamSwapPorcentagem(Double ramSwapPorcentagem) {
        this.ramSwapPorcentagem = ramSwapPorcentagem;
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

    public Double getTemperaturaCpu() {
        return temperaturaCpu;
    }

    public void setTemperaturaCpu(Double temperaturaCpu) {
        this.temperaturaCpu = temperaturaCpu;
    }

    public Double getTotalProcessosAtivos() {
        return totalProcessosAtivos;
    }

    public void setTotalProcessosAtivos(Double totalProcessosAtivos) {
        this.totalProcessosAtivos = totalProcessosAtivos;
    }

    //public Integer getTotalProcessos() {
    //    return totalProcessos;
    //}
//
    //public void setTotalProcessos(Integer totalProcessos) {
    //    this.totalProcessos = totalProcessos;
    //}


}
