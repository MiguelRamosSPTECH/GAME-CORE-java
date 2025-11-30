package gamecore.project.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gamecore.project.jira.dto.RespostaBuscaJira;
import gamecore.project.jira.dto.TicketJira;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JiraMapper {

    // O Jackson, a ferramenta que faz a tradução
    private final ObjectMapper objectMapper;

    public JiraMapper() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Faz o parse (desserialização) do JSON bruto da API do Jira
     * para uma lista de objetos TicketJira.
     * @param jsonBruto A string JSON completa retornada pelo JiraInteraction.
     * @return Uma lista de objetos TicketJira, ou uma lista vazia em caso de falha.
     */
    public List<TicketJira> jsonJiraParaLista(String jsonBruto) {

        if (jsonBruto == null || jsonBruto.isEmpty()) {
            System.err.println("JSON bruto recebido está vazio.");
            return Collections.emptyList();
        }

        try {
            // Mapeia a string JSON para a classe raiz (RespostaBuscaJira)
            RespostaBuscaJira response = objectMapper.readValue(jsonBruto, RespostaBuscaJira.class);

            // Retorna a lista de issues (tickets)
            return response.getIssues();

        } catch (IOException e) {
            System.err.println("ERRO fatal ao parsear JSON do Jira. Verifique as classes DTOs.");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}