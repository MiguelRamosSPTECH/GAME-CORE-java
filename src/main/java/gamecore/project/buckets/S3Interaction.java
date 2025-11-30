package gamecore.project.buckets;

import com.amazonaws.services.lambda.runtime.Context;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import software.amazon.awssdk.regions.Region;

public class S3Interaction {

    private static final String BUCKET_GOLD_NAME = "curated-gamecore";
    private static final Region AWS_REGION = Region.US_EAST_1;

    /*
     * Envia o JSON de KPIs calculados para o bucket S3.
     * @param jsonContent A string JSON contendo os KPIs.
     * @param periodo Identificador do período (ex: "7D", "30D").
     * @return true se o upload foi bem-sucedido.
     */

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

    //testar isso aq p ver se manda o json pro client

//    public void uploadJsonToCurated(String jsonPayload, String bucketName, String key, S3Client s3Client) {
//
//        // Converte a String JSON em bytes usando UTF-8
//        RequestBody requestBody = RequestBody.fromBytes(jsonPayload.getBytes(StandardCharsets.UTF_8));
//
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(key)
//                .contentType("application/json") // Define o tipo de conteúdo como JSON
//                .build();
//
//        s3Client.putObject(putObjectRequest, requestBody);
//
//        // Log de sucesso
//        System.out.println("JSON da Dashboard enviado com sucesso para s3://" + bucketName + "/" + key);
//    }




    public InputStream getFileStream(String bucketName, String key, S3Client s3Client) {
        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build();
        return s3Client.getObject(getObjectRequest);
    }

    public void uploadJsonToS3(S3Client s3Client, String sourceKey, String jsonPayload, Context context, String destination_bucket) {
        // Gera um nome para o arquivo JSON de destino (ex: "pasta/nome_original.json")
        String destinationKey = sourceKey.replace(".csv", ".json").replace(".CSV", ".json");

        PutObjectRequest putReq = PutObjectRequest.builder()
                .bucket(destination_bucket)
                .key(destinationKey)
                .contentType("application/json")
                .build();

        s3Client.putObject(putReq, RequestBody.fromString(jsonPayload));
        context.getLogger().log("JSON enviado para " + destination_bucket + "/" + destinationKey);
    }

    public boolean uploadJsonKpi(String jsonContent, String periodo) {

        // Ex: O nome do arquivo será "kpis/sre_kpis_7D_2025-11-29.json"
        String key = String.format("kpis/sre_kpis_%s_%s.json",
                periodo,
                LocalDate.now().format(DateTimeFormatter.ISO_DATE));

        // Inicializa o cliente S3
        S3Client s3 = S3Client.builder()
                .region(AWS_REGION)
                .build();

        try {
            PutObjectRequest putObject = PutObjectRequest.builder()
                    .bucket(BUCKET_GOLD_NAME)
                    .key(key)
                    .contentType("application/json")
                    .build();

            // Executa o upload
            s3.putObject(putObject, RequestBody.fromString(jsonContent));

            System.out.printf("SUCESSO: JSON KPI SRE enviado para s3://%s/%s%n", BUCKET_GOLD_NAME, key);
            return true;

        } catch (Exception e) {
            System.err.println("ERRO ao enviar arquivo para o S3: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            s3.close();
        }
    }



}
