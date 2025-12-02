package school.sptech;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CsvWriter {

        // Criar um CSV em memória utilizando ByteArrayOutputStream
        public ByteArrayOutputStream writeCsv(ResultadoMedia resultado) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

            // Processa e escreve os objetos, no caso, só a média
            CSVPrinter csvPrinter = new CSVPrinter(writer,
                    CSVFormat.DEFAULT.withHeader("media_cpu_porcentagem", "media_ram_porcentagem"));

            // Escrever apenas o registro da média
            csvPrinter.printRecord(
                    resultado.getMediaCpu(),
                    resultado.getMediaRam()
            );

            // Fechar o CSV para garantir que todos os dados sejam escritos
            csvPrinter.flush();
            writer.close();

            // Retornar o ByteArrayOutputStream que contém o CSV gerado
            return outputStream;
        }
    }
