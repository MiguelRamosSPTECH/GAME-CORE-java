package school.sptech;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultadoMedia {
    // Adicione anotações do Jackson se for serializar como JSON
    @JsonProperty("media_cpu")
    private final double mediaCpu;

    @JsonProperty("media_ram")
    private final double mediaRam;

    public ResultadoMedia(double mediaCpu, double mediaRam) {
        this.mediaCpu = mediaCpu;
        this.mediaRam = mediaRam;
    }

    // Você precisa de Getters para o Jackson serializar
    public double getMediaCpu() {
        return mediaCpu;
    }

    public double getMediaRam() {
        return mediaRam;
    }
}