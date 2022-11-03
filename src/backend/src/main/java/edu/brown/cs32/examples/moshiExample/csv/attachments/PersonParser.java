package edu.brown.cs32.examples.moshiExample.csv.attachments;

import edu.brown.cs32.examples.moshiExample.csv.FactoryFailureException;
import edu.brown.cs32.examples.moshiExample.csv.attachmentutilities.Person;
import java.util.List;

/**
 * This is an object specific row parser that creates a Row object from a list of String.
 *
 * @author - solaiya
 * @version - 1
 */
public class PersonParser implements CreatorFromRow<Person> {

  /**
   * Creates a Row Object containing count data for each line from a list of Strings (ie a line).
   *
   * @param row - the list of string in a line of csv
   * @return - a Row object that contains the count data of a given line of a csv
   * @throws FactoryFailureException -
   */
  public Person create(List<String> row) throws FactoryFailureException {
    if (row.size() != 4) {
      throw new FactoryFailureException(row);
    } else {
      String firstName = row.get(0);
      String lastName = row.get(1);
      return new Person(firstName, lastName);
    }
  }
}
