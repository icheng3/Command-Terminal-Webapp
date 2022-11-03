package edu.brown.cs32.examples.moshiExample.csv.attachmentutilities;

/**
 * Row class that contain the count data for each row.
 *
 * @author - solaiya
 * @version - 1
 */
public class Row {
  public int words;
  public int cols;
  public int chars;

  /**
   * Contains the count data for each row.
   *
   * @param words - the number of words in a row
   * @param cols - the number of columns in a row
   * @param chars - the number of characters in a row
   */
  public Row(int words, int cols, int chars) {
    this.words = words;
    this.cols = cols;
    this.chars = chars;
  }
}
