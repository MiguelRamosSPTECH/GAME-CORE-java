import Controller.S3Controller;
import Dao.ConfiguracaoServidorDAO;
import Database.Connection;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

//        // Inicia BD
//        Connection connection = new Connection();
//
//        // Criação do jdbc
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(connection.getDataSource());
//
//        // Criação do acesso aos dados
//        ConfiguracaoServidorDAO configDao = new ConfiguracaoServidorDAO(jdbcTemplate);
//
//        // Criação do processador
//        CsvProcessor processador = new CsvProcessor(configDao);
//
//        // Execução
//        processador.leImportaArquivoCsvServidor("dados_capturados");
        S3Controller s3Controller = new S3Controller();
        s3Controller.getBucketRaw("us-east-1", "bucket-raw-gamecore");

    }
}