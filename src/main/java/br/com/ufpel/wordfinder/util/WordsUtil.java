/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.util;

import java.util.ArrayList;
import java.util.List;
import scala.Tuple2;

/**
 *
 * @author WeslenSchiavon
 */
public class WordsUtil {

    public static long wordCount(List<String> words, String regex) {
        return words.parallelStream().filter(word -> word.matches(regex)).count();
    }

    public static List<Tuple2<String, Integer>> wordCount(List<String> words, List<String> wordsToFind) {
        List<Tuple2<String, Integer>> filtredWords = new ArrayList<>();
        for (String wordTd : wordsToFind) {
            int count = (int) words.parallelStream().filter(word -> word.contains(wordTd)).count();
            if (count > 0) {
                filtredWords.add(new Tuple2<>(wordTd, count));
            }
        }
        return filtredWords;
    }

    public static List<Tuple2<String, Integer>> getPosOfWords(List<String> words, String wordBase, List<String> wordsToFind, int maxDistance) {
        List<Tuple2<String, Integer>> filtredWords = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).contains(wordBase)) {
                for (int j = 1; ((j <= maxDistance) && (j + i < words.size())); j++) {
                    String currentWord = words.get(i + j);
                    boolean contaisWord = wordsToFind.stream()
                            .anyMatch(word -> currentWord.contains(word));
                    if (contaisWord) {
                        filtredWords.add(new Tuple2<>(words.get(i + j), i + j));
                    }
                }
            }
        }

        return filtredWords;
    }

    public static List<Tuple2<String, Integer>> getPosOfWords(List<String> words, String wordBase, String regex, int maxDistance) {
        List<Tuple2<String, Integer>> filtredWords = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).contains(wordBase)) {
                for (int j = 1; ((j <= maxDistance) && (j + i < words.size())); j++) {
                    String currentWord = words.get(i + j);

                    if (currentWord.matches(regex)) {
                        filtredWords.add(new Tuple2<>(words.get(i + j), i + j));
                    }
                }
            }
        }

        return filtredWords;
    }

}
