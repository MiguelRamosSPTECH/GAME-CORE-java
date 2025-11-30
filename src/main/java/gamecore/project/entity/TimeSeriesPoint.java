// gamecore.project.entity.TimeSeriesPoint.java (NOVO ARQUIVO)

package gamecore.project.entity;

// Usaremos 'value' para armazenar % de Swap, % de CPU Sistema, Índice de Desempenho, etc.
public class TimeSeriesPoint {

    private String timestamp;
    private double value;

    public TimeSeriesPoint() {
        // Construtor padrão (pode ser necessário para algumas bibliotecas de serialização)
    }

    // Construtor usado no DashboardProcessor
    public TimeSeriesPoint(String timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    // Getters e Setters (essenciais para o compilador e para o Mapper/JSON)
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}