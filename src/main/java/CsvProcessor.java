import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CsvProcessor {
    public void leExibeArquivoCsv(String nomeArq) {
        FileReader arq = null;      // arq corresponde ao arquivo
        Scanner entrada = null;     // entrada eh o objeto usado para ler do arquivo
        Boolean deuRuim = false;
        nomeArq += ".csv";

        // Bloco try-catch para abrir o arquivo
        try {
            arq = new FileReader(nomeArq);  // Abre o arquivo para leitura
            // Instancia a classe Scanner e especifica que deve usar delimitador ; ou \n
            entrada = new Scanner(arq).useDelimiter(";|\\n");
        }
        catch (FileNotFoundException erro) {
            System.out.println("Arquivo inexistente!");
            System.exit(1);
        }

        // Bloco try-catch para ler e fechar o arquivo
        try {
            Boolean cabecalho = true;
            // Enquanto nao chegou ao final do arquivo
            while (entrada.hasNext()) {
                if (cabecalho) {  // se for o cabecalho, le os titulos
                    String titulo1 = entrada.next();
                    String titulo2 = entrada.next();
                    String titulo3 = entrada.next();
                    String titulo4 = entrada.next();
                    // Printa os titulos em colunas na console
                    System.out.printf("%4s %-12s %-9s %4s\n", titulo1,
                            titulo2, titulo3, titulo4);
                    cabecalho = false;  // seta que a partir dai nao eh mais cabecalho
                }
                else {
                    Integer id = entrada.nextInt();
                    String nome = entrada.next();   // NAO USAR nextLine(), pois ai leria ate o final da linha
                    String porte = entrada.next();
                    Double peso = entrada.nextDouble();
                    // Printa os valores dos atributos em colunas na console
                    // Tamanho das colunas aqui deve ser igual ao tamanho das colunas dos titulos
                    System.out.printf("%04d %-12.12s %-9S %4.1f\n",
                            id, nome, porte, peso);
                }
            }
        }
        catch (NoSuchElementException erro) {
            System.out.println("Arquivo com problemas!");
            erro.printStackTrace();
            deuRuim = true;
        }
        catch (IllegalStateException erro) {
            System.out.println("Erro na leitura do arquivo!");
            erro.printStackTrace();
            deuRuim = true;
        }
        finally {
            try {
                entrada.close();
                arq.close();
            }
            catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }


    }

    public void leImportaArquivoCsvServidor (String nomeArq){
        Reader arq = null;
        BufferedReader entrada = null;
        nomeArq += ".csv";
        List<Coleta> listaLida = new ArrayList<>();

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
            registro = linha.split(";");
            System.out.printf("%s %-19s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s\n", registro[0], registro[1], registro[2], registro[3], registro[4], registro[5], registro[6], registro[7], registro[8], registro[9], registro[10], registro[11], registro[12], registro[13], registro[14], registro[15], registro[16], registro[17], registro[18], registro[19],registro[20], registro[21], registro[22], registro[23]);
            linha = entrada.readLine();
            while (linha != null){
                registro = linha.split(";");
                String user = registro[0];
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

                Coleta dados = new Coleta(user,timestamp,cpu_porcentagem,cpuOciosa,cpuUsuario,cpuSistema, cpuLoadAvg,ram,ramMb,ramGb,ramDisponivel,ramDisponivelMb,ramDisponivelGb,
                        ramSwap,ramSwapMb,ramSwapGb,disco,discoMb, discoGb,discoDisponivel,discoDisponivelMb,discoDisponivelGb,discoThroughputMbs,discoThroughputGbs);
                listaLida.add(dados);
                linha = entrada.readLine();
            }
        } catch (IOException erro) {
            System.out.println("Erro ao ler o arquivo");
            erro.printStackTrace();
        } finally {
            try{
                entrada.close();
                arq.close();
            } catch (IOException e) {
                System.out.println("erro ao fechar o arquivo");
            }
        }

        System.out.println("\nLista lida do arquivo: ");
        for (Coleta c : listaLida){
            System.out.println(c);
        }
    }

    public void leImportaArquivoCsvContainer (String nomeArq){
        Reader arq = null;
        BufferedReader entrada = null;
        nomeArq += ".csv";
        List<Coleta> listaLida = new ArrayList<>();

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
            registro = linha.split(";");
            System.out.printf("%s %-19s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s\n", registro[0], registro[1], registro[2], registro[3], registro[4], registro[5], registro[6], registro[7], registro[8], registro[9], registro[10], registro[11], registro[12], registro[13], registro[14], registro[15], registro[16], registro[17], registro[18], registro[19],registro[20], registro[21], registro[22], registro[23]);
            linha = entrada.readLine();
            while (linha != null){
                registro = linha.split(";");
                String servidor = registro[0];
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

                Coleta dados = new Coleta(servidor,timestamp,cpu_porcentagem,cpuOciosa,cpuUsuario,cpuSistema, cpuLoadAvg,ram,ramMb,ramGb,ramDisponivel,ramDisponivelMb,ramDisponivelGb,
                        ramSwap,ramSwapMb,ramSwapGb,disco,discoMb, discoGb,discoDisponivel,discoDisponivelMb,discoDisponivelGb,discoThroughputMbs,discoThroughputGbs);
                listaLida.add(dados);
                linha = entrada.readLine();
            }
        } catch (IOException erro) {
            System.out.println("Erro ao ler o arquivo");
            erro.printStackTrace();
        } finally {
            try{
                entrada.close();
                arq.close();
            } catch (IOException e) {
                System.out.println("erro ao fechar o arquivo");
            }
        }

        System.out.println("\nLista lida do arquivo: ");
        for (Coleta c : listaLida){
            System.out.println(c);
        }
    }
}
