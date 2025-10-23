import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CsvProcessor {

    private final ConfiguracaoServidorDAO configuracaoDAO;
    private final List<ColetaServidor> dadosEmAlertaServidor = new ArrayList<>();
    private final List<ColetaContainer> dadosEmAlertaContainer = new ArrayList<>();

    public CsvProcessor(ConfiguracaoServidorDAO configuracaoDAO) {
        this.configuracaoDAO = configuracaoDAO;
    }

    public String removeZeroEsquerda(String s){
        if(s == null){
            return null;
        }
        return s.replaceAll("^0+", "");
    }


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


            System.out.printf("%s %-19s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s\n",
                    registro[0], registro[1], registro[2], registro[3], registro[4], registro[5], registro[6], registro[7], registro[8], registro[9],
                    registro[10], registro[11], registro[12], registro[13], registro[14], registro[15], registro[16], registro[17], registro[18], registro[19],
                    registro[20], registro[21], registro[22], registro[23], registro[24], registro[25]);

            linha = entrada.readLine();
            while (linha != null){
                registro = linha.split(";");

                //For para tratar e retirar 0 à esquerda
                for (int i = 0; i < registro.length; i++) {
                    registro[i] = removeZeroEsquerda(registro[i]);
                }

                // 1. Extração dos dados (26 campos)
                String macAddress = registro[0];
                String timestamp = registro[1];
                Double cpu_porcentagem = Double.valueOf(registro[2]);
                Double cpuOciosa = Double.valueOf(registro[3]);
                Double cpuUsuario = Double.valueOf(registro[4]);
                Double cpuSistema = Double.valueOf(registro[5]);
                String cpuLoadAvg = registro[6];
                Double ram = Double.valueOf(registro[7]);
                Double ramMb = Double.valueOf(registro[8]);
                Double ramGb = Double.valueOf(registro[9]);
                Double ramDisponivel = Double.valueOf(registro[10]);
                Double ramDisponivelMb = Double.valueOf(registro[11]);
                Double ramDisponivelGb = Double.valueOf(registro[12]);
                Double ramSwap = Double.valueOf(registro[13]);
                Double ramSwapMb = Double.valueOf(registro[14]);
                Double ramSwapGb = Double.valueOf(registro[15]);
                Double disco = Double.valueOf(registro[16]);
                Double discoMb = Double.valueOf(registro[17]);
                Double discoGb = Double.valueOf(registro[18]);
                Double discoDisponivel = Double.valueOf(registro[19]);
                Double discoDisponivelMb = Double.valueOf(registro[20]);
                Double discoDisponivelGb = Double.valueOf(registro[21]);
                Double discoThroughputMbs = Double.valueOf(registro[22]);
                Double discoThroughputGbs = Double.valueOf(registro[23]);
                Double mbEnviados = Double.valueOf(registro[24]);
                Double mbRecebidos = Double.valueOf(registro[25]);


                // 2. BUSCA DO ALERTA E FILTRAGEM
                String nomeComponente = "CPU";
                String unidadeMedida = "porcentagem";

                ConfiguracaoServidor configAlerta =
                        this.configuracaoDAO.buscarConfiguracaoPorMac(
                                macAddress,
                                nomeComponente,
                                unidadeMedida
                        );

                // 3. Aplica o filtro
                if (configAlerta != null) {
                    try {
                        Double limiteGrave = Double.valueOf(configAlerta.getAlertaGrave());

                        if (cpu_porcentagem > limiteGrave) {
                            // ALERTA ACIONADO: Cria o objeto com os 26 argumentos
                            ColetaServidor dados = new ColetaServidor(
                                    macAddress, timestamp, cpu_porcentagem, cpuOciosa, cpuUsuario, cpuSistema, cpuLoadAvg,
                                    ram, ramMb, ramGb, ramDisponivel, ramDisponivelMb, ramDisponivelGb,
                                    ramSwap, ramSwapMb, ramSwapGb, disco, discoMb, discoGb, discoDisponivel, discoDisponivelMb, discoDisponivelGb,
                                    discoThroughputMbs, discoThroughputGbs,
                                    mbEnviados, mbRecebidos // <-- NOVOS ARGUMENTOS AQUI
                            );
                            dadosEmAlertaServidor.add(dados); // Adiciona na lista de ALERTA
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Erro: Limite de alerta grave ('" + configAlerta.getAlertaGrave() + "') não é um número válido.");
                    }
                }

                // Esta linha pode ser removida se você só quiser a lista de alertas
                // listaLidaServidor.add(dados);

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

        // FINALIZAÇÃO: Mostra a lista de alertas
        System.out.println("\n=================================================");
        System.out.println("LISTA FINAL DE ALERTA DO SERVIDOR GERADA:");
        System.out.println("=================================================");
        if (dadosEmAlertaServidor.isEmpty()) {
            System.out.println("Nenhum alerta grave detectado nos dados lidos.");
        } else {
            for (ColetaServidor c : dadosEmAlertaServidor){
                System.out.println(c);
            }
        }
    }

    public void leImportaArquivoCsvContainer (String nomeArq){
        Reader arq = null;
        BufferedReader entrada = null;
        nomeArq += ".csv";
        List<ColetaContainer> listaLidaContainer = new ArrayList<>();

        try {
            arq = new InputStreamReader(new FileInputStream(nomeArq), "UTF-8");
            entrada = new BufferedReader(arq);
        } catch (IOException erro){
            System.out.println("Erro na abertura de arquivo");
            System.exit(1);
        }

        try {
            String[] registro;
            String linha = entrada.readLine();
            registro = linha.split(";");
            // Se quiser imprimir o cabeçalho, mantenha a linha abaixo
            // System.out.printf("%s %12s %8s %8s %8s %8s %8s\n", registro[0], registro[1], registro[2], registro[3], registro[4], registro[5], registro[6]);

            linha = entrada.readLine();
            while (linha != null){

                // CORREÇÃO DE LÓGICA: Pega a linha atual antes de tratar os zeros
                registro = linha.split(";");

                // For para remover 0 a esquerda
                for (int i = 0; i < registro.length; i++) {
                    registro[i] = removeZeroEsquerda(registro[i]);
                }

                String identificacao_container = registro[0];
                String timestamp = registro[1];
                Double cpu_container = Double.valueOf(registro[2]);
                Double throughput_container = Double.valueOf(registro[3]);
                Double ram_container = Double.valueOf(registro[4]);
                Double throttled_time_container = Double.valueOf(registro[5]);
                Double tps_container = Double.valueOf(registro[6]);

                // A LÓGICA DE ALERTA PARA CONTAINER DEVE SER IMPLEMENTADA AQUI, usando o DAO e buscando a métrica correta.

                ColetaContainer dados = new ColetaContainer(identificacao_container,timestamp,cpu_container,throughput_container,ram_container,throttled_time_container,tps_container);
                listaLidaContainer.add(dados);

                linha = entrada.readLine();
            }
        } catch (IOException erro) {
            System.out.println("Erro ao ler o arquivo");
            erro.printStackTrace();
        } finally {
            try{
                if (entrada != null) entrada.close();
                if (arq != null) arq.close();
            } catch (IOException e) {
                System.out.println("erro ao fechar o arquivo");
            }
        }

        System.out.println("\nLista lida do arquivo: ");
        for (ColetaContainer c : listaLidaContainer){
            System.out.println(c);
        }
    }
}