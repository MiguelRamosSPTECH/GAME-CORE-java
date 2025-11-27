//usei essa classe pra mapear e serializar as metricas pra gerar o resultado final do DashboardData (que é onde eu pego os bglh)
// o unico metodo que tinha aqui era o de transformar pra json (manteve)
package gamecore.project.mappers;


import java.io.InputStream;

import gamecore.project.entity.ColetaServidor;
import gamecore.project.entity.DashboardData;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper; // biblioteca JAACKSON (pega um objeto e transforma automaticamente em string json safadinha
import java.io.IOException;

public class Mapper {
    //CLIENT
    public void csvToJson(InputStream arq) {

    }


    // pega as linhas e converta em objeto java (senao nao consigo faze os calculo - passa o csv "cru" pra entidade coletaServidor
    public List<ColetaServidor> mapToColetasServidor(List<String[]> linhas) {
        List<ColetaServidor> coletas = new ArrayList<>();

        // ATENÇÃO: A lógica real de mapeamento de colunas do CSV para os
        // atributos da ColetaServidor deve ser implementada aqui.
        // Por exemplo: coleta.setTimestamp(campos[1]);

        for (String[] campos : linhas) {
            if (campos.length > 1) { // Garante que a linha não está vazia
                ColetaServidor coleta = new ColetaServidor();
                // Sua lógica de mapeamento aqui...

                coletas.add(coleta);
            }
        }
        return coletas;
    }

    // metodo que pega o objeto final (que vem do DashboardData) q ta com as series e resumos e bota td em uma string json
    // o s3 viewer nao le objeto java (quer json -> ETAPA DE PREPARAÇÃO DOS DADOS)
    public String toJson(DashboardData data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(data);
    }
}
