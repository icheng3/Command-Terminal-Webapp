package edu.brown.cs32.examples.moshiExample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.Moshi;
import edu.brown.cs32.examples.moshiExample.server.handlers.getCSVHandler;
import edu.brown.cs32.examples.moshiExample.server.handlers.loadCSVHandler;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
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
 * Tests the CSVHandler classes: loadCSVHandler and getCSVHandler.
 */
public class TestCSVAPIHandlers {

  /**
   * Before everything, declare a spark port.
   */
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING);
  }

  /**
   * Shared state for all tests. We need to be able to mutate it (adding recipes etc.) but never
   * need to replace the reference itself. We clear this state out after every test runs.
   */
  final HashMap<String, Object> filepaths = new HashMap<>();
  final HashMap<String, Integer> stats = new HashMap<>();


  /**
   * Sets up the Spark server before every test.
   */
  @BeforeEach
  public void setup() {
    filepaths.clear();
    stats.clear();
    Spark.get("/loadcsv", new loadCSVHandler(filepaths, stats));
    Spark.get("/getcsv", new getCSVHandler(filepaths, stats));
    Spark.init();
    Spark.awaitInitialization();
  }

  /**
   * Tells spark to stop listening after each test suite.
   */
  @AfterEach
  public void teardown() {
    Spark.unmap("/loadcsv");
    Spark.unmap("/getcsv");
    Spark.awaitStop();
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private static HttpURLConnection tryLoadRequest(String apiCall, String filePath)
      throws IOException {
    URL requestURL =
        new URL("http://localhost:" + Spark.port() + "/" + apiCall + "?" + "filepath=" + filePath);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private static HttpURLConnection tryBadLoadRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * @param apiCall the call string, including endpoint (NOTE: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private static HttpURLConnection tryGetRequest(String apiCall) throws IOException {
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Test suite for a successful 'loadcsv' request.
   * @throws IOException - acknowledgement to thge type checker
   */
  @Test
  public void testCSVLoadSuccessResponse() throws IOException {
    HttpURLConnection clientConnection =
        tryLoadRequest(
            "loadcsv",
            "src/main/java/edu/brown/cs32/examples/moshiExample/testCSV/persondata.csv");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map<String, Object> output =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("success", output.get("result"));
    assertEquals("src/main/java/edu/brown/cs32/examples/moshiExample/testCSV/persondata.csv",
            output.get("filepath"));
    clientConnection.disconnect();
  }

  /**
   * Test suite for a 'loadcsv' request when a filepath is provided but invalid.
   * @throws IOException - acknowledgement to the type checker
   */
  @Test
  public void testCSVInvalidFileResponse() throws IOException {
    HttpURLConnection clientConnection = tryLoadRequest("loadcsv", "badfilepath");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map<String, Object> output =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_datasource", output.get("result"));
    assertEquals("badfilepath",
            output.get("filepath"));
    clientConnection.disconnect();
  }

  /**
   * Test suite for a 'loadcsv' request when no filepath is provided.
   * @throws IOException - acknowledgement to thge type checker
   */
  @Test
  public void testCSVInvalidParam() throws IOException {
    HttpURLConnection clientConnection = tryBadLoadRequest("loadcsv");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map<String, Object> output =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_bad_request", output.get("result"));
    clientConnection.disconnect();
  }

  /**
   * Test suite for an unsuccessful 'getcsv' request, ie when a csv is not previously loaded
   * before requesting 'getcsv'.
   * @throws IOException - acknowledgement to thge type checker
   */
  @Test
  public void testCSVNoFileFailureResponse() throws IOException {
    HttpURLConnection clientConnection = tryGetRequest("getcsv");
    assertEquals(200, clientConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map<String, Object> output =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    assertEquals("error_datasource", output.get("result"));
    clientConnection.disconnect();
  }

  /**
   * Test suite for a successful 'getcsv' request.
   * @throws IOException - acknowledgement to thge type checker
   */
  @Test
  public void testCSVGetSuccessResponse() throws IOException {
    HttpURLConnection loadConnection =
        tryLoadRequest(
            "loadcsv",
            "src/main/java/" + "edu/brown/cs32/examples/moshiExample/testCSV/persondata.csv");
    assertEquals(200, loadConnection.getResponseCode());
    HttpURLConnection getConnection = tryGetRequest("getcsv");
    assertEquals(200, getConnection.getResponseCode());
    Moshi moshi = new Moshi.Builder().build();
    Map<String, Object> output =
        moshi.adapter(Map.class).fromJson(new Buffer().readFrom(getConnection.getInputStream()));
    assertEquals("success", output.get("result"));
    assertEquals("[[FirstName,  LastName,  Course,  Role]," +
                    " [Tim, Nelson, CSCI 0320, instructor]," +
                    " [Nim, Telson, CSCI 0320, student]," +
                    " [Audrey, Banks, CLPS1200, student]," +
                    " [Michael, Lee, CLPS1200, TA]]",
            output.get("data").toString());
    loadConnection.disconnect();
    getConnection.disconnect();
  }
}
