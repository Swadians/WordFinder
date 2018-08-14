/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.main;

import br.com.ufpel.wordfinder.base.Spark;
import br.com.ufpel.wordfinder.util.IoUtil;
import java.util.List;
import scala.Tuple2;

/**
 *
 * @author Weslen
 */
public class Main {

    public static void main(String[] args) {
        try (Spark spark = new Spark()) {
            List<String> filesNames = IoUtil.getFilesNames("Entradas");
            filesNames.parallelStream().forEach(file -> {
                List<Tuple2<String, Integer>> local = spark.findWordsPDF(file, "networks", "momentum", 50);

                System.out.println("Artigo: " + file);
                local.forEach(linha -> System.out.println("Posição: " + linha._2));
                System.out.println("Numero total de occorencias: " + local.size());
            });
        }

    }
}
