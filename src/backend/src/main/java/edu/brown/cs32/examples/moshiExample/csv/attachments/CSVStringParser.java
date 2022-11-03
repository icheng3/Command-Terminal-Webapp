package edu.brown.cs32.examples.moshiExample.csv.attachments;

import edu.brown.cs32.examples.moshiExample.csv.FactoryFailureException;
import java.util.List;

public class CSVStringParser implements CreatorFromRow<List<String>> {

  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
