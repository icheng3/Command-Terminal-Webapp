package edu.brown.cs32.examples.moshiExample.csv.attachmentutilities;

import java.util.List;

/**
 * Counting Utility for the Row class. Contains the counting methods used on a list of Row Objects.
 *
 * @author - solaiya
 * @version - 1
 */
public class RowCount {
  private final List<Row> rowsData;

  /**
   * Constructor for RowCount utility class which performs counting operations for the entire csv ie
   * a list of Row Object each containing count data for every line.
   *
   * @param rowsData - the list of Row objects that contain parsed csv count data
   */
  public RowCount(List<Row> rowsData) {
    this.rowsData = rowsData;
  }

  /**
   * Counts the number of rows in the CSV (through List of .
   *
   * @return number of rows
   */
  public int countRows() {
    return this.rowsData.size();
  }

  /**
   * Counts the number of characters in the CSV.
   *
   * @return number of characters
   */
  public int countChars() {
    int charCount = 0;
    for (Row parsedRow : this.rowsData) {
      charCount = charCount + parsedRow.chars;
    }
    return charCount;
  }

  /**
   * Counts the number of words in the CSV.
   *
   * @return number of words
   */
  public int countWords() {
    int wordCount = 0;
    for (Row parsedRow : this.rowsData) {
      wordCount = wordCount + parsedRow.words;
    }
    return wordCount;
  }

  /**
   * Counts the number of columns in the CSV.
   *
   * @return number of columns ie the maximum number of columns used in the CSV
   */
  public int countCols() {
    int colCount = 0;
    for (Row parsedRow : this.rowsData) {
      if (parsedRow.cols > colCount) {
        colCount = parsedRow.cols;
      }
    }
    return colCount;
  }
}
