package gamecore.project.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Mapeia um único ticket do Jira.
 * Contém o objeto aninhado "fields" (Campos).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketJira {

    // O campo "fields" do JSON, mapeado para a nossa classe de campos
    private CampoTicketJira fields;

    public TicketJira() {}

    // Getters e Setters
    public CampoTicketJira getFields() {
        return fields;
    }

    public void setFields(CampoTicketJira fields) {
        this.fields = fields;
    }
}