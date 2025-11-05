package Controller;

import Dao.S3Dao;
import Service.S3Service;

public class S3Controller {
    S3Service s3Service;

    public S3Controller(S3Service s3Service, S3Dao s3Dao) {
        this.s3Service = new S3Service(s3Dao);
    }

    public void getBucketRaw() {

    }
}
