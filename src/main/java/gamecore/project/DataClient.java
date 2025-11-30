package gamecore.project;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import gamecore.project.buckets.S3Interaction;
import gamecore.project.csvs.CsvUtils;
import gamecore.project.dao.ConfiguracaoServidorDAO;
import gamecore.project.database.Connection;
import gamecore.project.entity.ColetaContainer;
import gamecore.project.entity.ConfiguracaoServidor;
import gamecore.project.entity.Layout;
import gamecore.project.entity.Servidor;
import gamecore.project.mappers.ColetaContainerMapper;
import gamecore.project.mappers.ColetaProcessoMapper;
import gamecore.project.mappers.ColetaServidorMapper;
import gamecore.project.mappers.Mapper;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataClient implements RequestHandler<S3Event,String> {
    private static final String DESTINATION_BUCKET = "gamecore-bucket-bucket-bucket-client";
    private static final Region AWS_REGION = Region.US_EAST_1;

    private final S3Interaction s3Interaction = new S3Interaction();
    private final CsvUtils csvUtils = new CsvUtils();


    //Conexao com o banco
    Connection connection = new Connection();
    JdbcTemplate con = new JdbcTemplate(connection.getDataSource());
    ConfiguracaoServidorDAO csd = new ConfiguracaoServidorDAO(con);
    Mapper mapper = new Mapper();
    ColetaServidorMapper csm = new ColetaServidorMapper();
    ColetaProcessoMapper cpm = new ColetaProcessoMapper();
    ColetaContainerMapper ccm = new ColetaContainerMapper();


    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        String sourceBucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String sourceKey = s3Event.getRecords().get(0).getS3().getObject().getKey();

        context.getLogger().log("SOURCE_BUCKET = " +sourceBucket+ " SOURCE_KEY = " +sourceKey);

        //aqui eu só filtro pelo dia
        int pastaDia = sourceKey.indexOf('/');
        String caminhoPastaDia = sourceKey.substring(0, pastaDia + 1);

        try(S3Client s3Client = S3Client.builder()
                .region(AWS_REGION)
                .build()) {
            S3Interaction s3Interaction = new S3Interaction();
            Set<String> macCaminhoPastas = s3Interaction.getCaminhosMac(sourceBucket, caminhoPastaDia, context, s3Client);


            for(String macKey: macCaminhoPastas) {


                String macadress = macKey.substring(macKey.indexOf("/") + 1, macKey.lastIndexOf("/"));

                //parte de conexao com o banco para identificar os alertas
                Servidor servidorByMacKey = csd.buscarServidorPorMac(macadress);
                Layout layoutEmUso = null;
                List<ConfiguracaoServidor> configsLayoutEmUso = new ArrayList<>();

                if(servidorByMacKey == null) {

                    return "[SAINDO... ]Nenhum servidor de macadress: !";

                } else {
                    context.getLogger().log("INFORMAÇÕES DO SERVIDOR "+macadress+" CAPTURADO " + servidorByMacKey.toString());

                    //if para ver se o servidor tem uma config atrelada ou nao
                    if(servidorByMacKey.getFk_layout() == null) {

                        layoutEmUso = csd.buscarLayoutPorFkEmpresa(servidorByMacKey.getFk_empresa_servidor());

                    } else {
                        //se tiver config atrelada vai para ca
                        layoutEmUso = csd.buscarLayoutPorFkLayout(servidorByMacKey.getFk_layout());
                    }

                    //salva configs dos layouts para filtrar nas linhas do csv
                    configsLayoutEmUso = csd.buscarConfiguracaoLayout(layoutEmUso.getId());
                    context.getLogger().log("Pegando cada arquivo CSV da pasta: "+ macKey);
                    context.getLogger().log("CONFIGURAÇÃO EM USO: " + configsLayoutEmUso.toString());


                    List<String> csvsFilePath = s3Interaction.getArquivosCsvDoMac(sourceBucket, caminhoPastaDia, context, s3Client);
                    context.getLogger().log("Arquivos do bucket-trusted encontrados: " + csvsFilePath.size());

                    context.getLogger().log("[STARTING...] -  Iniciando leitura dos csvs para identificação de alerta!");
                    for(String csvKey: csvsFilePath) {

                        String localFilePath = null;
                        context.getLogger().log("CHAVE_CSV: "+ csvKey);

                        String nomeCsv = csvKey.substring(csvKey.lastIndexOf("/")+1, csvKey.length());
                        try(InputStream csvStream = s3Interaction.getFileStream(sourceBucket, csvKey, s3Client)) {
                            String arquivoConvertidoJson = null;
//                            localFilePath = s3Interaction.readAndSaveFile(csvKey, sourceBucket, s3Client);

                            if(nomeCsv.equals("dados_capturados.csv")){
                                arquivoConvertidoJson = csm.converterCsvParaJsonArray(csvStream);
//                                csvUtils.readAndGetAlerts(localFilePath, configsLayoutEmUso, context, servidorByMacKey.getApelido());

                            } else if(nomeCsv.equals("dados_processos.csv")) {
                                arquivoConvertidoJson = cpm.converterCsvParaJsonAninhado(csvStream);
                            } else {
                                arquivoConvertidoJson = ccm.converterCsvParaJsonAninhado(csvStream);
                            }
                            context.getLogger().log("ARQUIVO JSON AI: "+ arquivoConvertidoJson);

                            s3Interaction.uploadJsonToS3(s3Client, csvKey, arquivoConvertidoJson, context, DESTINATION_BUCKET);
                        } catch (IOException e) {
                            context.getLogger().log("Erro ao ler ou salvar o arquivo "+csvKey+" no bucket "+sourceBucket);
                            e.printStackTrace();
                        }
                    }

                }
            }
        }

        return "/";
    }
}
