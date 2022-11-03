package edu.brown.cs32.examples.moshiExample.server;

import static spark.Spark.after;

import edu.brown.cs32.examples.moshiExample.server.handlers.WeatherHandler;
import edu.brown.cs32.examples.moshiExample.server.handlers.getCSVHandler;
import edu.brown.cs32.examples.moshiExample.server.handlers.loadCSVHandler;
import java.util.HashMap;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers.
 *
 * <p>We have three endpoints in this demo: loadcsv, getcsv, and weather. Requests to loadcsv loads
 * a CSV file if one is located at the specified path, provided by the query parameter 'filepath.'
 * Requests to getcsv sends back the previously loaded CSV file's contents as a Json 2-dimensional
 * array. loadcsv and getcsv share a state, specifically a HashMap that maps the filepath to the
 * parsed data of its corresponding CSV file's contents. Requests to weather sends back the
 * temperature at the specified location provided by its query parameters 'lat' and 'lon'
 * corresponding to latitude and longitude, respectively.
 */
public class Server {
  public static void main(String[] args) {
    HashMap<String, Object> filepaths = new HashMap<>();
    HashMap<String, Integer> stats = new HashMap<>();
    Spark.port(3000);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    // Setting up the handler for the loadcsv endpoint
    Spark.get("loadcsv", new loadCSVHandler(filepaths, stats));

    // Setting up the handler for the getcsv endpoint
    Spark.get("getcsv", new getCSVHandler(filepaths, stats));

    // Setting up handler for the weather endpoint
    Spark.get("weather", new WeatherHandler());

    System.out.println("Server started.");

    Spark.init();
    Spark.awaitInitialization();
  }
}
