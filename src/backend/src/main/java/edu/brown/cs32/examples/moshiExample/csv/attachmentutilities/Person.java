package edu.brown.cs32.examples.moshiExample.csv.attachmentutilities;

/**
 * Class that represents a person with a first name and last name.
 *
 * @author - solaiya
 * @version - 1
 */
public record Person(String firstName, String lastName) {

  /**
   * Returns the name of a person.
   *
   * @return - string that is the full name of a person
   */
  public String getName() {
    return new String(this.firstName + " " + this.lastName);
  }
}
