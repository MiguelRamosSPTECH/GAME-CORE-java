package school.sptech;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Mapper {

    public List<Stock> map(InputStream inputStream) throws IOException {
        List<Stock> stocks = new ArrayList<>();

        // Salva os headers que vão ser lidos
        String[] HEADERS = { "timestamp", "cpu_porcentagem", "ram_porcentagem" };


        try (Reader reader = new InputStreamReader(inputStream)) {

            // 2. O CSVParser usa o Reader para processar o arquivo inteiro
            CSVParser parser = new CSVParser(reader,    //abre o arquivo
                    CSVFormat.DEFAULT                   // Define o formato básico do arquivo - csv no caso.
                            .withHeader(HEADERS)        // deixa eu usar (record.get("cpu_porcentagem")) em vez da posição pelo indice
                            .withFirstRecordAsHeader()  // Fala pra ignorar a primeira linha - é o cabeçalho
                            .withIgnoreHeaderCase()     // ignora letras maiusculas e minusculas pra evitar erros
                            .withTrim()                 // faz o trim -> ignora os espaços para evitar erros
            );

            // 3. O parser itera sobre os REGISTROS (linhas) e trata o CSV
            for (CSVRecord record : parser) {
                // Lógica de mapeamento de cada registro para um objeto
                Stock stock = new Stock();
                stock.setCpu_porcentagem(Double.parseDouble(record.get("cpu_porcentagem")));
                stock.setRam_porcentagem(Double.parseDouble(record.get("ram_porcentagem")));
                stocks.add(stock);
            }
        }
        return stocks;
    }
}