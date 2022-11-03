package edu.brown.cs32.examples.moshiExample.server.handlers;

import edu.brown.cs32.examples.moshiExample.csv.ColRowCounter;
import edu.brown.cs32.examples.moshiExample.csv.CsvParser;
import edu.brown.cs32.examples.moshiExample.csv.FactoryFailureException;
import edu.brown.cs32.examples.moshiExample.csv.attachments.CSVStringParser;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

/**
 * Handler class for the csv loading API endpoint.
 *
 * <p>This endpoint takes a basic GET request with no Json body, and returns a Json object in reply
 * denoting if the CSV file at the inputted filepath is successfully loaded and its contents parsed.
 */
public class loadCSVHandler implements DataHandler {
  private final HashMap<String, Object> filepaths;
  private final HashMap<String, Integer> stats;

  /**
   * Constructor accepts a state shared with the Handler class for the getcsv API endpoint.
   *
   * @param filepaths the shared state; a HashMap mapping a CSV filepath to the parsed data
   */
  public loadCSVHandler(HashMap<String, Object> filepaths,
                        HashMap<String, Integer> stats) {
    this.filepaths = filepaths;
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
   * Handles the request to load a csv file.
   *
   * @param request the request to handle
   * @param response use to modify properties of the response
   * @return response content
   */
  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap queryMap = request.queryMap();
    if (!(queryMap.hasKey("filepath"))) {
      Map<String, Object> results = new LinkedHashMap<>();
      results.put("result", "error_bad_request");
      return new ResponseUtilities().serialize(results);
    } else {
      return this.handleData(queryMap);
    }
  }

  /**
   * Retrieve a CSV file and parse its contents. Assign the parsed contents as the value of the file
   * path key in the shared state.
   *
   * @param queryMap - the hashmap containing the filepath query and its inputted parameters.
   * @return a success response if the csv file is successfully loaded, an error response if not.
   * @throws IOException if a file path is provided, but no file is found in the specified path
   * @throws FactoryFailureException when a Factory class fails
   */
  public String handleData(QueryParamsMap queryMap) {
    try {
      String currentFilepath = queryMap.get("filepath").value();
      // only allows access to a specific folder
<<<<<<< HEAD
      // String openFolder = "backend/src/main/java/edu/brown/cs32/examples/moshiExample/testCSV/";
      String openFolder = "backend/src/main/java/edu/brown/cs32/examples/moshiExample/testCSV";
=======
      String openFolder = "/sprint-3-ayeung9-icheng3/data";
>>>>>>> 779a4be8b4eb10feb0f761b383e1f082fedd1936
      if (currentFilepath.contains(openFolder)) {
        CsvParser<List<String>> parser =
            new CsvParser<>(new FileReader(currentFilepath), new CSVStringParser(), false);
        List<List<String>> parsedData = parser.parse();
        ColRowCounter counts = new ColRowCounter(parsedData);
        this.filepaths.put("currentFilePath", parsedData);
        Map<String, Object> results = new LinkedHashMap<>();
        results.put("result", "success");
        results.put("filepath", currentFilepath);
        this.stats.put("rowCount", counts.rowCount());
        this.stats.put("colCount", counts.colCount());
        return new ResponseUtilities().serialize(results);
      } else {
        Map<String, Object> results = new LinkedHashMap<>();
        results.put("result", "error_datasource");
        results.put("filepath", currentFilepath);
        return new ResponseUtilities().serialize(results);
      }
    } catch (IOException | FactoryFailureException e) {
      String currentFilepath = queryMap.get("filepath").value();
      Map<String, Object> results = new LinkedHashMap<>();
      results.put("result", "helllooo");
      results.put("filepath", currentFilepath);
      return new ResponseUtilities().serialize(results);
    }
  }
}
