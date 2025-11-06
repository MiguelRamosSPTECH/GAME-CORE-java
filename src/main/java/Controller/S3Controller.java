package Controller;

import Dao.S3Dao;
import Service.S3Service;

import java.io.IOError;
import java.io.IOException;

public class S3Controller {
    private S3Service s3Service;

    public S3Controller() {
        this.s3Service = new S3Service();
    }


    public void getBucketRaw(String regiaoBucket, String nomeBucket) {
        try {
            s3Service.pegarCsvBucket(regiaoBucket, nomeBucket);
        } catch (IOException e) {
            System.out.println("Não foi possível ler ou escrever dados nos csvs do bucket RAW.");
            e.printStackTrace();
        }
    }
}
