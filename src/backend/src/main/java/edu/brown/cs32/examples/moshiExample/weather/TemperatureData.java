package edu.brown.cs32.examples.moshiExample.weather;

import java.util.List;

/**
 * This class is used to deserialize the nested contents of a successful request response to the NWS
 * API's 'gridpoints' endpoint.
 */
public class TemperatureData {
  public List<Period> periods;
}
