package gamecore.project.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import gamecore.project.entity.ColetaContainer; // Importação da nova entidade

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapeador responsável por converter o conteúdo CSV de coleta de containers
 * em uma estrutura JSON agrupada por timestamp.
 */
public class ColetaContainerMapper {

    private static final char DELIMITER = ';';
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CsvMapper csvMapper = new CsvMapper();

    /**
     * Converte um InputStream contendo dados CSV de containers para a estrutura JSON aninhada.
     * A estrutura agrupa todos os containers coletados no mesmo momento sob o timestamp.
     *
     * @param csvInputStream InputStream contendo o conteúdo CSV.
     * @return String JSON com os dados agrupados por timestamp.
     * @throws IOException Em caso de erro na leitura do stream ou serialização JSON.
     */
    public String converterCsvParaJsonAninhado(InputStream csvInputStream) throws IOException {

        // 1. Define o schema: usar cabeçalho e o delimitador ';'
        CsvSchema schema = CsvSchema.emptySchema()
                .withHeader()
                .withColumnSeparator(DELIMITER)
                .withoutQuoteChar();

        List<ColetaContainer> containers;

        // 2. Lê o CSV diretamente do InputStream, Mapeando cada linha para um objeto ColetaContainer
        try (InputStream is = csvInputStream) {
            if (is == null) {
                return "[]";
            }

            // SOLUÇÃO COM ARRAY DE TIPOS para garantir a tipagem correta
            // Lê para um Array de ColetaContainer
            ColetaContainer[] containersArray = csvMapper
                    .readerFor(ColetaContainer[].class)
                    .with(schema)
                    .readValues(is)
                    .readAll()
                    .toArray(new ColetaContainer[0]);

            // Converte o Array de volta para uma List<ColetaContainer>
            containers = Arrays.asList(containersArray);

        } catch (IOException e) {
            throw new IOException("Erro ao ler o InputStream CSV e mapear para ColetaContainer: " + e.getMessage(), e);
        }

        // 3. Agrupa os objetos ColetaContainer pelo timestamp
        Map<String, List<ColetaContainer>> groupedByTimestamp = containers.stream()
                .collect(Collectors.groupingBy(ColetaContainer::getTimestamp));

        // 4. Constrói a estrutura de lista final: List<Map<String, Object>>
        // Formato: [ {"timestamp1": [container1, container2, ...]}, {"timestamp2": [container3, container4, ...]} ]
        List<Map<String, Object>> finalJsonStructure = new ArrayList<>();

        // 5. Itera sobre os grupos de timestamps para construir a estrutura aninhada
        for (Map.Entry<String, List<ColetaContainer>> entry : groupedByTimestamp.entrySet()) {
            String timestamp = entry.getKey();
            List<ColetaContainer> containersNoTempo = entry.getValue();

            // Lista para conter os objetos de container (em formato Map)
            List<Map<String, Object>> containersListForTimestamp = new ArrayList<>();

            for (ColetaContainer container : containersNoTempo) {
                // Adiciona o Map de detalhes do container à lista do timestamp
                containersListForTimestamp.add(container.toMap());
            }

            // Cria o objeto de nível superior: {"timestamp": [lista de containers em Map]}
            Map<String, Object> timestampEntry = new HashMap<>();
            timestampEntry.put(timestamp, containersListForTimestamp);

            finalJsonStructure.add(timestampEntry);
        }

        // Opcional: Garante que os timestamps apareçam em alguma ordem previsível
        finalJsonStructure.sort(Comparator.comparing(m -> m.keySet().iterator().next()));

        // 6. Serializa a estrutura de lista final para JSON
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(finalJsonStructure);
        } catch (JsonProcessingException e) {
            throw new IOException("Erro ao serializar a estrutura de dados aninhada para JSON.", e);
        }
    }
}