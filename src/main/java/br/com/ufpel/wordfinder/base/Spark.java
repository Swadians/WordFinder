/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.ufpel.wordfinder.base;

import br.com.ufpel.wordfinder.util.PDFUtil;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.col;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

/**
 *
 * @author Weslen
 */
public class Spark implements Closeable {

    private final JavaSparkContext sparkContext;
    private final SparkSession sparkSqlContext;

    public Spark() {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Spark Master");
        conf.set("spark.driver.maxResultSize", "6g");
        conf.set("spark.driver.allowMultipleContexts", "true");
        conf.set("spark.network.timeout", "7000s");
        conf.set("spark.executor.heartbeatInterval", "6000s");

        this.sparkSqlContext = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config(conf)
                .getOrCreate();

        this.sparkContext = new JavaSparkContext(conf);
    }

    public List<String> findLineByWordText(String word, String fileName) {
        // Creates a DataFrame having a single column named "line"
        JavaRDD<String> textFile = this.sparkContext.textFile(fileName);
        JavaRDD<Row> rowRDD = textFile.map(RowFactory::create);
        List<StructField> fields = Arrays.asList(
                DataTypes.createStructField("line", DataTypes.StringType, true));
        StructType schema = DataTypes.createStructType(fields);

        Dataset<Row> df = this.sparkSqlContext.createDataFrame(rowRDD, schema);

        Dataset<Row> lines = df.filter(col("line").like("%" + word + "%"));
        // Counts all the occurrences
        return lines.as(Encoders.STRING()).collectAsList();
    }

    public List<String> findLineByWordPDF(String word, String fileName) {
        JavaPairRDD<String, PortableDataStream> rawFile = this.sparkContext.binaryFiles(fileName);

        JavaRDD<String> textFile = rawFile.map(PDFUtil::ParseToRawText);
        JavaRDD<Row> rowRDD = textFile.map(RowFactory::create);
        List<StructField> fields = Arrays.asList(
                DataTypes.createStructField("line", DataTypes.StringType, true));
        StructType schema = DataTypes.createStructType(fields);

        Dataset<Row> df = this.sparkSqlContext.createDataFrame(rowRDD, schema);

        Dataset<Row> lines = df.filter(col("line").like("%" + word + "%"));
        // Counts all the occurrences
        return lines.as(Encoders.STRING()).collectAsList();

    }

    public List<Tuple2<String, Integer>> findWords(String fileName, String wordA, String wordB, int maxDistance) {
        // Creates a DataFrame having a single column named "line"
        JavaRDD<Row> rowRDD = this.sparkContext.textFile(fileName)
                .flatMap(s -> Arrays.asList(s.split(" ")).iterator())
                .map(RowFactory::create);

        List<StructField> fields = Arrays.asList(
                DataTypes.createStructField("line", DataTypes.StringType, true));
        StructType schema = DataTypes.createStructType(fields);

        Dataset<Row> df = this.sparkSqlContext.createDataFrame(rowRDD, schema);

        List<String> wordList = df.as(Encoders.STRING()).collectAsList();

        List<Tuple2<String, Integer>> filtredWords = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).contains(wordA)) {
                for (int j = 1; ((j <= maxDistance) && (j + i < wordList.size())); j++) {
                    if (wordList.get(i + j).contains(wordB)) {
                        filtredWords.add(new Tuple2<>(wordList.get(i + j), i + j));
                    }
                }
            }
        }

        return filtredWords;
    }

    public List<Tuple2<String, Integer>> findWordsPDF(String fileName, String wordA, String wordB, int maxDistance) {
        // Creates a DataFrame having a single column named "line"
        JavaRDD<Row> rowRDD = this.sparkContext.binaryFiles(fileName)
                .map(PDFUtil::ParseToRawText)
                .flatMap(s -> Arrays.asList(s.split(" ")).iterator())
                .map(RowFactory::create);

        List<StructField> fields = Arrays.asList(
                DataTypes.createStructField("line", DataTypes.StringType, true));
        StructType schema = DataTypes.createStructType(fields);

        Dataset<Row> df = this.sparkSqlContext.createDataFrame(rowRDD, schema);

        List<String> wordList = df.as(Encoders.STRING()).collectAsList();

        List<Tuple2<String, Integer>> filtredWords = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).contains(wordA)) {
                for (int j = 1; ((j <= maxDistance) && (j + i < wordList.size())); j++) {
                    if (wordList.get(i + j).contains(wordB)) {
                        filtredWords.add(new Tuple2<>(wordList.get(i + j), i + j));
                    }
                }
            }
        }

        return filtredWords;
    }

    @Override
    public void close() {
        this.sparkContext.close();
        this.sparkSqlContext.close();
    }
}
