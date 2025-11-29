package gamecore.project.mappers;

import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import gamecore.project.entity.ComponenteAlerta;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    public Map<String, Object> createJsonAlertGeral(Map<String, Integer> contagemAlertaLeve, Map<String, Integer> contagemAlertaGrave, LocalDateTime ultimaDataAlerta, Integer qtdLinhasLidas) {
        String dataEmString = ultimaDataAlerta.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        AtomicInteger contaLeve = new AtomicInteger(0);
        AtomicInteger contaForte = new AtomicInteger(0);
        List<ComponenteAlerta> contagemFinalAlerta = new ArrayList<>();

        Map<String, Object> jsonOutput = new LinkedHashMap<>();
        Map<String, Object> jsonFinal = new LinkedHashMap<>();

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
            contagemFinalAlerta.add(new ComponenteAlerta(chave, tipo));

        });
        String gravidadeAlerta = contaForte.get() >= contaLeve.get() ? "Grave" : "Médio";


        jsonOutput.put("DATA_MAIS_RECENTE_ALERTA", dataEmString);
        jsonOutput.put("GRAVIDADE_ALERTA", gravidadeAlerta);
        jsonOutput.put("COMPONENTES",contagemFinalAlerta);

        List<Map<String, Object>> listaWrapper = new ArrayList<>();
        listaWrapper.add(jsonOutput); // Adiciona o objeto completo à lista wrapper

        // Coloca a lista wrapper no mapa final
        jsonFinal.put("LISTA_ALERTAS", listaWrapper);
        try {
            String jsonResultado = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(jsonFinal);
            System.out.println(jsonResultado);
        }catch (Exception e ) {
            System.out.println("Erro ao serializar JSON: " + e.getMessage());
        }
        return jsonFinal;
    }
}
