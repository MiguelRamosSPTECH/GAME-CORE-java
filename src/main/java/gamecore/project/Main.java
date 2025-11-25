package gamecore.project;

import gamecore.project.csvs.CsvUtils;
import software.amazon.awssdk.regions.Region;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import gamecore.project.buckets.S3Interaction;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.List;
import java.util.Set;


public class Main implements RequestHandler<S3Event, String> {
        private static final String DESTINATION_BUCKET = "gamecore-bucket-trusted";

        //client para interagir la com o bucket
        private static final Region AWS_REGION = Region.US_EAST_1;

        private final S3Interaction s3Interaction = new S3Interaction();
        private final CsvUtils csvUtils = new CsvUtils();

        //parte lambda
        @Override
        public String handleRequest(S3Event s3Event, Context context) {
                String sourceBucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
                //pega caminho absoluto do arquivo e dai eu filto pelo dia que foi inserido tlgd
                String sourceKey = s3Event.getRecords().get(0).getS3().getObject().getKey();

                //dai aqui eu só filtro pelo dia cuzao
                int pastaDia = sourceKey.indexOf('/');
                String caminhoPastaDia = sourceKey.substring(0, pastaDia + 1);

                try(S3Client s3Client = S3Client.builder()
                        .region(AWS_REGION)
                        .build()) {
                    S3Interaction s3Interaction = new S3Interaction();
                    Set<String> macCaminhoPastas = s3Interaction.getCaminhosMac(sourceBucket, caminhoPastaDia,  context, s3Client);

                    context.getLogger().log("Pastas de MAC Address encontradas:" + macCaminhoPastas.size());
                    for (String macKey : macCaminhoPastas) {
                        context.getLogger().log("Iniciando processamento para prefixo: " + macKey);
                        List<String> originalS3Keys = s3Interaction.getArquivosCsvDoMac(sourceBucket, macKey, context, s3Client);
                        context.getLogger().log(String.format("Arquivos CSV encontrados em %s: %d", macKey, originalS3Keys.size()));

                        for(String s3keys: originalS3Keys) {
                            String localFilePath = null;

                            try {
                                context.getLogger().log("-> Processando arquivo: " + s3keys);

                                localFilePath = s3Interaction.readAndSaveFile(s3keys, sourceBucket, s3Client);
                                csvUtils.leTrataArquivoCsv(localFilePath);

                                s3Interaction.uploadCsvToTrusted(localFilePath, DESTINATION_BUCKET, s3keys, s3Client);
                                context.getLogger().log("-> Arquivo processado e carregado em: " + s3keys);

                            } catch (IOException | RuntimeException e) {
                                context.getLogger().log("ERRO ao processar " + s3keys + ". Motivo: " + e.getMessage());
                                // Continua para o próximo item
                            }
                        }

                    }

                } catch (Exception e) {
                    context.getLogger().log("ERRO FATAL NO FLUXO PRINCIPAL: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Erro inesperado no Handler: " + e.getMessage(), e);
                }
                context.getLogger().log("Envio para o bucket trusted concluído!");
                return "Processamento ETL concluído com sucesso.";
        }

}