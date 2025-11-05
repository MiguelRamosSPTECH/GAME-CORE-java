package Service;

import Dao.S3Dao;
import org.springframework.cglib.core.Local;

import java.io.IOException;
import java.time.LocalDate;

public class S3Service {
    private final S3Dao s3Dao;

    public S3Service(S3Dao s3Dao) {
        this.s3Dao = s3Dao;
    }

    public void pegarCsvBucket() throws IOException {
        LocalDate date = LocalDate.now();

        String caminhoS3 = "";
    }

}
