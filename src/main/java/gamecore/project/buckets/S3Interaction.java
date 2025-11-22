package gamecore.project.buckets;

import com.amazonaws.services.lambda.runtime.Context;

// Imports do SDK V2
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;


public class S3Interaction {


    //metodo para pegar todos os arquivos do bucket e filtrar pelo mais recente
    public Set<String> getCaminhosMac(String bucket, String diaPrefixo, Context context, S3Client s3Client) {

        //pega tudo que ta dentro do bucket
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder() //retorna lista de objetos do bucket
                .bucket(bucket)
                .prefix(diaPrefixo)
                .build();

        //cria paginador para eu poder acessar os arquivos (iterar sobre eles)
        ListObjectsV2Iterable listResponse = s3Client.listObjectsV2Paginator(listRequest);

        Set<String> macKeys = listResponse.contents().stream()
                        .map(S3Object::key)
                                .filter(key -> key.endsWith(".csv"))
                                        .map(key -> {
                                            int ultimaBarra = key.lastIndexOf('/');
                                            return key.substring(0, ultimaBarra + 1);
                                        })
                                                .collect(Collectors.toSet());

        context.getLogger().log("As pastas com cada macadress foram encontradas! Tamanho: " + macKeys.size());
        return macKeys;
    }


    public List<String> getArquivosCsvDoMac(String bucket, String macPrefixo, Context context, S3Client s3Client) {

        // Requisição para listar objetos dentro da pasta de MAC específica
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .prefix(macPrefixo) // Usa o prefixo completo do macaddress
                .build();

        ListObjectsV2Iterable listResponse = s3Client.listObjectsV2Paginator(listRequest);

        // Filtra todas as chaves que terminam com .csv, retornando o caminho completo
        List<String> csvKeys = listResponse.contents().stream()
                .map(S3Object::key)
                .filter(key -> key.toLowerCase().endsWith(".csv"))
                .collect(Collectors.toList());

        context.getLogger().log("Encontrados " + csvKeys.size() + " arquivos CSV em " + macPrefixo);
        return csvKeys;
    }

    //nao obriga a gente a tratar, a gnt só captura o log de erro e manda pro controller se virar
    public String readAndSaveFile(String fileKey, String sourceBucket, S3Client s3Client) throws IOException {

            //pelo visto a lambda joga os arquivo temporário criados em /tmp/
            //dai aqui fica /tmp/yyyy-mm-dd HH:mm/nome_arquivo.csv
            String localFileName = fileKey.substring(fileKey.lastIndexOf('/') + 1);
            String localFilePath = "/tmp/" + localFileName;

            //o que e como pegar arquivo específico do bucket, versátil
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(sourceBucket)
                .key(fileKey)
                .build();

            //metadados e dados binários do arquivo, tipo de arqivo getObjectResponse( arquivo binário e metadados( cabecalho da requisição, formato, tipo)
            ResponseInputStream<GetObjectResponse> s3ObjectStream = s3Client.getObject(getObjectRequest);

            //bloco try-catch que cria um arquivo de saída com o nome especifico para escrever dados e salvar local
            try (FileOutputStream outputStream = new FileOutputStream(localFilePath)) {

                //1024 bytes, padrão para ler arquivos (4096, 512, 1024), le de 1024 em 1024 (1KB)
                byte[] buffer = new byte[1024];

                //bytesRead é para identificar quando o arquivo terminar, nao ficar no loop infinito
                int bytesRead;
                while((bytesRead = s3ObjectStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0 , bytesRead);
                }
                return localFilePath;
            } catch(IOException e) {
                System.err.printf("[%s] - Erro ao salvar o arquivo %s local: \n" + e.getMessage(), localFilePath);
                throw e; //lanca exceção pro controller saber o que deu ruim
            } finally {
                s3ObjectStream.close();
            }
    }


    //TRUSTED
    public void uploadCsvToTrusted(String localFilePath, String bucketDestino, String s3Key, S3Client s3Client) throws IOException {

        File fileToUpload = new File(localFilePath);

        System.out.printf("[CMD> Iniciando upload do CSV tratado para TRUSTED\n");
        try {

            //criando req para jogar no trusted
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketDestino)
                    .key(s3Key)
                    .contentType("text/csv")
                    .build();

            //Faz o upload
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(fileToUpload));
            System.out.printf("[OK...] = Upload do %s no bucket trusted!!! \n", localFilePath);
        } catch (SdkClientException e) {
            System.err.printf("Erro ao enviar CSV para Trusted: %s\n", e.getMessage());
            throw new IOException("Falha no pipeline Trusted", e);
        }
    }





}
