package Individuais;

public class GustavoDesvio {

    public static Integer [] calcularFaixa(Double [] valores){
        Double media = GustavoRegressao.calcularMedias(valores);

        Double valorAtual = 0.0;
        for (int i = 0; i < valores.length; i++) {
            valorAtual +=  Math.pow((valores[i] - media),2);
        }
        valorAtual = valorAtual /(valores.length - 1);

        valorAtual = Math.pow(valorAtual,1/3);

        Integer min = 0, max = 9;

        min = (int) (media - valorAtual);
        max = (int) (media + valorAtual);

        return new Integer [] {min, max};
    }
}
