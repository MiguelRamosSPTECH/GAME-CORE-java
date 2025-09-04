package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Logs> registros = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        Logs log1 = new Logs("Servidor Lol");
        log1.aumentarCpu(40.0);
        log1.aumentarRam(30.0);
        log1.aumentarDisco(20.0);
        log1.diminuirCpu(5.0);
        log1.diminuirRam(11.0);
        log1.diminuirDisco(2.0);
        registros.add(log1);

        Logs log2 = new Logs("Servidor Fortnite");
        log2.aumentarCpu(75.0);
        log2.aumentarRam(60.0);
        log2.aumentarDisco(90.0);
        log2.diminuirCpu(5.0);
        log2.diminuirRam(11.0);
        log2.diminuirDisco(2.0);
        registros.add(log2);

        Logs log3 = new Logs("Servidor Minecraft");
        log3.aumentarCpu(55.0);
        log3.aumentarRam(80.0);
        log3.aumentarDisco(70.0);
        log3.diminuirCpu(5.0);
        log3.diminuirRam(11.0);
        log3.diminuirDisco(2.0);
        registros.add(log3);

        //Pergunta
        System.out.println("Deseja ordenar por:");
        System.out.println("1 - Maior CPU");
        System.out.println("2 - Maior RAM");
        System.out.println("3 - Maior Disco");
        System.out.print("Escolha: ");
        Integer escolha = sc.nextInt();

        // Ordenação
        for (int i = 0; i < registros.size() - 1; i++) {
            for (int j = 0; j < registros.size() - 1 - i; j++) {
                Boolean precisaTrocar = false;

                if (escolha == 1 && registros.get(j).getCpu() < registros.get(j + 1).getCpu()) {
                    precisaTrocar = true;
                } else if (escolha == 2 && registros.get(j).getRam() < registros.get(j + 1).getRam()) {
                    precisaTrocar = true;
                } else if (escolha == 3 && registros.get(j).getDisco() < registros.get(j + 1).getDisco()) {
                    precisaTrocar = true;
                }

                if (precisaTrocar) {
                    Logs aux = registros.get(j);
                    registros.set(j, registros.get(j + 1));
                    registros.set(j + 1, aux);
                }
            }
        }

        // Resultado
        System.out.println("\n--- Resultados ---");
        for (Logs l : registros) {
            System.out.println("Servidor: " + l.getNome()
                    + " | CPU: " + l.getCpu()
                    + " | RAM: " + l.getRam()
                    + " | Disco: " + l.getDisco()
                    + " | DataHora: " + l.getDataHora());
        }
    }
}
