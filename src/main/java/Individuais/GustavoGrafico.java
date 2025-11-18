package Individuais;

import java.util.ArrayList;
import java.util.List;

public class GustavoGrafico {
    // Vai pegar os dados em csv
    // Um csv por dia, que vão ser juntandos

    // Cria uma matriz, com os vetores dos dias.

    // Tenho que pesquisar como enviar somente um vetor dos que está dentro,




    public static class MontarDadosGraficos(Double [] valoresCpu, Double [] valoresRam){
        // vai receber uma matriz com todos os valores de uso, da CPU e da RAM
        List<Double> mediasCpu = new ArrayList<>();

        for(int i = 0; i < valoresCpu.length; i++){
            mediasCpu.add(GustavoRegressao.calcularMedias(valoresCpu[i]));
        }

        List<Double> mediasRam = new ArrayList<>();
        for(int i = 0; i < valoresRam.length; i++){
            mediasRam.add(GustavoRegressao.calcularMedias(valoresRam[i]));
        }

        // Agora salva em um novo CSV os valores de CPU e RAM até o dia atual --- falta

            // Partindo pra logica dos dados realmente futuros:


        List<Integer> dias = new ArrayList<>();

        for(Integer i = 0; i < valoresCpu.length; i++){
            dias.add(i+1);
        }

        Double [] indicesCpu = GustavoRegressao.calcularRegressao(mediasCpu, dias);
        Double [] indicesRam = GustavoRegressao.calcularRegressao(mediasRam, dias);

        // salva os valores de "a" e "b" da CPU e da RAM em um novo CSV --- falta
    }
}
