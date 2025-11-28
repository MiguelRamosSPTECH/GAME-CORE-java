package gamecore.project.csvs;

import com.amazonaws.services.lambda.runtime.Context;
import gamecore.project.entity.ConfiguracaoServidor;
import gamecore.project.mappers.Mapper;

import java.time.LocalDateTime;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class CsvUtils {


    public String tratarLinha(String linha){
        String[] campos = linha.split(";");
        Boolean camposNotConverted = false;
        for(int i=0;i<campos.length;i++) {

            //trata dados nulos para gerar alertas disso para o cara também
            if(campos[i] == null || campos[i].trim().isEmpty()) {
                campos[i] = "N/A";
            }
            //tirar espaços a+ em branco
            campos[i] = campos[i].trim();

            //arredondando
            try {
                Double valorDouble = Double.parseDouble(campos[i]);
                campos[i] = String.format("%.2f", valorDouble);
            } catch (NumberFormatException  e) {
                camposNotConverted = true;
            }

            //removendo zeros a esquerda
//            campos[i] = campos[i].replaceAll("^0+(?!$)", "");
        }

        if(camposNotConverted) System.out.println("Não foi possível converter alguns campos!");

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
    public void readAndGetAlerts(String csvLocalPath, List<ConfiguracaoServidor> configsLayoutEmUso, Context context) {
        DateTimeFormatter dataLinhaConvertida = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        List<String> arquivoTodo = null;
        List<String> linhasDados = null;
        Boolean deuRuim = false;

        try {
            arquivoTodo = Files.readAllLines(Paths.get(csvLocalPath));
        } catch (IOException e) {
            context.getLogger().log("Erro ao ler o arquivo!");
            e.printStackTrace();
            System.exit(1);
        }

        try {
            String linhaCabecalho = arquivoTodo.get(0); //pega primeira linha (cabecalho)
            String[] camposCabecalho = linhaCabecalho.split(";");

            List<LinkedHashSet<Object>> indicesDadosConfiguracao = new ArrayList<>();

            for (int i = 0; i < camposCabecalho.length; i++) {
                if (!camposCabecalho[i].contains("_")) continue;
                String pegaComponente = camposCabecalho[i].substring(0, camposCabecalho[i].lastIndexOf("_"));
                String pegaMetrica = camposCabecalho[i].substring(camposCabecalho[i].lastIndexOf("_"), camposCabecalho[i].length());
                for (ConfiguracaoServidor config : configsLayoutEmUso) {
                    if (config.getNomeComponente().equalsIgnoreCase(pegaComponente) && config.getNomeMetrica().equalsIgnoreCase(pegaMetrica)) {
                        LinkedHashSet<Object> dadosConfigIndex = new LinkedHashSet<>(Arrays.asList(i, config.getAlertaLeve(), config.getAlertaGrave()));
                        indicesDadosConfiguracao.add(dadosConfigIndex);
                    }
                }
            }

            //agora for para identificar os alertas
            LocalDateTime tempoMinimo = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).minusMinutes(5);
            linhasDados = arquivoTodo.subList(1 , arquivoTodo.size()); //da primeira linha em diante.
            Integer contaCpu = 0;
            Integer contaRam = 0;

            for(int i = linhasDados.size() - 1; i >=0; i--) {
                String linha = linhasDados.get(i);
                String[] separaCampos = linha.split(";");
                LocalDateTime campoEmData = null;

                //convertendo campo timestamp para data
                try {
                    campoEmData = LocalDateTime.parse(separaCampos[1], dataLinhaConvertida);
                } catch(DateTimeParseException e) {

                }
                //pegando campos que estão no período de até 5 minutos!
                if(campoEmData != null && campoEmData.isAfter(tempoMinimo)) {
                    //fazendo esquema de período só para cpu e ram!
                    for (int j = 0; j < indicesDadosConfiguracao.size(); j++) {
                        LinkedHashSet<Object> configData = indicesDadosConfiguracao.get(j);
                        Double dadoConvertido = null;
                        try {
                            dadoConvertido = Double.parseDouble(separaCampos[(Integer) configData.toArray()[0]]);
                            context.getLogger().log("DADO CSV: "  +dadoConvertido + "FAIXA ALERTA LEVE: " +(Double) configData.toArray()[1]);
                            //maior que alerta leve
                            if (dadoConvertido > (Double) configData.toArray()[1]) {
                                if (configsLayoutEmUso.get(j).getNomeMetrica().contains("CPU")) {
                                    contaCpu++;
                                } else if (configsLayoutEmUso.get(j).getNomeMetrica().contains("RAM")) {
                                    contaRam++;
                                } else {
                                    context.getLogger().log(configData.toArray().toString());
                                }
                            }
                        } catch (Exception e) {
                            context.getLogger().log("Alguns dados não foram convertidos!" + e.getMessage());

                        }

                    }
                }
                context.getLogger().log("CONTA CPU: " +  contaCpu + " CONTA RAM: "   +  contaRam);


            }

        } catch (Exception e) {
            context.getLogger().log("Erro no processamento do arquivo!");
            e.printStackTrace();
            deuRuim = true;
        } finally {
            if(deuRuim) {
                System.exit(1);
            }
        }

    }

    public void readAndGetAlerts(String[] linhasCsv, List<ConfiguracaoServidor> configsLayoutEmUso) {
        Mapper toJson = new Mapper();
        LocalDateTime ultimaDataAlerta = null;
        DateTimeFormatter dataLinhaConvertida = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Boolean deuRuim = false;

        Map<String, Integer> contagemAlertasLeve = new HashMap<>();
        Map<String, Integer> contagemAlertasGrave = new HashMap<>();


        try {
            String linhaCabecalho = linhasCsv[0]; //pega primeira linha (cabecalho)
            String[] camposCabecalho = linhaCabecalho.split(";");

            List<LinkedHashSet<Object>> indicesDadosConfiguracao = new ArrayList<>();

            for (int i = 0; i < camposCabecalho.length; i++) {
                if (!camposCabecalho[i].contains("_")) continue;
                String pegaComponente = camposCabecalho[i].substring(0, camposCabecalho[i].lastIndexOf("_"));
                String pegaMetrica = camposCabecalho[i].substring(camposCabecalho[i].lastIndexOf("_") + 1);
                if(pegaMetrica.equals("porcentagem")) pegaMetrica = "%";

                for (ConfiguracaoServidor config : configsLayoutEmUso) {
                    if (config.getNomeComponente().equalsIgnoreCase(pegaComponente) && config.getNomeMetrica().equalsIgnoreCase(pegaMetrica)) {

                        String nomeCompleto = config.getNomeComponente()+"_"+config.getNomeMetrica();
                        LinkedHashSet<Object> dadosConfigIndex = new LinkedHashSet<>(Arrays.asList(i, config.getAlertaLeve(), config.getAlertaGrave(), nomeCompleto));
                        indicesDadosConfiguracao.add(dadosConfigIndex);

                        contagemAlertasLeve.put(nomeCompleto, 0);
                        contagemAlertasGrave.put(nomeCompleto, 0);
                    }
                }
            }

            //agora for para identificar os alertas
            LocalDateTime tempoMinimo = LocalDateTime.now(ZoneId.of("America/Sao_Paulo")).minusMinutes(5);

            for(int i = linhasCsv.length - 1; i >=1; i--) {
                String linha = linhasCsv[i];
                String[] separaCampos = linha.split(";");
                LocalDateTime campoEmData = null;

                //convertendo campo timestamp para data
                try {
                    campoEmData = LocalDateTime.parse(separaCampos[1], dataLinhaConvertida);
                } catch(DateTimeParseException e) {
                    System.out.println(e.getMessage());
                }
                //pegando campos que estão no período de até 5 minutos!
//                if(campoEmData != null && campoEmData.isBefore(tempoMinimo)) {
                    //fazendo esquema de período só para cpu e ram!

                    for (int j = 0; j < indicesDadosConfiguracao.size(); j++) {
                        Boolean entrouAlerta = false;
                        LinkedHashSet<Object> configData = indicesDadosConfiguracao.get(j);
                        Double dadoConvertido = null;
                        try {
                            dadoConvertido = Double.parseDouble(separaCampos[(Integer) configData.toArray()[0]]);
                            Double faixaLeve = Double.parseDouble(String.valueOf(configData.toArray()[1]));
                            Double faixaGrave = Double.parseDouble(String.valueOf(configData.toArray()[2]));


                            if(dadoConvertido > faixaGrave){
                                contagemAlertasGrave.put(String.valueOf(configData.toArray()[3]), contagemAlertasGrave.get(String.valueOf(configData.toArray()[3])) + 1);
                                entrouAlerta = true;
                            } else if (dadoConvertido > faixaLeve) {
                                contagemAlertasLeve.put(String.valueOf(configData.toArray()[3]), contagemAlertasGrave.get(String.valueOf(configData.toArray()[3])) + 1);
                                entrouAlerta = true;
                            }

                            if (entrouAlerta) {
                                if(ultimaDataAlerta == null) {
                                    ultimaDataAlerta = campoEmData;
                                }
                            }

                        } catch (Exception e) {
                            System.out.println("Alguns dados não foram convertidos!" + e.getMessage());

                        }
                    }
//                }
            }

            toJson.createJsonAlertGeral(contagemAlertasLeve, contagemAlertasGrave, ultimaDataAlerta, linhasCsv.length);

        } catch (Exception e) {
            System.out.println("Erro no processamento do arquivo!");
            e.printStackTrace();
            deuRuim = true;
        } finally {
            if(deuRuim) {
                System.exit(1);
            }
        }

    }

}