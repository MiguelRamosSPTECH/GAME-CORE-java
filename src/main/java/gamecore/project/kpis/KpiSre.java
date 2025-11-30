package gamecore.project.kpis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;

/**
 * DTO que armazena os KPIs calculados (MTBF e MTTR) para serem serializados para JSON e enviados ao S3.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KpiSre {

    private String periodo;
    private long mttrMinutos;
    private double mtbfDias;
    private String timestamp;

    // Construtor obrigatório para criar o objeto após o cálculo
    public KpiSre(String periodo, long mttrMinutos, double mtbfDias) {
        this.periodo = periodo;
        this.mttrMinutos = mttrMinutos;
        this.mtbfDias = mtbfDias;
        // Armazena a data/hora exata do cálculo
        this.timestamp = Instant.now().toString();
    }

    // Construtor padrão (necessário para Jackson caso haja desserialização)
    public KpiSre() {}

    // Getters para o Jackson serializar (gerar o JSON)
    public String getPeriodo() { return periodo; }
    public long getMttrMinutos() { return mttrMinutos; }
    public double getMtbfDias() { return mtbfDias; }
    public String getTimestamp() { return timestamp; }

    // Setters (se necessário)
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public void setMttrMinutos(long mttrMinutos) { this.mttrMinutos = mttrMinutos; }
    public void setMtbfDias(double mtbfDias) { this.mtbfDias = mtbfDias; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}