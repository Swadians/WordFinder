/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.main;

import br.com.ufpel.wordfinder.base.Spark;
import br.com.ufpel.wordfinder.base.ArticleFile;
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

        // ------------------------------#LOG#----------------------------------
        PrintStream ps = new PrintStream(new File("Relatorio"));

        List<ArticleFile> files = buffer.keySet().stream().collect(Collectors.toList());
        Collections.sort(files);

        files.forEach(file -> {
            ps.println("Artigo: " + file.fileName);
            buffer.get(file)
                    .forEach((tupla) -> {
                        ps.println("Palavra " + tupla._1 + " numero de ocorrencias: " + tupla._2);
                    });
            ps.println();
        });

    }
}
