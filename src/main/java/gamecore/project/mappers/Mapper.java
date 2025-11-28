package gamecore.project.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gamecore.project.entity.ComponenteAlerta;

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
    //CLIENT
    public void csvToJson(InputStream arq) {

    }

    public void createJsonAlertGeral(Map<String, Integer> contagemAlertaLeve, Map<String, Integer> contagemAlertaGrave, LocalDateTime ultimaDataAlerta, Integer qtdLinhasLidas) {
        String dataEmString = ultimaDataAlerta.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        AtomicInteger contaLeve = new AtomicInteger(0);
        AtomicInteger contaForte = new AtomicInteger(0);
        List<ComponenteAlerta> contagemFinalAlerta = new ArrayList<>();

        Map<String, Object> jsonOutput = new LinkedHashMap<>();

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

        System.out.println(contagemFinalAlerta.toString());
        System.out.println(ultimaDataAlerta);
        System.out.println(gravidadeAlerta);

        jsonOutput.put("DATA_MAIS_RECENTE_ALERTA", dataEmString);
        jsonOutput.put("GRAVIDADE_ALERTA", gravidadeAlerta);
        jsonOutput.put("COMPONENTES",contagemFinalAlerta);

        try {
            String jsonResultado = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(jsonOutput);
            System.out.println("JSON GERADO: " + jsonResultado);
        }catch (Exception e ) {
            System.out.println("Erro ao serializar JSON: " + e.getMessage());
        }


    }
}
