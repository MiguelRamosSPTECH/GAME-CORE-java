package gamecore.project.mappers;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import gamecore.project.entity.ColetaServidor;
import gamecore.project.entity.ComponenteAlerta;
import gamecore.project.entity.ConfiguracaoServidor;
import gamecore.project.entity.TimeSeriesPoint;
import gamecore.project.jira.JiraInteraction;

import java.io.IOException;
import java.time.LocalDateTime;


import java.io.InputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Mapper {
    private ObjectMapper objectMapper = null;
    public Mapper() {
        objectMapper = new ObjectMapper();

    }

    // ===== MÉTODO 1: Mapear CSV → List<ColetaServidor> =====
    public List<ColetaServidor> mapToColetasServidor(List<String[]> linhas) {
        List<ColetaServidor> coletas = new ArrayList<>();

        for (String[] linha : linhas) {
            if (linha.length < 30) continue;

            ColetaServidor c = new ColetaServidor();
            try {
                c.setMacAddress(linha[0]);
                c.setTimestamp(linha[1]);
                c.setCpu(parseDouble(linha[2]));
                c.setCpuOciosaPorcentagem(parseDouble(linha[3]));
                c.setCpuUsuariosPorcentagem(parseDouble(linha[4]));
                c.setCpuSistemaPorcentagem(parseDouble(linha[5]));
                c.setCpuLoadAvg(linha[6]);
                c.setRamPorcentagem(parseDouble(linha[10]));
                c.setRamDisponivel(parseDouble(linha[13]));
                c.setRamSwapPorcentagem(parseDouble(linha[16]));
                c.setTotalProcessosAtivos(parseInt(linha[29])); // Índice 29 para total_processos_ativos

                coletas.add(c);
            } catch (Exception e) {
                System.err.println("Erro ao mapear linha: " + e.getMessage());
            }
        }

        return coletas;
    }

    // ===== MÉTODO 2: Gerar JSON do Dashboard =====
    public String gerarJsonDashboard(List<ColetaServidor> coletas) throws Exception {
        Map<String, Object> dashboard = new LinkedHashMap<>();

        if (coletas.isEmpty()) {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dashboard);
        }

        ColetaServidor ultimaColeta = coletas.get(coletas.size() - 1);

        // KPIs
        dashboard.put("dataReferencia", ultimaColeta.getTimestamp().substring(0, 10));
        dashboard.put("totalProcessos", calcularMaxProcessos(coletas));
        dashboard.put("totalAlertasMes", 0); // TODO: Buscar do Jira
        dashboard.put("ramUtilizadaPercent", ultimaColeta.getRamPorcentagem());
        dashboard.put("ramDisponivelPercent", ultimaColeta.getRamDisponivel());

        // Séries temporais diárias
        dashboard.put("swapTimeSeries", gerarSerieSwap(coletas));
        dashboard.put("desempenhoTimeSeries", gerarSerieDesempenho(coletas));
        dashboard.put("esperaTimeSeries", gerarSerieEspera(coletas));
        dashboard.put("cpuSystemTimeSeries", gerarSerieCpuSistema(coletas));
        dashboard.put("cpuUserTimeSeries", gerarSerieCpuUsuario(coletas));

        // Séries mensais (mockadas temporariamente)
        dashboard.put("alertasMesTimeSeries", gerarAlertasMockados());
        dashboard.put("picoMaxCpuMesTimeSeries", gerarPicosCpuMockados());
        dashboard.put("picoMaxRamMesTimeSeries", gerarPicosRamMockados());
        dashboard.put("picoMaxProcessosMesTimeSeries", gerarPicosProcessosMockados());

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dashboard);
    }

// ===== MÉTODOS AUXILIARES =====

    private List<TimeSeriesPoint> gerarSerieSwap(List<ColetaServidor> coletas) {
        List<TimeSeriesPoint> serie = new ArrayList<>();
        for (ColetaServidor c : coletas) {
            serie.add(new TimeSeriesPoint(
                    c.getTimestamp().replace(" ", "T") + "Z",
                    c.getRamSwapPorcentagem()
            ));
        }
        return serie;
    }

    private List<TimeSeriesPoint> gerarSerieDesempenho(List<ColetaServidor> coletas) {
        List<TimeSeriesPoint> serie = new ArrayList<>();
        for (ColetaServidor c : coletas) {
            double cpuUso = c.getCpu();
            double cpuOciosa = c.getCpuOciosaPorcentagem();
            double swap = c.getRamSwapPorcentagem();

            double denominador = cpuOciosa + swap;
            double desempenho = denominador <= 0.001 ? 1000.0 : cpuUso / denominador;

            serie.add(new TimeSeriesPoint(
                    c.getTimestamp().replace(" ", "T") + "Z",
                    Math.round(desempenho * 100.0) / 100.0
            ));
        }
        return serie;
    }

    private List<TimeSeriesPoint> gerarSerieEspera(List<ColetaServidor> coletas) {
        List<TimeSeriesPoint> serie = new ArrayList<>();
        for (ColetaServidor c : coletas) {
            serie.add(new TimeSeriesPoint(
                    c.getTimestamp().replace(" ", "T") + "Z",
                    c.getCpuOciosaPorcentagem() + c.getRamSwapPorcentagem()
            ));
        }
        return serie;
    }

    private List<TimeSeriesPoint> gerarSerieCpuSistema(List<ColetaServidor> coletas) {
        List<TimeSeriesPoint> serie = new ArrayList<>();
        for (ColetaServidor c : coletas) {
            serie.add(new TimeSeriesPoint(
                    c.getTimestamp().replace(" ", "T") + "Z",
                    c.getCpuSistemaPorcentagem()
            ));
        }
        return serie;
    }

    private List<TimeSeriesPoint> gerarSerieCpuUsuario(List<ColetaServidor> coletas) {
        List<TimeSeriesPoint> serie = new ArrayList<>();
        for (ColetaServidor c : coletas) {
            serie.add(new TimeSeriesPoint(
                    c.getTimestamp().replace(" ", "T") + "Z",
                    c.getCpuUsuariosPorcentagem()
            ));
        }
        return serie;
    }

    private int calcularMaxProcessos(List<ColetaServidor> coletas) {
        int max = 0;
        for (ColetaServidor c : coletas) {
            Integer totalProc = c.getTotalProcessosAtivos();
            if (totalProc != null && totalProc > max) {
                max = totalProc;
            }
        }
        return max;
    }

    private List<TimeSeriesPoint> gerarAlertasMockados() {
        List<TimeSeriesPoint> serie = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            serie.add(new TimeSeriesPoint(
                    String.format("2025-11-%02dT00:00:00Z", i),
                    Math.random() * 5
            ));
        }
        return serie;
    }

    private List<TimeSeriesPoint> gerarPicosCpuMockados() {
        List<TimeSeriesPoint> serie = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            serie.add(new TimeSeriesPoint(
                    String.format("2025-11-%02dT00:00:00Z", i),
                    70 + Math.random() * 30
            ));
        }
        return serie;
    }

    private List<TimeSeriesPoint> gerarPicosRamMockados() {
        List<TimeSeriesPoint> serie = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            serie.add(new TimeSeriesPoint(
                    String.format("2025-11-%02dT00:00:00Z", i),
                    60 + Math.random() * 35
            ));
        }
        return serie;
    }

    private List<TimeSeriesPoint> gerarPicosProcessosMockados() {
        List<TimeSeriesPoint> serie = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            serie.add(new TimeSeriesPoint(
                    String.format("2025-11-%02dT00:00:00Z", i),
                    150 + Math.random() * 100
            ));
        }
        return serie;
    }

    private Double parseDouble(String valor) {
        try {
            return Double.parseDouble(valor.replace(",", "."));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Integer parseInt(String valor) {
        try {
            return Integer.parseInt(valor);
        } catch (Exception e) {
            return 0;
        }
    }


    public String csvToJson(InputStream arq, Context context) {
        try {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();

            List<Object> data = csvMapper
                    .readerFor(Map.class)
                    .with(schema)
                    .readValues(arq)
                    .readAll();

            arq.close();
            context.getLogger().log("Conversão para json concluída");
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (IOException e) {
            context.getLogger().log("Erro durante a conversão ou manipulação de arquivo: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    //reaproveitando para criar alerta do jira.
    public void createJsonAlertGeral(
            Map<String, Integer> contagemAlertaLeve,
            Map<String, Integer> contagemAlertaGrave,
            LocalDateTime ultimaDataAlerta,
            Integer qtdLinhasLidas,
            String nomeServidor,
            List<ConfiguracaoServidor> configsLayout,
            Map<String, Double> mediaUltimoDado
            ) {
        System.out.println("MEDIA ULTIMO DADO: "+ mediaUltimoDado.toString());

        String dataEmString = ultimaDataAlerta.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        AtomicInteger contaLeve = new AtomicInteger(0);
        AtomicInteger contaForte = new AtomicInteger(0);
        List<ComponenteAlerta> contagemFinalAlerta = new ArrayList<>();

//        Map<String, Object> jsonOutput = new LinkedHashMap<>();
//        Map<String, Object> jsonFinal = new LinkedHashMap<>();

        contagemAlertaGrave.forEach((chave, valor) -> {
            String tipo = "";
            System.out.println("chave: "+chave+" valor: "+valor);
            if(contagemAlertaLeve.get(chave) <= valor) {
                contaForte.incrementAndGet();

                if(contagemAlertaGrave.get(chave) > (qtdLinhasLidas * 0.8)) {
                    tipo = "CONSISTENTE";
                } else {
                    tipo = "OSCILAÇÃO";
                }
            } else {
                if(contagemAlertaLeve.get(chave) > (qtdLinhasLidas * 0.8)) {
                    tipo = "CONSISTENTE";
                } else {
                    tipo = "OSCILAÇÃO";
                }
                contaLeve.incrementAndGet();
            }
            if(valor >= 15) {
                contagemFinalAlerta.add(new ComponenteAlerta(chave, tipo));
            }

        });

        String gravidadeAlerta = contaForte.get() >= contaLeve.get() ? "Grave" : "Médio";
        String horasResolver = "";
        if(gravidadeAlerta.equals("Grave")) {
            horasResolver = "1 horas";
        } else {
            horasResolver = "2 horas";
        }


        String tabelaComponentes = String.format("%-15s | %-12s | %-10s | %-12s | %-12s\n-------------------------------------------------------------------------\n", "COMPONENTE","TIPO","FAIXA LEVE","FAIXA GRAVE", "ÚLTIMO DADO");
        for(ComponenteAlerta ca : contagemFinalAlerta) {
            List<String> faixas = new ArrayList<>();
            AtomicReference<Double> ultimoValorOrMedia = new AtomicReference<>(0.0);
            for(int i=0;i<configsLayout.size();i++) {
                if(ca.getNome().substring(0, ca.getNome().lastIndexOf("_")).equalsIgnoreCase(configsLayout.get(i).getNomeComponente())) {
                    faixas.add(configsLayout.get(i).getAlertaLeve());
                    faixas.add(configsLayout.get(i).getAlertaGrave());
                }
            }
            mediaUltimoDado.forEach((chave, valor) -> {
                if(chave.equalsIgnoreCase(ca.getNome())) {
                    if(ca.getNome().contains("CPU") || ca.getNome().contains("RAM")) {
                        Integer numOcorrencia = 0;
                        if(gravidadeAlerta.equals("Grave")) {
                            numOcorrencia = contagemAlertaGrave.get(ca.getNome());
                        } else {
                            numOcorrencia = contagemAlertaLeve.get(ca.getNome());
                        }
//                        Integer  conta = (int) contagemFinalAlerta.get(ca.getNome());
                        ultimoValorOrMedia.set(valor / numOcorrencia);
                    } else {
                        ultimoValorOrMedia.set(valor);
                    }
                }
            });
            tabelaComponentes+=String.format("%-15s | %-12s | %-10s | %-12s | %-12.2f\n", ca.getNome(), ca.getTipo(), faixas.get(0), faixas.get(1), ultimoValorOrMedia.get());
        }

        String tituloTicket = String.format("(%d) Componente(s) sob estresse | %s", contagemFinalAlerta.size(), nomeServidor);
        String startDateTicket = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        //AREA PARA MONTAR A DESCRIÇÃO DO ALERTA JIRA
        String descricaoTicket = String.format("""
                 1. STATUS E IMPACTO IMEDIATO\n
                 - Servidor: %s\n
                 - Ambiente: Produção\n
                 - Início do Alerta: %s\n
                 - Prazo IDEAL para resolução: %s\n

                 2. DETALHES DA ANOMALIA E EVIDÊNCIA\n
                 Os seguintes componentes ultrapassaram a faixa de alerta designada, apresentando comportamento irregular nos últimos 5 minutos:\n
                %s
                 Definição dos Tipos de Anomalia:
                 - Oscilação: Comportamento desregulado com diversos picos acima da faixa de alerta (Ex: Vários saltos).
                 - Consistência: Manteve-se constantemente acima do limite mínimo de alerta por um período sustentado.\n\n

                 3. AÇÕES SUGERIDAS\n
                 Para o(s) técnico(s):\n
                    - Trabalhe junto com o SRE para identificar no relatório a principal causa do alerta, se possível, sempre pedindo relatórios a cada 5 minutos para caso haja algum alerta, estar com os dados em mãos para identificar brevemente a causa raíz

                 Para o(s) Engenheiro(s) De SRE:\n
                    1. Dashboard Técnico: Verifique o comportamento dos containers e identifique o container que teve o consumo mais elevado (referência: Dashboard Técnica/Monitoramento de Containers).

                    2. Relatórios (DASH): Verificar a dash de relatório para visualizar o(s) comportamento(s) dos componente(s) sob estresse, afim de correlacionar dados.

                    3. Logs: Procure por eventos ou exceções nos logs do serviço a partir do Start Date para correlacionar o pico de consumo com a atividade do código.

                """, nomeServidor, String.format("%s (%s)", dataEmString.replace("T", " ").split(" ")[0], "HOJE"), horasResolver, tabelaComponentes );

        //ENVIANDO ALERTA PARA SER PLOTADO NO JIRA
        JiraInteraction.criarTicket(tituloTicket, descricaoTicket, gravidadeAlerta, startDateTicket);


//
//        jsonOutput.put("DATA_MAIS_RECENTE_ALERTA", dataEmString);
//        jsonOutput.put("GRAVIDADE_ALERTA", gravidadeAlerta);
//        jsonOutput.put("COMPONENTES",contagemFinalAlerta);
//
//        List<Map<String, Object>> listaWrapper = new ArrayList<>();
//        listaWrapper.add(jsonOutput); // Adiciona o objeto completo à lista wrapper
//
//        // Coloca a lista wrapper no mapa final
//        jsonFinal.put("LISTA_ALERTAS", listaWrapper);
//        try {
//            String jsonResultado = objectMapper.writerWithDefaultPrettyPrinter()
//                    .writeValueAsString(jsonFinal);
//            System.out.println(jsonResultado);
//        }catch (Exception e ) {
//            System.out.println("Erro ao serializar JSON: " + e.getMessage());
//        }
//        return jsonFinal;
    }
}
