
package gamecore.project;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import gamecore.project.buckets.S3Interaction;
import gamecore.project.csvs.CsvUtils;
import gamecore.project.dao.ConfiguracaoServidorDAO;
import gamecore.project.database.Connection;
import gamecore.project.entity.ConfiguracaoServidor;
import gamecore.project.entity.DashboardData; // capturar o resultado do processamento
import gamecore.project.entity.Layout;
import gamecore.project.entity.Servidor;
import gamecore.project.mappers.Mapper; // pra botar no s3 dps
import gamecore.project.processors.DashboardProcessor; //  processador principal
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataClient implements RequestHandler<S3Event,String> {


    private static final String DESTINATION_BUCKET = "gamecore-bucket-client";
    private static final Region AWS_REGION = Region.US_EAST_1;


    private final S3Interaction s3Interaction = new S3Interaction();
    private final CsvUtils csvUtils = new CsvUtils();

    //componentes da dashboard
    private final DashboardProcessor dashboardProcessor = new DashboardProcessor();
    private final Mapper mapper = new Mapper(); // deixei ai pra qnd for pro s3

    // Conexao com o banco
    Connection connection = new Connection();
    JdbcTemplate con = new JdbcTemplate(connection.getDataSource());
    ConfiguracaoServidorDAO csd = new ConfiguracaoServidorDAO(con);


    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        String sourceBucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String sourceKey = s3Event.getRecords().get(0).getS3().getObject().getKey();

        context.getLogger().log("SOURCE_BUCKET = " +sourceBucket+ " SOURCE_KEY = " +sourceKey);

        //dai aqui eu só filtro pelo dia cuzao
        int pastaDia = sourceKey.indexOf('/');
        String caminhoPastaDia = sourceKey.substring(0, pastaDia + 1);

        try(S3Client s3Client = S3Client.builder()
                .region(AWS_REGION)
                .build()) {
            S3Interaction s3Interaction = new S3Interaction(); // Reutiliza a instância local
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

                    context.getLogger().log("[STARTING...] -  Iniciando leitura dos csvs para identificação de alerta e dashboard!");

                    // leitura e processamento do csv (E -> T)
                    for(String csvKey: csvsFilePath) {
                        String localFilePath = null;


                        try {
                            // pega o csv do trusted (E)
                            localFilePath = s3Interaction.readAndSaveFile(csvKey, sourceBucket, s3Client);


                            csvUtils.readAndGetAlerts(localFilePath, configsLayoutEmUso, context);

                            // processa pra mandar pra dashprocessor (onde rola o T)
                            DashboardData sumario = dashboardProcessor.generateDashboardSummary(localFilePath);


                            // pra fazer o L (kk) tem que botar o nome do bucket aqui (mas tem q ta em outro lambda)
                            // String jsonPayload = mapper.toJson(sumario);
                            // s3Interaction.uploadJsonToCurated(jsonPayload, "NOME-DO-MALDITO-BUCKET-DE-DESTINO", "dashboard_data.json", s3Client);
                            // esse dashboard_data.json é o arquivo q jason (transformado ja) que vai pro s3 e eu puxo do front no s3 view pra botar na dash

                            context.getLogger().log("Dashboard do MAC " + macadress + " processada (apenas E -> T) para o arquivo: " + csvKey);


                            //exception generico
                        } catch (Exception e) {
                            context.getLogger().log("Erro ao ler ou salvar o arquivo"+csvKey+" no bucket "+sourceBucket);
                            e.printStackTrace();
                        }
                    }

                }
            }
        } catch (Exception e) {
            context.getLogger().log("Erro crítico no handleRequest: " + e.getMessage());
            e.printStackTrace();
            // erro critico
            return "Erro crítico no pipeline.";
        }

        // sucesso
        return "/";
    }
}