// gamecore.project.entity.DashboardData.java (NOVO ARQUIVO)

package gamecore.project.entity;

import java.util.List;

public class DashboardData {

    // alimenta o grafico de ram e a kpi de processos
    private String data_referencia;
    private int total_processos;
    private double ram_utilizada_percent;
    private double ram_disponivel_percent;

    // pega series temporais pro grafico de linha
    private List<TimeSeriesPoint> swapTimeSeries;
    private List<TimeSeriesPoint> desempenhoTimeSeries;
    private List<TimeSeriesPoint> esperaTimeSeries;
    private List<TimeSeriesPoint> cpuSystemTimeSeries;
    private List<TimeSeriesPoint> cpuUserTimeSeries;


    public DashboardData() {}

    //getters e setters

    public String getDataReferencia() {
        return data_referencia;
    }

    public void setDataReferencia(String data_referencia) {
        this.data_referencia = data_referencia;
    }

    public int getTotalProcessos() {
        return total_processos;
    }

    public void setTotalProcessos(int total_processos) { // Corrige Erro setTotalProcessos(int)
        this.total_processos = total_processos;
    }

    public double getRamUtilizadaPercent() {
        return ram_utilizada_percent;
    }

    public void setRamUtilizadaPercent(double ram_utilizada_percent) { // Corrige Erro setRamUtilizadaPercent
        this.ram_utilizada_percent = ram_utilizada_percent;
    }

    public double getRamDisponivelPercent() {
        return ram_disponivel_percent;
    }

    public void setRamDisponivelPercent(double ram_disponivel_percent) { // Corrige Erro setRamDisponivelPercent
        this.ram_disponivel_percent = ram_disponivel_percent;
    }

    // Séries Temporais
    public List<TimeSeriesPoint> getSwapTimeSeries() {
        return swapTimeSeries;
    }

    public void setSwapTimeSeries(List<TimeSeriesPoint> swapTimeSeries) { // Corrige Erro setSwapTimeSeries(...)
        this.swapTimeSeries = swapTimeSeries;
    }

    public List<TimeSeriesPoint> getDesempenhoTimeSeries() {
        return desempenhoTimeSeries;
    }

    public void setDesempenhoTimeSeries(List<TimeSeriesPoint> desempenhoTimeSeries) { // Corrige Erro setDesempenhoTimeSeries(...)
        this.desempenhoTimeSeries = desempenhoTimeSeries;
    }

    public List<TimeSeriesPoint> getEsperaTimeSeries() {
        return esperaTimeSeries;
    }

    public void setEsperaTimeSeries(List<TimeSeriesPoint> esperaTimeSeries) { // Corrige Erro setEsperaTimeSeries(...)
        this.esperaTimeSeries = esperaTimeSeries;
    }

    public List<TimeSeriesPoint> getCpuSystemTimeSeries() {
        return cpuSystemTimeSeries;
    }

    public void setCpuSystemTimeSeries(List<TimeSeriesPoint> cpuSystemTimeSeries) { // Corrige Erro setCpuSystemTimeSeries(...)
        this.cpuSystemTimeSeries = cpuSystemTimeSeries;
    }

    public List<TimeSeriesPoint> getCpuUserTimeSeries() {
        return cpuUserTimeSeries;
    }

    public void setCpuUserTimeSeries(List<TimeSeriesPoint> cpuUserTimeSeries) { // Corrige Erro setCpuUserTimeSeries(...)
        this.cpuUserTimeSeries = cpuUserTimeSeries;
    }
}