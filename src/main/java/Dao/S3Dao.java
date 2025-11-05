package Dao;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.FileOutputStream;
import java.io.IOException;

public class S3Dao {

    private final S3Client s3Client;
    private final String nomeBucket;

    public S3Dao(String regiaoBucket, String nomeBucket) {
        this.nomeBucket = nomeBucket;
        //estabelece conexão com o s3
        this.s3Client = S3Client.builder()
                .region(Region.of(regiaoBucket)) //regiao do s3
                .credentialsProvider(DefaultCredentialsProvider.create()) //procura credenciais da aws (AWS_ACCESS_KEY_ID e AWS_SECRET_ACCESS_KEY) em .env, aws/credentials, IAM.
                .build(); //cria instancia do s3
    }
    /*
    * caminhoArquivo -> onde o arquivo ta no bucket
    * caminhoRecebimento -> para onde, de forma local aq, o arquivo vai ser "salvo"
    *  */
    public Boolean getBucket(String caminhoArquivo, String caminhoRecebimento) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder() //o que e como pegar arquivo específico do bucket, versátil
                .bucket(this.nomeBucket)
                .key(caminhoArquivo)
                .build();

        //metadados e dados binários do arquivo, tipo de arqivo getObjectResponse( arquivo binário e metadados( cabecalho da requisição, formato, tipo)
        ResponseInputStream<GetObjectResponse> s3ObjectStream = s3Client.getObject(getObjectRequest);

        //bloco try-catch para pegar conteúdo do stream e salvar local aqui, nome e/ou caminho arquivo
        try (FileOutputStream outputStream = new FileOutputStream(caminhoRecebimento)) {
            //nao obriga a gente a tratar, a gnt só captura o log de erro e manda pro controller se virar

            //n entendi isso aqui nao
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = s3ObjectStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao salvar o arquivo local: " + e.getMessage());
            throw e; //lanca exceção pro controller saber o que deu bosta
        } finally {
            //Fecha arquivo binário que pegamo do s3, encerra "conexão"
            s3ObjectStream.close();
        }
    }
}
