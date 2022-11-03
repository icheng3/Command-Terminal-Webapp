package edu.brown.cs32.examples.moshiExample.csv;

import edu.brown.cs32.examples.moshiExample.csv.attachments.CreatorFromRow;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSV parser that takes in a CSV reader of any type and a boolean specifying is.excludeHeader is
 * present and returns a list of specified objects as specified by parser object input.
 *
 * @author - solaiya
 * @version - 1
 * @param <T> Object type that the parser is specialized for
 */
public class CsvParser<T> {

  private Reader data;
  private CreatorFromRow<T> rowParser;
  private boolean excludeHeader;

  /**
   * Creates a CSV Parser that is generic in that it can parse a CSV into a list of generic object.
   *
   * @param data - reader object that reads the file data
   * @param rowParser - object specific parser that specifies and creates the object type of list
   * @param excludeHeader - specifies whether reader should excluds.excludeHeader of not
   */
  public CsvParser(Reader data, CreatorFromRow<T> rowParser, boolean excludeHeader) {
    this.data = data;
    this.rowParser = rowParser;
    this.excludeHeader = excludeHeader;
  }

  /**
   * Parser method that reads in a reader object and returns list of objects that are of interest to
   * be extracted from csv.
   *
   * @return a list of parsed objects from a csv according to the row parser create function
   * @throws IOException - a file not found exception
   * @throws FactoryFailureException - if object cannot be created with create method
   */
  public List<T> parse() throws IOException, FactoryFailureException {
    List<List<String>> allRows = new ArrayList<>();
    int readCount = 0;
    // wrap in buffered reader
    Reader copy = this.data;
    BufferedReader bufferedReader = new BufferedReader(copy);
    String line = bufferedReader.readLine();
    String delimiter = ",";
    // if excludeHeader is true, skip the first line of the csv
    if (!this.excludeHeader) {
      readCount = 1;
    }
    while (line != null) {
      if (readCount > 0) { // if excludeHeader is true, readcount == 0 and first line is skipped
        List<String> row = List.of(line.split(delimiter));
        allRows.add(row);
      }
      line = bufferedReader.readLine();
      readCount += 1;
    }
    List<T> parsedRows = new ArrayList<>();

    // create a list of object of type T based on object specific row-parser
    for (List<String> r : allRows) {
      try {
        T parsedRow = (T) this.rowParser.create(r);
        parsedRows.add(parsedRow);
      } catch (FactoryFailureException f) {
        throw new FactoryFailureException(r); // throw exception if factory cannot create object
      }
    }
    bufferedReader.close();
    return parsedRows;
  }
//  public int[] counter() throws IOException {
////    // storing total word, character, row, column count
////    int wordCount = 0;
////    int charCount = 0;
////    int rowCount = 0; // headings are excluded, so we subtract the first line
////    int colCount = 0;
////
////    try {
////      // reading a file line-by-line using the BufferedReader
////      BufferedReader bReader = new BufferedReader(this.data);
////      String line = bReader.readLine();
////
////      // computing the number of commas
////      int commaCount = 0;
////      if (line != null) {
////        for (int i = 0; i < line.length(); i++) {
////          if (line.charAt(i) == ',') {
////            commaCount++;
////          }
////        }
////        // since column count is the number of commas + 1
////        colCount = commaCount + 1;
////      }
////
////      while (line != null) {
////        rowCount++;
////
////        // computing the number of columns
////        String[] splitLine = line.split("\\s+|,\\s+");
////
////        for (String word : splitLine) {
////          if (word != " ") {
////            wordCount++;
////            charCount += word.toCharArray().length;
////          }
////        }
////
////        // getting next line from file
////        line = bReader.readLine();
////      }
////
////      // configuring rowCount
////      if (rowCount == 0) {
////        rowCount = 0;
////      } else {
////        rowCount--;
////      }
////
////      // closing file after we're done
////      bReader.close();
////
////    } catch (IOException e) {
////      System.out.println("Encountered an error: " + e.getMessage());
////    } catch (NullPointerException f) {
////      System.out.println("Encountered an error: " + f.getMessage());
////    }
////
////    int[] res = new int[]{rowCount, colCount};
////    return res;
////  }

  public int[] count()
          throws IOException, FactoryFailureException {
    // first initializes all counts back to
    Reader copy = this.data;
    Integer colCount = 0;
    Integer rowCount = 0;
    BufferedReader br = new BufferedReader(copy);
    String[] row;
    String line = "";
    List<T> parsedData = new ArrayList<>();
    String SEPARATOR = ",";
    // flag ensuring that the header is not considered \
    // in word utility counts
    // to User: set to false if you wish to include header
    // in parsing
    boolean headerRow = true;
    // processes the Reader object line by line
    while ((line = br.readLine()) != null) {
      row = line.split(SEPARATOR);
      // first converts row, an Array, into a List of Strings
      // then removes empty columns from the split line
      List<String> rowList = this.removeEmpty(Arrays.asList(row));
      // condition only entered when processing first line of any object
      if (headerRow) {
        for (String col : row) {
          if (!col.equals("")) {
            colCount += 1;
          }
        }
        headerRow = false;
      } else if (rowList.size() > 0) {
        if (row.length > 0) {
          rowCount += 1;
        }// using the contents of rowList, \
        // converts into the Object Type specified
      }
    }
    br.close();
    int[] res = new int[]{rowCount, colCount};
    return res;
  }
  /**
   * Removes empty strings from a List of Strings.
   *
   * @param parsedRow the List of Strings to be filtered
   * @return a List of Strings barring those that are empty
   */
  public List<String> removeEmpty(List<String> parsedRow) {
    List<String> cleanedRow = new ArrayList<>();
    for (String str : parsedRow) {
      if (str.length() > 0) {
        cleanedRow.add(str);
      }
    }
    return cleanedRow;
  }
}

