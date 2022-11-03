package edu.brown.cs32.examples.moshiExample;


import edu.brown.cs32.examples.moshiExample.csv.CsvParser;
import edu.brown.cs32.examples.moshiExample.csv.FactoryFailureException;
import edu.brown.cs32.examples.moshiExample.csv.attachments.CSVStringParser;
import org.junit.jupiter.api.Test;
import spark.utils.Assert;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCSVParser {

    final static int MAX_DIM = 100;

    final static int NUM_TRIALS = 1000;

    /**
     * Helper method to create random alpha-numeric strings of random length
     * @return a random string
     */
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 40) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
    /**
     * Helper method for fuzz testing CSV parsing. Instantiates CSV files of
     * random column and row count, considering empty and non-empty rows.
     * Row contents are randomly generated as well.
     * @param maxDim - maximum number of rows and columns the randomly generated CSV file will
     *              have
     * @return the randomly generated CSV file as a StringReader
     */
    public static String[] generateRandomCSVs(int maxDim) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Integer numRows = random.nextInt(1, maxDim);
            Integer numCols = random.nextInt(1, maxDim);
            int r = 0;
            List<String> csvStrings = new ArrayList<>();
            while (r < numRows) {
                boolean fullRow = true;
                if (fullRow) {
                    int c = 0;
                    List<String> newRow = new ArrayList<>();
                    while (c < numCols) {
                        String randomString = getSaltString();
                        String withoutCommas = randomString.replace(",", "");
                        if (c == numCols - 1) {
                            newRow.add(withoutCommas + "\n");
                        } else {
                            newRow.add(withoutCommas + ",");
                        }
                        c++; // randomly generate strings
                    }
                    csvStrings.add(String.join("", newRow));
                } else {
                    String commas = new String(new char[numCols - 1]).replace("\0", ",");
                    String withLineBreak = commas + "\n";
                    csvStrings.add(withLineBreak);
                }
                r++;
            }
            String finalCSV = String.join("", csvStrings);
            String[] output = new String[]{finalCSV, numRows.toString(), numCols.toString()};
            return output;
    }

    /**
     * The throws clause for this method is immaterial; JUnit will
     * fail the test if any exception is thrown unless it's marked
     * *expected* inside the @Test annotation or it's explicitly
     * part of an assertion.
     *
     * @throws IOException - a file not found exception
     * @throws FactoryFailureException - when a Factory class fails
     */
    @Test
    public void fuzzTestParse() throws IOException, FactoryFailureException {
        for(int counter=0;counter<NUM_TRIALS;counter++) {
            String[] output = generateRandomCSVs(MAX_DIM);
            StringReader csvString = new StringReader(output[0]);
            Integer numRows = Integer.valueOf(output[1]);
            Integer numCols = Integer.valueOf(output[2]);
            Reader csv = csvString;
            CsvParser testCSVParser = new CsvParser(csv, new CSVStringParser(), false);
            List<List<String>> parsedData = testCSVParser.parse();
            assertEquals(numRows, parsedData.size());
            assertEquals(numCols, parsedData.get(0).size());
        }
    }
}
