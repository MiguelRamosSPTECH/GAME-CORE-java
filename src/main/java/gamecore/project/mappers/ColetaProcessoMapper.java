package gamecore.project.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import gamecore.project.entity.ColetaProcesso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapeador responsável por converter o conteúdo CSV de coleta de processos
 * em uma estrutura JSON aninhada.
 */
public class ColetaProcessoMapper {

    private static final char DELIMITER = ';';
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CsvMapper csvMapper = new CsvMapper();

    /**
     * Converte um InputStream contendo dados CSV de processos para a estrutura JSON aninhada.
     *
     * @param csvInputStream InputStream contendo o conteúdo CSV.
     * @return String JSON com os dados agrupados por timestamp e aninhados por nome do processo.
     * @throws IOException Em caso de erro na leitura do stream ou serialização JSON.
     */
    public String converterCsvParaJsonAninhado(InputStream csvInputStream) throws IOException {

        // 1. Define o schema: usar cabeçalho e o delimitador ';'
        CsvSchema schema = CsvSchema.emptySchema()
                .withHeader()
                .withColumnSeparator(DELIMITER)
                .withoutQuoteChar();

        List<ColetaProcesso> processos;

        // 2. Lê o CSV diretamente do InputStream, Mapeando cada linha para um objeto ColetaProcesso
        try (InputStream is = csvInputStream) {
            if (is == null) {
                return "[]";
            }

            processos = new ArrayList<>();

            // CORREÇÃO DEFINITIVA PARA ERRO DE RUNTIME (START_OBJECT) e AMBIGUIDADE (ADD)
            // Lemos linha por linha (MappingIterator), e usamos um lambda com cast explícito
            // para garantir a tipagem na List e remover a ambiguidade do compilador.
            csvMapper
                    .readerFor(ColetaProcesso.class) // LÊ CADA LINHA COMO UMA INSTÂNCIA DE ColetaProcesso
                    .with(schema)
                    .readValues(is)
                    // Usa um lambda com cast explícito para ColetaProcesso
                    .forEachRemaining(obj -> processos.add((ColetaProcesso) obj));

        } catch (IOException e) {
            // Se o erro JsonToken.START_OBJECT persistir, verifique a formatação do CSV
            // (cabeçalho, delimitador) no InputStream.
            throw new IOException("Erro ao ler o InputStream CSV e mapear para ColetaProcesso: " + e.getMessage(), e);
        }

        // 3. Agrupa os objetos ColetaProcesso pelo timestamp
        Map<String, List<ColetaProcesso>> groupedByTimestamp = processos.stream()
                .collect(Collectors.groupingBy(ColetaProcesso::getTimestamp));

        // 4. Constrói a estrutura de lista final: List<Map<String, Object>>
        List<Map<String, Object>> finalJsonStructure = new ArrayList<>();

        // 5. Itera sobre os grupos de timestamps para construir a estrutura aninhada
        for (Map.Entry<String, List<ColetaProcesso>> entry : groupedByTimestamp.entrySet()) {
            String timestamp = entry.getKey();
            List<ColetaProcesso> processosNoTempo = entry.getValue();

            // Lista para conter os objetos de processo no formato: [ {"nome": {detalhes}}, ... ]
            List<Map<String, Object>> processosListForTimestamp = new ArrayList<>();

            for (ColetaProcesso processo : processosNoTempo) {
                // Cria o objeto aninhado: {"nomeProcesso": {...detalhes...}}
                Map<String, Object> processoEntry = new HashMap<>();

                // Usa o nome do processo como chave e o toMap() para os valores
                processoEntry.put(processo.getNome_processo(), processo.toMap());
                processosListForTimestamp.add(processoEntry);
            }

            // Cria o objeto de nível superior: {"timestamp": [lista de processos aninhados]}
            Map<String, Object> timestampEntry = new HashMap<>();
            timestampEntry.put(timestamp, processosListForTimestamp);

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