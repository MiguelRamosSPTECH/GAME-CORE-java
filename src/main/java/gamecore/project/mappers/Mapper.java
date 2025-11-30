package gamecore.project.mappers;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import gamecore.project.entity.ComponenteAlerta;
import gamecore.project.entity.ConfiguracaoServidor;
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
