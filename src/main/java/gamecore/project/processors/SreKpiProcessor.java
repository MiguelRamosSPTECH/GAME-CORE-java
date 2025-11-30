package gamecore.project.processors;

// Importações necessárias
import gamecore.project.jira.JiraInteraction;
import gamecore.project.mappers.JiraMapper;
import gamecore.project.jira.dto.TicketJira; // Importa o DTO que acabamos de criar
import gamecore.project.kpis.KpiSre; // Sua classe de resultado final (assumindo que está no pacote 'kpis')

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SreKpiProcessor {

    private final JiraInteraction jiraInteraction = new JiraInteraction();
    private final JiraMapper jiraMapper = new JiraMapper();

    /**
     * Ponto de entrada do processador: orquestra a busca, cálculo e publicação.
     */
    public void runKpiProcessing() {
        // Processa os últimos 7 dias
        processarEPublicar(7, "7D");
        // Processa os últimos 30 dias
        processarEPublicar(30, "30D");
    }

    private void processarEPublicar(int dias, String periodoId) {

        // 1. Geração da JQL (Simplificada para usar -7d e -30d diretamente)
        String jql = String.format("project = \"KAN\" AND issuetype = \"Task\" AND resolution IS NOT EMPTY AND resolutiondate >= \"-%dd\"", dias);

        // 2. EXTRAÇÃO: Busca o JSON bruto
        String jsonBruto = jiraInteraction.buscarIncidentesResolvidos(jql);

        if (jsonBruto == null) {
            System.err.printf("Falha ao obter JSON para o período %s.%n", periodoId);
            return;
        }

        // 3. PARSEAMENTO: Converte JSON bruto em objetos TicketJira
        List<TicketJira> incidentes = jiraMapper.jsonJiraParaLista(jsonBruto);

        if (incidentes.isEmpty()) {
            System.out.printf("Nenhum incidente resolvido encontrado para %s. KPI será zero.%n", periodoId);
            publicarKpisNoS3(new KpiSre(periodoId, 0, (double) dias), periodoId);
            return;
        }

        // 4. CÁLCULO
        KpiSre kpiResultado = calcularMTBF_MTTR(incidentes, dias, periodoId);

        // 5. PUBLICAÇÃO (Simulação, a lógica real de S3 virá depois)
        publicarKpisNoS3(kpiResultado, periodoId);
    }


    // LÓGICA DE CÁLCULO DE MTBF MTTR


    private KpiSre calcularMTBF_MTTR(List<TicketJira> incidentes, int dias, String periodoId) {

        long totalDowntimeSegundos = 0;

        // 1. CALCULAR O DOWNTIME TOTAL (Soma de todos os MTTRs individuais)
        for (TicketJira ticket : incidentes) {
            try {
                // A data do Jira está no formato ISO 8601 (Ex: 2025-11-29T10:00:00.000+0000)
                Instant created = Instant.parse(ticket.getFields().getCreated());
                Instant resolved = Instant.parse(ticket.getFields().getResolutiondate());

                // Calcula a duração entre criação e resolução em segundos
                totalDowntimeSegundos += Duration.between(created, resolved).get(ChronoUnit.SECONDS);
            } catch (Exception e) {
                System.err.println("Erro ao converter data de um ticket. Ignorando este ticket.");
            }
        }

        // 2. CÁLCULO DO MTTR (Mean Time to Repair)
        long mttrSegundos = totalDowntimeSegundos / incidentes.size();
        long mttrMinutos = mttrSegundos / 60; // Apresentado em minutos

        // 3. CÁLCULO DO MTBF (Mean Time Between Failures)
        long totalPeriodoSegundos = (long) dias * 24 * 3600;
        long numFalhas = incidentes.size();

        // MTBF = (Tempo Total de Operação - Downtime Total) / Número de Falhas
        double mtbfSegundos = (double) (totalPeriodoSegundos - totalDowntimeSegundos) / numFalhas;

        // Converte para dias e arredonda para 1 casa decimal
        double mtbfDias = Math.round((mtbfSegundos / (24 * 3600)) * 10.0) / 10.0;

        return new KpiSre(periodoId, mttrMinutos, mtbfDias);
    }

    private void publicarKpisNoS3(KpiSre kpi, String periodoId) {
        // Este método será finalizado no próximo passo (integração com S3)
        // Por enquanto, apenas simula a saída
        System.out.printf("--- DADOS CALCULADOS (%s) ---%n", periodoId);
        System.out.printf("MTTR: %d minutos%n", kpi.getMttrMinutos());
        System.out.printf("MTBF: %.1f dias%n", kpi.getMtbfDias());
        // Aqui, o JSON final (KpiSre) seria serializado e enviado para o S3Uploader
    }
}