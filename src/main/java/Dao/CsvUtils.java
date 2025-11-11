package Dao;

import Entity.*;
import Jira.JiraInteraction;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvUtils {

    private final ConfiguracaoServidorDAO configuracaoDAO;
    private final List<String> dadosEmAlertaServidor = new ArrayList<>();
    private final List<ColetaServidor> listaAlertas = new ArrayList<>();

    public CsvUtils(ConfiguracaoServidorDAO configuracaoDAO) {
        this.configuracaoDAO = configuracaoDAO;
    }

    public String removeZeroEsquerda(String s){
        if(s == null){
            return null;
        }
        return s.replaceAll("^0+", "");
    }

//    public Boolean trataNulos() {
//
//    }

    public


    public void leImportaArquivoCsvServidor (String nomeArq){
        Reader arq = null;
        BufferedReader entrada = null;
        nomeArq += ".csv";
        List<ColetaServidor> listaLidaServidor = new ArrayList<>();

        //Bloco try catch para abrir o arquivo
        try {
            arq = new InputStreamReader(new FileInputStream(nomeArq), "UTF-8");
            entrada = new BufferedReader(arq);
        } catch (IOException erro){
            System.out.println("Erro na abertura de arquivo");
            System.exit(1);
        }

        //try catch para ler o arquivo
        try {
            String[] registro;
            String linha = entrada.readLine();

            // Pula a primeira linha (cabeçalho)
            registro = linha.split(";");

            String[] cabecalho = registro;
            System.out.printf("%s %-19s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s\n",
                    registro[0], registro[1], registro[2], registro[3], registro[4], registro[5], registro[6], registro[7], registro[8], registro[9],
                    registro[10], registro[11], registro[12], registro[13], registro[14], registro[15], registro[16], registro[17], registro[18], registro[19],
                    registro[20], registro[21], registro[22], registro[23], registro[24], registro[25]);

            linha = entrada.readLine();
            while (linha != null){
                ColetaServidor linhaObj = new ColetaServidor();
                registro = linha.split(";");

                //For para tratar e retirar 0 à esquerda
                for (int i = 0; i < registro.length; i++) {
                    registro[i] = removeZeroEsquerda(registro[i]);
                }

                // 1. Extração dos dados (26 campos)
                linhaObj.setMacAddress(registro[0]);
                linhaObj.setTimestamp(registro[1]);
                linhaObj.setCpu_porcentagem(Double.valueOf(registro[2]));
                linhaObj.setCpuOciosa(Double.valueOf(registro[3]));
                linhaObj.setCpuUsuario(Double.valueOf(registro[4]));
                linhaObj.setCpuSistema(Double.valueOf(registro[5]));
                linhaObj.setCpuLoadAvg(registro[6]);
                linhaObj.setRam(Double.valueOf(registro[7]));
                linhaObj.setRamMb(Double.valueOf(registro[8]));
                linhaObj.setRamGb(Double.valueOf(registro[9]));
                linhaObj.setRamDisponivel(Double.valueOf(registro[10]));
                linhaObj.setRamDisponivelMb(Double.valueOf(registro[11]));
                linhaObj.setRamDisponivelGb(Double.valueOf(registro[12]));
                linhaObj.setRamSwap(Double.valueOf(registro[13]));
                linhaObj.setRamSwapMb(Double.valueOf(registro[14]));
                linhaObj.setRamGb(Double.valueOf(registro[15]));
                linhaObj.setDisco(Double.valueOf(registro[16]));
                linhaObj.setDiscoMb(Double.valueOf(registro[17]));
                linhaObj.setDiscoGb(Double.valueOf(registro[18]));
                linhaObj.setDiscoDisponivel(Double.valueOf(registro[19]));
                linhaObj.setDiscoDisponivelMb(Double.valueOf(registro[20]));
                linhaObj.setDiscoDisponivelGb(Double.valueOf(registro[21]));
                linhaObj.setDiscoThroughputMbs(Double.valueOf(registro[22]));
                linhaObj.setDiscoThroughputGbs(Double.valueOf(registro[23]));
                linhaObj.setMbEnviados(Double.valueOf(registro[24]));
                linhaObj.setMbRecebidos(Double.valueOf(registro[25]));


                Servidor servidorAchado =
                        this.configuracaoDAO.buscarServidorPorMac(
                                linhaObj.getMacAddress()
                        );

                List<ConfiguracaoServidor> listaConfigs = new ArrayList<>();
                Layout layoutAchado = null;
                // ESSE IF SE O SERVIDOR NAO TIVER LAYOUT DE PREFERENCIA
                if(servidorAchado.getFk_layout() == null) {
                    //PRIMEIRO ACHA LAYOUT
                    layoutAchado =
                            this.configuracaoDAO.buscarLayoutPorFkEmpresa(
                                    servidorAchado.getFk_empresa_servidor()
                            );
                }
                else { //CASO TENHA LAYOUT DE PREFERENCIA O SERVIDOR
                    layoutAchado =  this.configuracaoDAO.buscarLayoutPorFkLayout(servidorAchado.getFk_layout());
                }

                //DEPOIS ENTRA NAS CONFIGURACOES DO LAYOUT E PEGA COMPONENTE E METRICA E AS FAIXAS DE ALERTA (LISTA)
                listaConfigs = this.configuracaoDAO.buscarConfiguracaoLayout(layoutAchado.getId());

                String nomeComponente;
                String guardaMetrica = "";
                Boolean linhaEstourada = false;

                //AREA PARA CHAMADO JIRA
                String tituloAlerta = "";
                String prioridadeAlerta = "";
                String descricaoAlerta = "";

                //MENSAGEM QUE SERA EXIBIDA NO CONSOLE
                String mensagemAlerta = "";
                for(int i=0;i<cabecalho.length;i++) {
                    nomeComponente = "";
                    String[] cabecalhoSeparado = cabecalho[i].toLowerCase().split("_");
                    if(cabecalhoSeparado.length > 1) {
                        for(int j=0;j<cabecalhoSeparado.length-1;j++) {
                            if(nomeComponente != "") {
                                nomeComponente+="_"+cabecalhoSeparado[j];
                            } else {
                                nomeComponente+=cabecalhoSeparado[j];
                            }
                        }
                    }
                    for(ConfiguracaoServidor linhaConfig : listaConfigs) {
                        if(nomeComponente.equalsIgnoreCase(linhaConfig.getNome_componente())) {
                            if(linhaConfig.getNome_metrica().equals("%")) {
                                guardaMetrica = linhaConfig.getNome_metrica();
                                linhaConfig.setNome_metrica("porcentagem");
                            }
                            if(cabecalhoSeparado[cabecalhoSeparado.length-1].equalsIgnoreCase(linhaConfig.getNome_metrica())) {
                                if(Double.parseDouble(registro[i]) > Double.parseDouble(linhaConfig.getAlertaLeve())) {
                                    prioridadeAlerta = "Medium";
                                    linhaEstourada = true;
                                    tituloAlerta = "ALERTA DISPARADO NO SERVIDOR "+servidorAchado.getApelido();
                                    descricaoAlerta = linhaConfig.getNome_componente()+ " ESTÁ NA FAIXA DE " + registro[i]+guardaMetrica+ ", demorar para resolver pode resultar em sérios problemas à partida e tender a crashar o jogo!";
                                    mensagemAlerta = registro[1] + tituloAlerta +" | MENSAGEM = "+descricaoAlerta;
                                }
                                if(Double.parseDouble(registro[i]) > Double.parseDouble(linhaConfig.getAlertaGrave())) {
                                    prioridadeAlerta = "Highest";
                                }
                            }
                        }
                    }
                }
                if(!mensagemAlerta.equals("")) {
                    dadosEmAlertaServidor.add(mensagemAlerta);
                    listaAlertas.add(linhaObj);

                    //CRIA CHAMADO NO JIRA
                    String dataHoje = LocalDate.now().toString();
                    JiraInteraction.criarTicket(tituloAlerta, descricaoAlerta, prioridadeAlerta, dataHoje);

                }

                linha = entrada.readLine();
            }
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException erro) { // Incluído AIOOBE para caso o CSV não tenha 26 colunas
            System.out.println("Erro ao ler ou processar dados do arquivo (Verifique se o CSV tem 26 colunas).");
            erro.printStackTrace();
        } finally {
            try{
                if (entrada != null) entrada.close();
                if (arq != null) arq.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar o arquivo.");
            }
        }
        gravarArquivoCsv(listaAlertas, "alertas_capturados");

        // FINALIZAÇÃO: Mostra a lista de alertas
        System.out.println("\n=================================================");
        System.out.println("LISTA FINAL DE ALERTA DO SERVIDOR GERADA:");
        System.out.println("=================================================");
        if (dadosEmAlertaServidor.isEmpty()) {
            System.out.println("Nenhum alerta grave detectado nos dados lidos.");
        } else {
            for (String linha: dadosEmAlertaServidor){
                System.out.println(linha);
            }
            System.out.println("TOTAL DE ALERTAS: "+ dadosEmAlertaServidor.size());
        }
    }

    public void gravarArquivoCsv(List<ColetaServidor> listaAlertas, String nomeArquivo) {
        OutputStreamWriter saida = null;

        Boolean deuRuim = false;
        nomeArquivo +=".csv";

        try {

            saida = new OutputStreamWriter(new FileOutputStream(nomeArquivo),
                    StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.out.println("Erro ao abrir o arquivo");
            System.exit(1);
        }
        System.out.println(saida);
        try {

            saida.append("macadress;timestamp;cpu_porcentagem;cpu_ociosa_porcentagem;cpu_usuarios_porcentagem;cpu_sistema_porcentagem;cpu_loadavg;ram_porcentagem;ram_mb;ram_gb;ram_disp_porcentagem;ram_disp_mb;ram_disp_gb;ram_swap_porcentagem;ram_swap_mb;ram_swap_gb;disco_porcentagem;disco_mb;disco_gb;disco_livre_porcentagem;disco_livre_mb;disco_livre_gb;disco_throughput_mbs;disco_throughput_gbs;rede_enviados_mb_;rede_recebidos_mb\n"); //primeira linha do csv
            for(ColetaServidor linhaColeta : listaAlertas) {
                //String.format para uma string formata, nao printada como printf
                saida.write(String.format("%s;%s;%.2f;%.2f;%.2f;%.2f;%s;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f;%.2f\n",
                        linhaColeta.getMacAddress(), linhaColeta.getTimestamp(), linhaColeta.getCpu_porcentagem(), linhaColeta.getCpuOciosa(), linhaColeta.getCpuUsuario(), linhaColeta.getCpuSistema(), linhaColeta.getCpuLoadAvg(), linhaColeta.getRam(), linhaColeta.getRamMb(), linhaColeta.getRamGb(),
                        linhaColeta.getRamDisponivel() , linhaColeta.getRamDisponivelMb(), linhaColeta.getRamDisponivelGb(), linhaColeta.getRamSwap(), linhaColeta.getRamSwapMb(), linhaColeta.getRamSwapGb(), linhaColeta.getDisco(), linhaColeta.getDiscoMb(), linhaColeta.getDiscoGb(), linhaColeta.getDiscoDisponivel(),
                        linhaColeta.getDiscoDisponivelMb(),linhaColeta.getDiscoDisponivelGb(),linhaColeta.getDiscoThroughputMbs(), linhaColeta.getDiscoThroughputGbs(), linhaColeta.getMbEnviados() ,linhaColeta.getMbRecebidos()));
            }

        } catch (IOException e) {
            System.out.println("Erro ao gravar no arquivo");
            e.printStackTrace();
            deuRuim = true;
        } finally {
            try {
                saida.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if(deuRuim) {
                System.exit(1);
            }
        }

    }




}