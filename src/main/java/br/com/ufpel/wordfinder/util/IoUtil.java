/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.util;

import br.com.ufpel.wordfinder.base.ArticleFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Weslen
 */
public class IoUtil {

    public static List<ArticleFile> getFilesNames(String directoryPath) {
        List<ArticleFile> results = new ArrayList<>();

        File[] files = new File(directoryPath).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile()) {
                results.add(new ArticleFile(file.getPath(), directoryPath));
            } else {
                results.addAll(
                        IoUtil.getFilesNames(file.getPath())
                );
            }
        }
        return results;
    }

    public static List<String> getWordsOfFile(String directoryPath) {
        StringBuilder buffer = new StringBuilder();
        try {
            Scanner sc = new Scanner(new File(directoryPath), "UTF-8");

            while (sc.hasNext()) {
                buffer.append(sc.nextLine())
                        .append("\n");
            }

            String[] palavras = buffer.toString().split("\n");

            return Arrays.asList(palavras);
        } catch (IOException ex) {
            System.out.println("Erro ao ler o arquivo: " + ex);
            throw new RuntimeException(ex);
        }
    }

}
