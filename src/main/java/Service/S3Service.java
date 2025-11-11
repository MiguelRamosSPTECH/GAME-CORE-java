package Service;

import Dao.S3Dao;
import org.springframework.cglib.core.Local;

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

    public void tratarCsvs(String csvName) {
        //arredondar numeros double e limitar casas decimais
        //tratar valores nulos


    }

}
