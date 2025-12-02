package Controller;

import Dao.S3Dao;
import Service.S3Service;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class S3Controller {
    private S3Service s3Service;

    public S3Controller() {
        this.s3Service = new S3Service();
    }


    public List<String> getBucketRaw(String regiaoBucket, String nomeBucket) {
        List<String> result = new ArrayList<>();
        try {
            result = s3Service.pegarCsvBucket(regiaoBucket, nomeBucket);
        } catch (IOException e) {
            System.out.println("Não foi possível ler ou escrever dados nos csvs do bucket RAW.");
            e.printStackTrace();
        }
        return result;
    }

    public void csvsToTrusted(List<String> csvs) {
        s3Service.tratarCsvs(csvs);

    }
}
