package edu.brown.cs32.examples.moshiExample.server.handlers;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

/**
 * Handler class for the getcsv API endpoint.
 *
 * <p>This endpoint takes a basic GET request with no Json body, and returns a Json object in reply
 * denoting if a CSV file's contents are retrieved and the CSV file contents themselves if
 * applicable.
 */
public class getCSVHandler implements DataHandler {

  /**
   * Shared state that maps a CSV filepath to its parsed data.
   */
  private final HashMap<String, Object> filepaths;
  private final HashMap<String, Integer> stats;


  /**
   * Constructor accepts a state shared with the Handler class for the loadcsv API endpoint.
   *
   * @param filepaths the shared state; a HashMap mapping a CSV filepath to the parsed data
   */
  public getCSVHandler(HashMap<String, Object> filepaths,
                       HashMap<String, Integer> stats) {
    this.filepaths = filepaths; // defensive copy
    this.stats = stats;
  }

  /**
   * Getter method for the shared state filepaths that creates a defensive copy.
   *
   * @return a defensive copy of the shared state
   */
  public HashMap<String, Object> getFilepaths() {
    return new HashMap<>(filepaths);
  }

  /**
   * Handles the request to retrieve a previously loaded csv.
   *
   * @param request the request to handle
   * @param response use to modify properties of the response
   * @return the CSV file contents if the shared state contains the necessary key, an error response
   * otherwise
   */
  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap queryMap = request.queryMap();
    if (!(this.filepaths.containsKey("currentFilePath"))) {
      Map<String, Object> results = new LinkedHashMap<>();
      results.put("result", "error_datasource");
      return new ResponseUtilities().serialize(results);
    } else {
      return this.handleData(queryMap);
    }
  }

  /**
   * If previously loaded by entering a valid request to the loadcsv endpoint, the parsed contents
   * of the CSV are returned as a 2D Json array.
   * @param queryMap - the hashmap containing the filepath query and its inputted parameter.
   * @return the CSV file contents if the shared state contains the necessary key, an error response
   * otherwise
   */
  public String handleData(QueryParamsMap queryMap) {
    Object parsedData = this.filepaths.get("currentFilePath");
    Integer rows = this.stats.get("rowCount");
    Integer cols = this.stats.get("colCount");
    Map<String, Object> results = new LinkedHashMap<>();
    results.put("result", "success");
    results.put("data", parsedData);
    results.put("rowCount", rows);
    results.put("colCount", cols);
    return new ResponseUtilities().serialize(results);
  }
}
