package edu.brown.cs32.examples.moshiExample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs32.examples.moshiExample.server.handlers.WeatherHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/**
 * Tests the handlers for the WeatherHandler class.
 */
public class TestWeatherAPIHandler {

  /** Sets the spark port number to 0, indicating to Spark to use an arbitrary available port. */
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  /** Restarts the entire Spark server for every test. */
  @BeforeEach
  public void setup() {
    Spark.get("/weather", new WeatherHandler());
    Spark.init();
    Spark.awaitInitialization();
  }

  /** Tells Spark to stop listening on both endpoints after each test. */
  @AfterEach
  public void teardown() {
    Spark.unmap("/weather");
    Spark.awaitStop();
  }

  /**
   * Helper to start a connection to the weather endpoint given the two parameters.
   *
   * @param apiCall the call string, including endpoint
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private static HttpURLConnection tryRequest(String apiCall, String lat, String lon)
      throws IOException {
    URL requestURL =
        new URL(
            "http://localhost:"
                + Spark.port()
                + "/"
                + apiCall
                + "?"
                + "lat="
                + lat
                + "&lon="
                + lon);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Helper to start a connection to the weather endpoint given the zero parameters.
   *
   * @param apiCall the call string, including endpoint
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private static HttpURLConnection tryBadJsonRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Tests a successful weather response.
   * @throws IOException - acknowledgement to the type checker
   */
  @Test
  public void testWeatherResponse() throws IOException {
    HttpURLConnection clientConnection = tryRequest("weather", "33.4484", "-112.074");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map<String, Object> output =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("success", output.get("result"));
    assertEquals("85",
            output.get("temperature"));
    clientConnection.disconnect();
  }

  /**
   * Tests a weather response given invalid latitude and longitude query parameters.
   * @throws IOException - acknowledgement to the type checker
   */
  @Test
  public void testWeatherInvalidParam() throws IOException {
    HttpURLConnection clientConnection = tryRequest("weather", "", "");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map<String, Object> output =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_datasource", output.get("result"));
    clientConnection.disconnect();
  }

  /**
   * Tests a weather response given no request parameters.
   * @throws IOException - acknowledgement to the type checker
   */
  @Test
  public void testWeatherBadJSONResponse() throws IOException {
    HttpURLConnection clientConnection = tryBadJsonRequest("weather");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map<String, Object> output =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_bad_request", output.get("result"));
    clientConnection.disconnect();
  }
}
