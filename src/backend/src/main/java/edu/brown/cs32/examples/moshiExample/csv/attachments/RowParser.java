package edu.brown.cs32.examples.moshiExample.csv.attachments;

import edu.brown.cs32.examples.moshiExample.csv.FactoryFailureException;
import edu.brown.cs32.examples.moshiExample.csv.attachmentutilities.Row;
import java.util.List;

/**
 * This is an object specific row parser that creates a Row object from a list of String.
 *
 * @author - solaiya
 * @version - 1
 */
public class RowParser implements CreatorFromRow<Row> {

  /**
   * Creates a Row Object containing count data for each line from a list of Strings (ie a line).
   *
   * @param row - the list of string in a line of csv
   * @return - a Row object that contains the count data of a given line of a csv
   * @throws FactoryFailureException -
   */
  public Row create(List<String> row) throws FactoryFailureException {
    // the number of columns is the size of the input list of strings
    int colNumber = row.size();

    int wordCount = 0;
    int charCount = 0;

    // counts the characters in the specifies list of strings
    for (String cellVal : row) {
      // Counts each character including space
      for (int i = 0; i < cellVal.length(); i++) {
        charCount++;
      }

      // Counts the number of words in each 'cell' of row ie in the list of strings
      if (cellVal == null || cellVal.isEmpty()) { // is a cell in csv is empty
        wordCount = wordCount;
      } else {
        String[] wordsInString = cellVal.split("\\s+");
        wordCount = wordCount + wordsInString.length;
      }
    }

    // creates a new row with the new info
    return new Row(wordCount, colNumber, charCount);
  }
}
