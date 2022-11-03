package edu.brown.cs32.examples.moshiExample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.brown.cs32.examples.moshiExample.server.handlers.WeatherHandler;
import java.io.IOException;
import java.net.URISyntaxException;

import edu.brown.cs32.examples.moshiExample.weather.ForecastData;
import edu.brown.cs32.examples.moshiExample.weather.Grid;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for WeatherHandler's utilities.
 */
public class TestWeatherAPIUtilities {

  final WeatherHandler testHandler = new WeatherHandler();

  /**
   * no set up needed.
   */
  @BeforeEach
  public void setup() {
    // none
  }

  /**
   * no tear down needed.
   */
  @AfterEach
  public void teardown() {
    // no tear down
  }

  /**
   * Test suite for retrieveGridURL utility. Demonstrates a successful retrieval as well
   * as one that throws an Exception.
   * @throws Exception - When invalid latitude and longitude points are provided.
   */
  @Test
  public void testRetrieveGridURL() throws Exception {
    // from Sprint reflection:
    String goodAPIResponse = "{\"properties\": {\"forecast\": " +
            "\"https://api.weather.gov/gridpoints/PSR/158,57/forecast\"}}";
    String badAPIResponse = "badJson, no properties!";

    Grid mockGrid = (Grid) testHandler.deserialize(Grid.class, goodAPIResponse);
    assertEquals("https://api.weather.gov/gridpoints/PSR/158,57/forecast",
            mockGrid.properties.forecast);
    assertThrows(IOException.class, () -> {
      testHandler.deserialize(Grid.class, badAPIResponse);
    });
    // end of Sprint reflection edits

    assertEquals(
        "https://api.weather.gov/gridpoints/PSR/158,57/forecast",
        testHandler.retrieveGridURL("33.4484", "-112.074"));
    assertThrows(Exception.class, () -> {
      testHandler.retrieveGridURL("3000", "2000");
    });
  }

  /**
   * Test suite for retrieveForecast utility. Tests for a successful retrieval
   * as well as one that throws an exception using an invalid URL.
   * @throws Exception - when an invalid URL is provided or forecast information
   * is unsuccessfully deserialized.
   */
  @Test
  public void testRetrieveForecast() throws Exception {
    //from Sprint reflection:
    String goodAPIResponse = "{\"properties\": {\"periods\": [{\"temperature\":" + 87 +"}]}}";
    String badAPIResponse = "badJson, no properties!";
    ForecastData mockForecast = (ForecastData) testHandler.deserialize(ForecastData.class, goodAPIResponse);
    assertEquals(87,
            mockForecast.properties.periods.get(0).temperature);
    assertThrows(IOException.class, () -> {
      testHandler.deserialize(ForecastData.class, badAPIResponse);
    });
    // end of Sprint reflection edits
    assertEquals(
            "87",
            testHandler.retrieveForecast(
                    "https://api.weather.gov/gridpoints/PSR/158,57/forecast"));
    assertThrows(Exception.class, () -> {
      testHandler.retrieveForecast("invalidurl");
    });
  }
}
