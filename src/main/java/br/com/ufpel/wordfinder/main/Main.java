/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.main;

import br.com.ufpel.wordfinder.base.ArticleFile;
import br.com.ufpel.wordfinder.base.Spark;
import br.com.ufpel.wordfinder.util.IoUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import scala.Tuple2;

/**
 *
 * @author Weslen
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Map<ArticleFile, List<Tuple2<String, Integer>>> buffer = Collections.synchronizedMap(new HashMap<>());

        try (Spark spark = new Spark()) {

            List<ArticleFile> filesNames = IoUtil.getFilesNames("Entradas");
            List<String> words = IoUtil.getWordsOfFile("words.txt");

            filesNames.parallelStream().forEach(file -> {
                List<Tuple2<String, Integer>> local = spark.findNumberOfWordOccurrenceInPDF(file.fileName, words);
                if (local.size() > 0) {
                    buffer.put(file, local);
                }
            });
        }

        //-------------------------------#LOG#----------------------------------
        PrintStream ps = new PrintStream(new File("Relatorio"));

        Map<String, List<List<Tuple2<String, Integer>>>> articlesGroup = buffer.entrySet().stream().collect(Collectors.groupingBy(
                data -> data.getKey().folder, Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        System.out.println("");
        List<String> folders = articlesGroup.keySet().stream().collect(Collectors.toList());
        Collections.sort(folders);

        folders.forEach(folder -> {
            ps.println("Pasta: " + folder);

            Map<String, List<Tuple2<String, Integer>>> wordGroup = articlesGroup.get(folder).stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList())
                    .stream().collect(
                            Collectors.groupingBy(Tuple2::_1)
                    );

            wordGroup.keySet().forEach(key -> {

                int sum = wordGroup.get(key).stream().mapToInt(data -> data._2).sum();
                ps.println("Palavra " + key + " numero de ocorrencias: " + sum);
                ps.println("Palavra " + key + " citada em: " + wordGroup.get(key).size() + " artigos");

            });
        });
    }
}
