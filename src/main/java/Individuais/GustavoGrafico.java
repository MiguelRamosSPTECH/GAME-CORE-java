package Individuais;

import java.util.ArrayList;
import java.util.List;

public class GustavoGrafico {
    // Vai pegar os dados em csv
    // Um csv por dia, que vão ser juntandos

    // Cria uma matriz, com os vetores dos dias.

    // Tenho que pesquisar como enviar somente um vetor dos que está dentro,




    public static void MontarDadosGraficos(Double [] valoresCpu, Double [] valoresRam){
        // vai receber uma matriz com todos os valores de uso, da CPU e da RAM
        List<Double> mediasCpu = new ArrayList<>();
        mediasCpu.add(GustavoRegressao.calcularMedias(valoresCpu));

        List<Double> mediasRam = new ArrayList<>();
        mediasRam.add(GustavoRegressao.calcularMedias(valoresRam));


        // Agora salva em um novo CSV os valores de CPU e RAM até o dia atual --- falta

            // Partindo pra logica dos dados realmente futuros:


        List<Integer> dias = new ArrayList<>();

        for(Integer i = 0; i < valoresCpu.length; i++){
            dias.add(i+1);
        }

        // Convertendo os lists para vetor:

        Double[] diasVetor = dias.toArray(new Double[0]);
        Double[] mediasCpuVetor = mediasCpu.toArray(new Double[0]);
        Double[] mediasRamVetor = mediasRam.toArray(new Double[0]);


        Double [] indicesCpu = GustavoRegressao.calcularRegressao(mediasCpuVetor, diasVetor);
        Double [] indicesRam = GustavoRegressao.calcularRegressao(mediasRamVetor, diasVetor);

        // salva os valores de "a" e "b" da CPU e da RAM em um novo CSV --- falta
    }
}
