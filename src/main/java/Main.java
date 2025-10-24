import Entity.ColetaServidor;
import org.springframework.jdbc.core.JdbcTemplate;

public class Main {
    public static void main(String[] args) {

        // Inicia BD
        Connection connection = new Connection();

        // Criação do jdbc
        JdbcTemplate jdbcTemplate = new JdbcTemplate(connection.getDataSource());

        // Criação do acesso aos dados
        ConfiguracaoServidorDAO configDao = new ConfiguracaoServidorDAO(jdbcTemplate);

        // Criação do processador
        CsvProcessor processador = new CsvProcessor(configDao);

        // Execução
        processador.leImportaArquivoCsvServidor("dados_capturados");
    }
}