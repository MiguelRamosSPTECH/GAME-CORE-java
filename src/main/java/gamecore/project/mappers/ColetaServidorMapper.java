package gamecore.project.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.databind.MappingIterator;
import gamecore.project.entity.ColetaServidor;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Mapeador responsável por converter o conteúdo CSV de coleta de servidores
 * em uma estrutura JSON de array plano (Lista de objetos de coleta).
 */
public class ColetaServidorMapper {

    private static final char DELIMITER = ';';
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converte um InputStream contendo dados CSV (com cabeçalho e delimitado por ';')
     * em uma String JSON no formato de array de objetos (coleta).
     *
     * @param csvInputStream InputStream contendo o conteúdo CSV.
     * @return String JSON com os dados em formato de array plano.
     * @throws IOException Em caso de erro na leitura do stream ou serialização JSON.
     */
    public String converterCsvParaJsonArray(InputStream csvInputStream) throws IOException {

        CsvMapper csvMapper = new CsvMapper();

        // Define o schema: usar cabeçalho e o delimitador ';'
        CsvSchema schema = CsvSchema.emptySchema()
                .withHeader()
                .withColumnSeparator(DELIMITER)
                .withoutQuoteChar(); // recomendado por não usar aspas no CSV

        try (InputStream is = csvInputStream) {
            if (is == null) {
                return "[]";
            }

            // le o CSV diretamente do InputStream.
            // mapeia para ColetaServidor
            // A lista agora é tipada corretamente.
            MappingIterator<ColetaServidor> servidorIterator = csvMapper
                    .readerFor(ColetaServidor.class)
                    .with(schema)
                    .readValues(is);

            //chama readAll() no iterador tipado. Isso garante que o retorno seja List<ColetaServidor>
            List<ColetaServidor> allDataPoints = servidorIterator.readAll();

            // serializa a lista plana para JSON
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