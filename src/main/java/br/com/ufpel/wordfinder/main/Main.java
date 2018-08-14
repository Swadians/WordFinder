/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.main;

import br.com.ufpel.wordfinder.base.Spark;
import java.util.List;

/**
 *
 * @author Weslen
 */
public class Main {

    public static void main(String[] args) {
        List<String> linhas;

        try (Spark spark = new Spark()) {
            linhas = spark.findLineByWordPDF("networks", "Entrada.pdf");
        }

        linhas.forEach(linha -> System.out.println(linha));
        System.out.println("###" + linhas.size());

    }
}
