/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Weslen
 */
public class IoUtil {

    public static List<String> getFilesNames(String directoryPath) {
        List<String> results = new ArrayList<>();

        File[] files = new File(directoryPath).listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getPath());
            }
        }
        return results;
    }

}
