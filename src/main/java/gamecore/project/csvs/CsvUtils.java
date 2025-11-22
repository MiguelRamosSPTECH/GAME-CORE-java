package gamecore.project.csvs;

import gamecore.project.entity.ConfiguracaoServidor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class CsvUtils {


    public String tratarLinha(String linha){
        String[] campos = linha.split(";");
        for(int i=0;i<campos.length;i++) {

            //trata dados nulos para gerar alertas disso para o cara também
            if(campos[i] == null || campos[i].trim().isEmpty()) {
                campos[i] = "N/A";
            }
            //tirar espaços a+ em branco
            campos[i] = campos[i].trim();

            //removendo zeros a esquerda
            campos[i] = campos[i].replaceAll("^0+(?!$)", "");
        }
        //junta tudo dnv tratado boy
        return String.join(";", campos);

    }

    public void leTrataArquivoCsv(String localFilePath) {
        String nomeArqTemporario = localFilePath.replaceAll("\\.csv$", "_temp.csv");

        //pra que o Path?
        Path pathOriginal = Paths.get(localFilePath);
        Path pathTemporario = Paths.get(nomeArqTemporario);

        Boolean deuRuim = false; //caso der ruim sair no finally (n pode sair do nada)

        try {
            //Esses path é para manipular melhor os arquuivo( caminho e blablabla)
            FileReader leitorArqOriginal = new FileReader(pathOriginal.toFile(), StandardCharsets.UTF_8);
            Scanner entrada = new Scanner(leitorArqOriginal);

            //abrindo arquivo temp
            OutputStreamWriter saidaTemp = new OutputStreamWriter(
                    new FileOutputStream(pathTemporario.toFile()),
                    StandardCharsets.UTF_8
            );

            //Loop para ler e gravar os dados
            while(entrada.hasNextLine()) {
                String linha = entrada.nextLine();

                //Agr é para tratar os dados men
                String linhaTrata = tratarLinha(linha);

                saidaTemp.write(linhaTrata);

                if(entrada.hasNextLine()) {
                    saidaTemp.write("\n"); //vai quebrando a linha dps de tratar linha todaa
                }
            }
            saidaTemp.flush();
            System.out.println("\n------ DADOS TRATADOS COM SUCESSO NO ARQUIVO TEMPORÁRIO! ------");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado +"+ localFilePath);
            deuRuim = true;
        } catch (IOException e) {
            System.out.println("Erro durante o tratamento/leitura/escrita" + e.getMessage());
            deuRuim = true;
            e.printStackTrace();
        } finally {
            if(!deuRuim) {
                try {
                    Files.move(pathTemporario, pathOriginal, StandardCopyOption.REPLACE_EXISTING);
                    System.out.printf("%s foi sobrescrito e tratado com sucesso.\n", localFilePath);
                } catch (IOException e) {
                    System.out.println("Erro ao finalizar a sobrescrita! Arquivo temporário pode ter ficado!");
                }
            } else {
                try {
                    Files.deleteIfExists(pathTemporario);
                } catch (IOException e) {
                    System.out.println("Erro ao tentar deletar o arquivo temporário!"+ e);
                }
            }
        }
    }

    //CLIENT
    public void readAndGetAlerts(String csvLocalPath, List<ConfiguracaoServidor> configsLayoutEmUso) {
        FileReader arq = null;

        Scanner entrada = null;
        Boolean deuRuim = false;

        try {
            arq = new FileReader(csvLocalPath);
            entrada = new Scanner(arq).useDelimiter(";|\\n");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo inexistente!");
            System.exit(1);
        }

        try {
            Boolean cabecalho = true;
            List<LinkedHashSet<Object>> indicesDadosConfiguracao = new ArrayList<>();


            while(entrada.hasNextLine()) {
                indicesDadosConfiguracao = new ArrayList<>();
                String linha = entrada.nextLine();
                String[] camposSeparados = linha.split(";");

                if(cabecalho) {
                    for(int i=0;i<camposSeparados.length;i++) {
                        String pegaComponente = camposSeparados[i].substring(0, camposSeparados[i].lastIndexOf("_"));
                        String pegaMetrica = camposSeparados[i].substring(camposSeparados[i].lastIndexOf("_"), camposSeparados.length);

                        for(ConfiguracaoServidor config : configsLayoutEmUso) {
                            if(config.getNome_componente().equalsIgnoreCase(pegaComponente) && config.getNome_metrica().equalsIgnoreCase(pegaMetrica)) {
                                LinkedHashSet<Object> dadosConfigIndex = new LinkedHashSet<>(Arrays.asList(i, config.getAlertaLeve(), config.getAlertaGrave()));
                                indicesDadosConfiguracao.add(dadosConfigIndex);
                            }
                        }

                    }
                    cabecalho = false;
                } else {
                    for(int i=0;i<indicesDadosConfiguracao.size();i++) {
                        //dai aqui vai ver se a linha ta em alerta ou nao para poder fazer o esquema de tempo recorrente de alerta pra poder jogar para os json
                    }
                }
            }s
        } catch (NoSuchElementException e) { //tenta ler proximo dado e n consegue (.next())
            System.out.println("Dados de leitura inconsistentes e/ou corrompidos!");
            e.printStackTrace();
            deuRuim = true;
        } catch (IllegalStateException e) {//uso do Scanner sem conseguir usar ele
            System.out.println("Erro na leitura do arquivo!");
            e.printStackTrace();
            deuRuim = true;
        } finally {
            try {
                arq.close(); //fechar arquivo
                entrada.close(); //fechar scanner
            } catch (IOException e) {
                System.out.println("Erro ao fechar arquivo!");
                deuRuim = true;
            }
            if(deuRuim) {
                System.exit(1);
            }
        }

    }

}