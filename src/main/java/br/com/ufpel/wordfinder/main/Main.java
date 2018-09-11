/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.main;

import br.com.ufpel.wordfinder.base.Spark;
import br.com.ufpel.wordfinder.util.IoUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;
import java.util.stream.Collectors;
import scala.Tuple2;

/**
 *
 * @author Weslen
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        try (Spark spark = new Spark()) {
            PrintStream ps = new PrintStream(new File("Relatorio"));
            List<String> filesNames = IoUtil.getFilesNames("Entradas");
            List<String> words = IoUtil.getWordsOfFile("words.txt");

            filesNames.parallelStream().forEach(file -> {
                List<Tuple2<String, Integer>> local = spark.findPosOfWordsPDF(file, "networks", words, 50);

                ps.println("Artigo: " + file);
                local.forEach(linha -> ps.println("Palavra " + linha._1 + " Posicao: " + linha._2));
                local.stream()
                        .collect(Collectors.groupingBy(p -> p._1, Collectors.counting()))
                        .forEach((palavra, num) -> {
                            ps.println("Palavra " + palavra + " numero de ocorrencias: " + num);
                        });

            });
        }

    }
}
