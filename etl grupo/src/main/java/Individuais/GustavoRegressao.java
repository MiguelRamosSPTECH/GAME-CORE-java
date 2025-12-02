package Individuais;

public class GustavoRegressao {

    public static Double calcularMedias(Double[]medidas){
        Double soma = 0.0;
        for (int i = 0; i < medidas.length; i++) {
            soma += medidas[i];
        }
        return  soma / medidas.length;
    }

    public static Double[] calcularRegressao(Double[] x, Double[] y) {

        Double somaX = 0.0, somaY = 0.0, somaXY = 0.0, somaX2 = 0.0;

        for(Integer i = 0; i < x.length; i++) {
            somaX += x[i];
            somaY += y[i];
            somaXY += x[i] * y[i];
            somaX2 += x[i] * x[i];
        }

        Double b = (x.length * somaXY - somaX * somaY) / (x.length * somaX2 - (somaX * somaX));
        Double a = (somaY - b * somaX) / x.length;

        return new Double[]{a, b};  // retorna um "vetor" com o indice 0 sendo o 'a' e o 1 o 'b'
    }

    public static Double preverValor(Double a, Double b, Double xNovo) {
        return a + b * xNovo;
    }


    public static Double calcularR2(Double[] x, Double[] y, Double a, Double b) {


        Double mediaY = calcularMedias(y);

        Double rss = 0.0;
        Double tss = 0.0;

        for (int i = 0; i < y.length; i++) {
            Double yPred = a + b * x[i];   // pega o valor previsto em cada ponto

            Double erro = y[i] - yPred;     // pega a distancia da previsão e o ponto
            Double erroTot = y[i] - mediaY; // pega a distancia da previsão e a media

            rss += erro * erro;       // quadrado do erro residual
            tss += erroTot * erroTot; // quadrado do erro total
        }

        return 1 - (rss / tss);
    }
}
