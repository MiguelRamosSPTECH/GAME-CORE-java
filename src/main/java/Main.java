import Controller.S3Controller;
import Dao.ConfiguracaoServidorDAO;
import Database.Connection;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        String dtNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm:ss"));
        System.out.printf("c\n" +
                "Logs de interações com os arquivos csvs\n" +
                "Data de Início: [%s]\n" +
                "Diretório de Trabalho: ...\n" +
                "===============================================\n", dtNow);

        dtNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd - HH:mm:ss"));
        System.out.printf("[%s] INFO: Iniciando a execução do script.\n\n", dtNow);



        S3Controller s3Controller = new S3Controller();
        List<String> csvs = s3Controller.getBucketRaw("us-east-1", "bucket-raw-gamecore");
        s3Controller.csvsToTrusted(csvs);

    }
}