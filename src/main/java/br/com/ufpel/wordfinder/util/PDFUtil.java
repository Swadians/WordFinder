/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.util;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.spark.input.PortableDataStream;
import scala.Tuple2;

/**
 *
 * @author Weslen
 */
public class PDFUtil {

    public static String ParseToRawText(Tuple2<String, PortableDataStream> data) {
        String rawText = "";

        try (PDDocument pdf = PDDocument.load(data._2.open())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();

            rawText = pdfStripper.getText(pdf);

        } catch (IOException | RuntimeException ex) {
            Logger.getLogger(PDFUtil.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error on open file: " + data._1);
            System.out.println("error: " + ex);
        }

        return rawText;
    }

    public static String ParseToRawTextItext(Tuple2<String, PortableDataStream> data) {
        StringBuilder rawText = new StringBuilder();

        try {
            PdfReader reader = new PdfReader(data._2.open());

            int numberOfPages = reader.getNumberOfPages();
            for (int page = 1; page <= numberOfPages; page++) {
                rawText.append(PdfTextExtractor.getTextFromPage(reader, page).getBytes("UTF-8"));
            }

        } catch (IOException | RuntimeException ex) {
            Logger.getLogger(PDFUtil.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("error on open file: " + data._1);
            System.out.println("error: " + ex);
        }

        return rawText.toString();
    }
}
