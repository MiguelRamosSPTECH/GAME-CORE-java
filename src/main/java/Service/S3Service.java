package Service;

import Dao.ConfiguracaoServidorDAO;
import Dao.CsvUtils;
import Dao.S3Dao;
import Database.Connection;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class S3Service {
    private S3Dao s3Dao;


    public List<String> pegarCsvBucket(String regiaoBucket, String nomeBucket) throws IOException {
        s3Dao = new S3Dao(regiaoBucket, nomeBucket);
        List<String> csvs = s3Dao.getCsvs();
        if(csvs.isEmpty()) {
            System.out.println("======== NENHUM ARQUIVO CSV ENCONTRADO EM TEMPO REAL [X] ===========");
        } else {
            s3Dao.readAndSaveFile(csvs);
        }
        return csvs;
    }

    public void tratarCsvs(List<String> csvs) {
        CsvUtils csv = new CsvUtils();

        String csvTratado;
        for(int i=0;i<csvs.size();i++) {
            csvTratado = csvs.get(i).split("/")[1];
            csv.leTrataArquivoCsv(csvTratado);

        }





    }

}
