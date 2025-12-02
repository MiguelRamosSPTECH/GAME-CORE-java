package school.sptech;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Stock {

    @JsonProperty("cpu_porcentagem")
    private double cpu_porcentagem;

    @JsonProperty("ram_porcentagem")
    private double ram_porcentagem;

    @JsonProperty("timestamp")
    private Timestamp timestamp;


    // Getters e setters


    public double getCpu_porcentagem() {
        return cpu_porcentagem;
    }

    public void setCpu_porcentagem(double cpu_porcentagem) {
        this.cpu_porcentagem = cpu_porcentagem;
    }

    public double getRam_porcentagem() {
        return ram_porcentagem;
    }

    public void setRam_porcentagem(double ram_porcentagem) {
        this.ram_porcentagem = ram_porcentagem;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

