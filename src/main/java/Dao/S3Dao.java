package Dao;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


public class S3Dao {

    private final S3Client s3Client;
    private final String nomeBucket;
    private DateTimeFormatter formatter;

    public S3Dao(String regiaoBucket, String nomeBucket) {
        this.nomeBucket = nomeBucket;

        //estabelece conexão com o s3
        this.s3Client = S3Client.builder()
                .region(Region.of(regiaoBucket)) //regiao do s3
                .credentialsProvider(DefaultCredentialsProvider.create()) //procura credenciais da aws (AWS_ACCESS_KEY_ID e AWS_SECRET_ACCESS_KEY) em .env, aws/credentials, IAM.
                .build(); //cria instancia do s3
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }


    //metodo para pegar todos os arquivos do bucket e filtrar pelo mais recente
    public List<String> getCsvs() {
        String dtNow = LocalDateTime.now().format(formatter);

        //pega tudo que ta dentro do bucket
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder() //retorna lista de objetos do bucket
                .bucket(this.nomeBucket)
                .prefix(dtNow.split(" ")[0] + "/")
                .build();

        //cria paginador para eu poder acessar os arquivos (iterar sobre eles)
        ListObjectsV2Iterable listResponse = s3Client.listObjectsV2Paginator(listRequest);

        List<String> csvKeys = listResponse.contents().stream()
                //cria arquivo fantasma com nome da pasta, dai descarta ele e filtra pelos arquivos .csv
                .filter(obj -> obj.size() > 0 && obj.key().toLowerCase().endsWith(".csv"))
                .map(S3Object::key) //ou obj -> obj.key() mas de forma direta, referencia de metodo (pra cada s3 object, pega o key)
                .collect(Collectors.toList()); //atribui para a lista mesmo (csvKeys)

        System.out.printf("[%s] CMD> cat 'nome dos csvs capturados com sucesso!!!'\n", dtNow);
        return csvKeys;
    }

    //nao obriga a gente a tratar, a gnt só captura o log de erro e manda pro controller se virar
    public Boolean readAndSaveFile(List<String> csvKeys) throws IOException {

        String dtNow = LocalDateTime.now().format(formatter);
        System.out.printf("\n[%s] CMD> cat 'Iniciando o processamento de " + csvKeys.size() + " arquivos CSV'\n\n", dtNow);

        for(String fileKey : csvKeys) {
            dtNow = LocalDateTime.now().format(formatter);
            String novoNome = fileKey.split("/")[1];
            System.out.printf("[%s] CMD> execute 'Lendo e salvando o arquivo %s'\n", dtNow, fileKey);

            //o que e como pegar arquivo específico do bucket, versátil
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(this.nomeBucket)
                .key(fileKey)
                .build();

            //metadados e dados binários do arquivo, tipo de arqivo getObjectResponse( arquivo binário e metadados( cabecalho da requisição, formato, tipo)
            ResponseInputStream<GetObjectResponse> s3ObjectStream = s3Client.getObject(getObjectRequest);

            //bloco try-catch que cria um arquivo de saída com o nome especifico para escrever dados e salvar local
            try (FileOutputStream outputStream = new FileOutputStream(novoNome)) {

                //1024 bytes, padrão para ler arquivos (4096, 512, 1024), le de 1024 em 1024 (1KB)
                byte[] buffer = new byte[1024];

                //bytesRead é para identificar quando o arquivo terminar, nao ficar no loop infinito
                int bytesRead;
                while((bytesRead = s3ObjectStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0 , bytesRead);
                }
            } catch(IOException e) {
                System.err.printf("[%s] - Erro ao salvar o arquivo %s local: \n" + e.getMessage(), novoNome, dtNow);
                throw e; //lanca exceção pro controller saber o que deu ruim
            } finally {
                s3ObjectStream.close();
            }
        }
        return true;
    }


    //TRUSTED
    public void uploadCsvToTrusted(String csv) throws IOException {
        String dtNow = LocalDateTime.now().format(formatter);

        File fileToUpload = new File(csv);

        String trustedKey = String.format("%s/%s", dtNow.split(" ")[0], csv);

        System.out.printf("[CMD> Iniciando upload do CSV tratado para TRUSTED\n");
        try {

            //criando req para jogar no trusted
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(this.nomeBucket)
                    .key(trustedKey)
                    .contentType("text/csv")
                    .build();

            //Faz o upload
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(fileToUpload));
            System.out.printf("[OK...] = Upload do %s no bucket trusted!!! \n", csv);
        } catch (SdkClientException e) {
            System.err.printf("Erro ao enviar CSV para Trusted: %s\n", e.getMessage());
            throw new IOException("Falha no pipeline Trusted", e);
        }



    }
}
