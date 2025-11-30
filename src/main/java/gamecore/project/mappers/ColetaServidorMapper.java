package gamecore.project.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Mapeador responsável por converter o conteúdo CSV de coleta de servidores
 * em uma estrutura JSON de array plano (Lista de objetos de coleta).
 *
 * REQUER AS DEPENDÊNCIAS: jackson-databind e jackson-dataformat-csv.
 */
public class ColetaServidorMapper {

    // Delimitador usado no CSV de coleta. Agora como char para uso com CsvSchema.
    private static final char DELIMITER = ';';

    // Inicializa o ObjectMapper uma vez para reutilização eficiente.
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converte um InputStream contendo dados CSV (com cabeçalho e delimitado por ';')
     * em uma String JSON no formato de array de objetos (coleta).
     *
     * A estrutura JSON será: [ {ponto_coleta_1}, {ponto_coleta_2}, ... ]
     *
     * @param csvInputStream InputStream contendo o conteúdo CSV.
     * @return String JSON com os dados em formato de array plano.
     * @throws IOException Em caso de erro na leitura do stream ou serialização JSON.
     */
    public String converterCsvParaJsonArray(InputStream csvInputStream) throws IOException {
        // CsvMapper é específico para dados CSV/Tabulares
        CsvMapper csvMapper = new CsvMapper();

        // Define o schema: usar cabeçalho e o delimitador ';'
        CsvSchema schema = CsvSchema.emptySchema()
                .withHeader()
                .withColumnSeparator(DELIMITER);

        // Usamos try-with-resources para garantir que o InputStream seja fechado corretamente
        try (InputStream is = csvInputStream) {
            if (is == null) {
                return "[]";
            }

            // 1. Lê o CSV diretamente do InputStream.
            // Cada linha (registro) será mapeada para um Map<String, Object>
            List<Object> allDataPoints = csvMapper
                    .readerFor(Map.class) // Mapeia cada registro para um Map
                    .with(schema)
                    .readValues(is)
                    .readAll();

            // 2. Serializa a lista plana para uma String JSON usando o ObjectMapper.
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(allDataPoints);

        } catch (JsonProcessingException e) {
            // Em caso de falha na serialização
            throw new IOException("Erro ao serializar dados para JSON usando Jackson.", e);
        } catch (IOException e) {
            // Em caso de falha na leitura do stream
            throw new IOException("Erro ao ler o InputStream CSV: " + e.getMessage(), e);
        }
    }
}