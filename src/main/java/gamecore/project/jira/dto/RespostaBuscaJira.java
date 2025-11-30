package gamecore.project.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Mapeia o objeto de resposta principal retornado pela API /search do Jira.
 * Contém a lista de tickets (issues).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaBuscaJira {

    // O campo "issues" do JSON que contém a lista de tickets
    private List<TicketJira> issues;

    public RespostaBuscaJira() {}

    // Getters e Setters
    public List<TicketJira> getIssues() {
        return issues;
    }

    public void setIssues(List<TicketJira> issues) {
        this.issues = issues;
    }
}