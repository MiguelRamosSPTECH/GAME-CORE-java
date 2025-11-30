package gamecore.project.jira.dto; // AJUSTE: Mantenha o seu pacote DTO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Mapeia os campos 'criado' (Início da Falha) e 'dataResolucao' (Fim da Reparação)
 * que estão aninhados dentro do objeto "fields" do JSON do Jira.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampoTicketJira {

    // Data e hora de criação (Início da Falha)
    private String created; // Mantemos o nome 'created' pois é o nome exato no JSON do Jira

    // Data e hora de resolução (Fim da Reparação)
    private String resolutiondate; // Mantemos o nome 'resolutiondate' pois é o nome exato no JSON do Jira

    public CampoTicketJira() {}

    // Getters e Setters
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getResolutiondate() {
        return resolutiondate;
    }

    public void setResolutiondate(String resolutiondate) {
        this.resolutiondate = resolutiondate;
    }
}