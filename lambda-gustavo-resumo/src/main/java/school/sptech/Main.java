package school.sptech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class Main implements RequestHandler<S3Event, String> {

    // Criação do cliente S3 para acessar os buckets
    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

    // Bucket de destino para o CSV gerado
    private static final String DESTINATION_BUCKET = "bucket-lambda-3678";

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        String sourceBucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String sourceKey = s3Event.getRecords().get(0).getS3().getObject().getKey();

        // Define a chave do arquivo de saída
        String destinationKey = sourceKey.replace(".csv", "_media.json");

        try (S3ObjectInputStream s3InputStream = s3Client.getObject(sourceBucket, sourceKey).getObjectContent()) {

            // Usa o mapper pra gerar uma lista de objetos, passando o arquivo lido como parametro
            Mapper mapper = new Mapper();
            List<Stock> stocks = mapper.map(s3InputStream);

            // calcula  a medida do objeto stock gerado
            ResultadoMedia resultado = calcularMedia(stocks);

            // faz a serialização - converte os dados para bytes
            ByteArrayOutputStream jsonOutputStream = serializarResultadoJson(resultado);

            // envia para o S3
            InputStream jsonInputStream = new ByteArrayInputStream(jsonOutputStream.toByteArray());
            s3Client.putObject(DESTINATION_BUCKET, destinationKey, jsonInputStream, null);

            return "Sucesso! Média de recursos salva em JSON: " + destinationKey;

        } catch (Exception e) {
            context.getLogger().log("Erro na ETL: " + e.getMessage());
            e.printStackTrace();
            return "Erro no processamento";
        }
    }

    private ResultadoMedia calcularMedia(List<Stock> stocks) {
        // validação pra garantir que não vai ter divissão por 0
        if (stocks == null || stocks.isEmpty()) {
            return new ResultadoMedia(0.0, 0.0);
        }

        double somaCpu = 0.0;
        double somaRam = 0.0;

        Integer contador = stocks.size();

        for (Stock stock : stocks) {
            somaCpu += stock.getCpu_porcentagem();
            somaRam += stock.getRam_porcentagem();
        }

        double mediaCpu = somaCpu / contador;
        double mediaRam = somaRam / contador;

        // Retorna um novo objeto com as médias calculadas
        return new ResultadoMedia(mediaCpu, mediaRam);
    }

    private ByteArrayOutputStream serializarResultadoJson(ResultadoMedia resultado) throws Exception {
        ObjectMapper mapper = new ObjectMapper();   // cria um novo mapeador

        // Converte o objeto Java diretamente para um array de bytes JSON pelo jackson (serializar)
        byte[] jsonBytes = mapper.writeValueAsBytes(resultado);

        // Novo objeto para salvar os dados temporariamente
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //Escreve os dados salvos anteriormente
        outputStream.write(jsonBytes);
        return outputStream;
    }


}  