////// gamecore.project.processors.DashboardProcessor.java
////
////package gamecore.project.processors;
////
////import gamecore.project.entity.ColetaServidor;
////import gamecore.project.entity.DashboardData;
////import gamecore.project.entity.TimeSeriesPoint;
////import gamecore.project.csvs.CsvUtils;
////import gamecore.project.mappers.Mapper;
////import gamecore.project.buckets.S3Interaction;
////
////import software.amazon.awssdk.services.s3.S3Client;
////import java.time.LocalDate;
////import java.util.ArrayList;
////import java.util.List;
////import java.io.IOException;
////
////public class DashboardProcessor {
////
////    private final CsvUtils csvUtils = new CsvUtils();
////    private final Mapper mapper = new Mapper();
////    private final S3Interaction s3Interaction = new S3Interaction();
////
////    private static final String CURATED_KEY_DASHBOARD = "dashboard_data.json";
////    private static final String CURATED_BUCKET = "s3-curated-lab-mrl";
////
////
////    public CsvUtils getCsvUtils() {
////        return csvUtils;
////    }
////
////    public Mapper getMapper() {
////        return mapper;
////    }
////
////    public S3Interaction getS3Interaction() {
////        return s3Interaction;
////    }
////
////
////
////    public DashboardData generateDashboardSummary(String localFilePath) throws Exception {
////
////        List<String[]> linhasFiltradas = csvUtils.readRawLinesFilterByDate(localFilePath);
////        List<ColetaServidor> coletasDoDia = mapper.mapToColetasServidor(linhasFiltradas);
////
////        DashboardData sumario = aggregateMetrics(coletasDoDia);
////
////
////        //esquece isso, acho q viajei pq pulei um lambda
////        // String jsonPayload = mapper.toJson(sumario);
////        // s3Interaction.uploadJsonToCurated(jsonPayload, CURATED_BUCKET, CURATED_KEY_DASHBOARD, s3Client);
////
////        // retorna o objeto ja processado
////        return sumario;
////    }
////
////    /**
////     * MÉTODO CENTRAL: Calcula todos os resumos e séries temporais em um único loop.
////     */
////    private DashboardData aggregateMetrics(List<ColetaServidor> coletas) {
////
////        if (coletas.isEmpty()) {
////            return new DashboardData();
////        }
////
////        DashboardData sumario = new DashboardData();
////        int maxProcessos = 0;
////
////        List<TimeSeriesPoint> swapSeries = new ArrayList<>();
////        List<TimeSeriesPoint> desempenhoSeries = new ArrayList<>();
////        List<TimeSeriesPoint> esperaSeries = new ArrayList<>();
////        List<TimeSeriesPoint> cpuSystemSeries = new ArrayList<>();
////        List<TimeSeriesPoint> cpuUserSeries = new ArrayList<>();
////
////
////        for (ColetaServidor coleta : coletas) {
////
////            // 1. MÉTODOS DE SUMÁRIO (Máximo de Processos)
////            maxProcessos = Math.max(maxProcessos, coleta.getTotalProcessos());
////
////            // 2. SÉRIE DE TAXA DE SWAP (Linha)
////            swapSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), coleta.getRamSwapPorcentagem()));
////
////            // 3. SÉRIE DE CPU (Sistema vs Usuário)
////            cpuSystemSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), coleta.getCpuSistemaPorcentagem()));
////            cpuUserSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), coleta.getCpuUsuariosPorcentagem()));
////
////            // 4. CÁLCULO DO ÍNDICE DE DESEMPENHO (Performance vs Espera)
////            double cpuUso = coleta.getCpuPorcentagem();
////            double cpuOciosa = coleta.getCpuOciosaPorcentagem();
////            double swap = coleta.getRamSwapPorcentagem();
////
////            double denominador = cpuOciosa + swap;
////            double indiceDesempenho;
////
////            if (denominador <= 0.001) {
////                indiceDesempenho = 1000.0; // Valor seguro para evitar divisão por zero
////            } else {
////                indiceDesempenho = cpuUso / denominador;
////            }
////
////            // Adiciona as duas linhas do gráfico Desempenho vs Tempo de Espera
////            desempenhoSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), indiceDesempenho));
////            esperaSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), cpuOciosa + swap));
////        }
////
////        // 5. FINALIZAÇÃO E ATRIBUIÇÃO
////        ColetaServidor ultimaColeta = coletas.get(coletas.size() - 1);
////        sumario.setRamUtilizadaPercent(ultimaColeta.getRamPorcentagem());
////        sumario.setRamDisponivelPercent(100.0 - ultimaColeta.getRamPorcentagem());
////
////        sumario.setDataReferencia(LocalDate.now().toString());
////        sumario.setTotalProcessos(maxProcessos);
////
////        sumario.setSwapTimeSeries(swapSeries);
////        sumario.setDesempenhoTimeSeries(desempenhoSeries);
////        sumario.setEsperaTimeSeries(esperaSeries);
////        sumario.setCpuSystemTimeSeries(cpuSystemSeries);
////        sumario.setCpuUserTimeSeries(cpuUserSeries);
////
////        return sumario;
////    }
////}
//
//// gamecore.project.processors.DashboardProcessor.java (CORRIGIDO: APENAS T)
//
//package gamecore.project.processors;
//
//import gamecore.project.entity.ColetaServidor;
//import gamecore.project.entity.DashboardData;
//import gamecore.project.entity.TimeSeriesPoint;
//import gamecore.project.csvs.CsvUtils;
//import gamecore.project.mappers.Mapper;
//import gamecore.project.buckets.S3Interaction;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.IOException;
//
//public class DashboardProcessor {
//
//    private final CsvUtils csvUtils = new CsvUtils();
//    private final Mapper mapper = new Mapper();
//    // private final S3Interaction s3Interaction = new S3Interaction(); // Nao precisa mais do S3Interaction aqui
//
//    //quando tiver o s3 ele vai receber
//    public DashboardData generateDashboardSummary(String localFilePath) throws Exception {
//
//        // ETAPA E: Filtragem e Mapeamento
//        List<String[]> linhasFiltradas = csvUtils.readRawLinesFilterByDate(localFilePath);
//        List<ColetaServidor> coletasDoDia = mapper.mapToColetasServidor(linhasFiltradas);
//
//        // ETAPA T: Agregação e Cálculo
//        DashboardData sumario = aggregateMetrics(coletasDoDia);
//
//
//
//        return sumario;
//    }
//
//
//    //calcula os resumos e series temporais (os graficos q sao plotados com tempos difentes)
//    private DashboardData aggregateMetrics(List<ColetaServidor> coletas) {
//
//        if (coletas.isEmpty()) {
//            return new DashboardData();
//        }
//
//        DashboardData sumario = new DashboardData();
//        int maxProcessos = 0;
//
//        List<TimeSeriesPoint> swapSeries = new ArrayList<>();
//        List<TimeSeriesPoint> desempenhoSeries = new ArrayList<>();
//        List<TimeSeriesPoint> esperaSeries = new ArrayList<>();
//        List<TimeSeriesPoint> cpuSystemSeries = new ArrayList<>();
//        List<TimeSeriesPoint> cpuUserSeries = new ArrayList<>();
//
//
//        for (ColetaServidor coleta : coletas) {
//
//            // 1. MÉTODOS DE SUMÁRIO (Máximo de Processos - KPI)
//            maxProcessos = Math.max(maxProcessos, coleta.getTotalProcessos());
//
//            // 2. SÉRIE DE TAXA DE SWAP (Gráfico)
//            swapSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), coleta.getRamSwapPorcentagem()));
//
//            // 3. SÉRIE DE CPU (Sistema vs Usuário - Gráfico)
//            cpuSystemSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), coleta.getCpuSistemaPorcentagem()));
//            cpuUserSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), coleta.getCpuUsuariosPorcentagem()));
//
//            // 4. CÁLCULO DO ÍNDICE DE DESEMPENHO (Performance vs Espera - Gráfico)
//            double cpuUso = coleta.getCpuPorcentagem();
//            double cpuOciosa = coleta.getCpuOciosaPorcentagem();
//            double swap = coleta.getRamSwapPorcentagem();
//
//            // Desempenho = CPU Utilizada / (CPU Ociosa + Swap)
//            double denominador = cpuOciosa + swap;
//            double indiceDesempenho;
//
//            if (denominador <= 0.001) {
//                indiceDesempenho = 1000.0; // Evita divisão por zero
//            } else {
//                indiceDesempenho = cpuUso / denominador;
//            }
//
//            // Adiciona as duas linhas do gráfico
//            desempenhoSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), indiceDesempenho));
//            esperaSeries.add(new TimeSeriesPoint(coleta.getTimestamp(), cpuOciosa + swap));
//        }
//
//        // 5. FINALIZAÇÃO E ATRIBUIÇÃO DOS KPIs E RAM
//        ColetaServidor ultimaColeta = coletas.get(coletas.size() - 1);
//
//        sumario.setRamUtilizadaPercent(ultimaColeta.getRamPorcentagem());
//        sumario.setRamDisponivelPercent(100.0 - ultimaColeta.getRamPorcentagem());
//        sumario.setDataReferencia(LocalDate.now().toString());
//        sumario.setTotalProcessos(maxProcessos);
//
//        sumario.setSwapTimeSeries(swapSeries);
//        sumario.setDesempenhoTimeSeries(desempenhoSeries);
//        sumario.setEsperaTimeSeries(esperaSeries);
//        sumario.setCpuSystemTimeSeries(cpuSystemSeries);
//        sumario.setCpuUserTimeSeries(cpuUserSeries);
//
//        return sumario;
//    }
//}