package edu.brown.cs32.examples.moshiExample.server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs32.examples.moshiExample.weather.ForecastData;
import edu.brown.cs32.examples.moshiExample.weather.Grid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;

/**
 * Handler class for the current weather API endpoint.
 *
 * <p>This endpoint takes a basic GET request with no Json body and two query parameters: lat and
 * lon. They denote the latitude and longitude of the location upon which to retrieve the weather
 * of. A request using these parameters is then sent to the NWS API to retrieve the current
 * temperature. The endpoint returns a Json object in reply denoting if the request was successful,
 * and if it was, the current temperature at the specified coordinates.
 */
public class WeatherHandler implements DataHandler {

  /**
   * Handles the weather request.
   *
   * @param request the request to handle
   * @param response use to modify properties of the response
   * @return the current temperature if a request is successful, an error status if not.
   */
  @Override
  public Object handle(Request request, Response response) {
    QueryParamsMap queryMap = request.queryMap();
    // TODO: Populate the HashMap here. HashMap<Request as a unmodifiable List<String> of queryMap
    //  values, Response> where the response is the result of the this.handleData(queryMap) ie
    //  serialized String.
    // eg of code: HashMap<List<String>, String> cache;
    //
    //cache.put(
    //    // unmodifiable so key cannot change hash code
    //    Collections.unmodifiableList(Arrays.asList("queryMapvalue1", "queryMapvalue1")),
    //    response String)
    //  TODO: Check if the List is already in the HashMap and if it is just return the value for
    //   that key if key not in HashMap, add new request and response to HashMap
    // TODO: inner if statement to check call the allowCache() method to specify if new key value
    //    pair should be added.
    return this.handleData(queryMap);
  }

  // TODO:defensive programming: create a method that returns the response that is needed
  //  from the HashMap

  // TODO: Create allowCache() method that takes in the queryMap and returns a boolean specifying
  //  if the response of the API request allows caching. Checks instruction field in page response

  /**
   * Helper method to deserialize Json response from NWS API, makes Mock
   * testing easier. *added during Reflection*
   * @param deserializeTo - Object to deserialize Json response to
   * @param jsonBody - Json response to extract necessary information from
   * @return - the object deserialized from the Json
   * @throws IOException - if Json cannot be deserialized
   */
  public Object deserialize(Class deserializeTo, String jsonBody) throws IOException {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<Object> jsonAdapter = moshi.adapter(deserializeTo);
      Object newObject = jsonAdapter.fromJson(jsonBody);
      if (newObject == null) {
          throw new IOException();
        } else {
        return newObject;
      }
  }


  /**
   * Retrieve the temperature at the provided latitude and longitude coordinates. Calls upon two
   * helper methods to establish connections with the NWS API and navigate to the necessary endpoint
   * which contains the current temperature.
   * @param queryMap - the hashmap containing the query parameters and their inputs;
   * @return the current temperature if a request is successful, an error status if not.
   */
  public String handleData(QueryParamsMap queryMap) {
    try {
      if ((!(queryMap.hasKey("lon"))) || (!(queryMap.hasKey("lat")))) {
        Map<String, Object> results = new LinkedHashMap<>();
        results.put("result", "error_bad_request");
        return new ResponseUtilities().serialize(results);
      } else {
        String latitude = queryMap.get("lat").value();
        String longitude = queryMap.get("lon").value();
        String forecastURL = this.retrieveGridURL(latitude, longitude);
        String forecast = this.retrieveForecast(forecastURL);
        Map<String, Object> results = new LinkedHashMap<>();
        results.put("result", "success");
        results.put("temperature", forecast);
        return new ResponseUtilities().serialize(results);
      }
    } catch (Exception e) {
      Map<String, Object> results = new LinkedHashMap<>();
      results.put("result", "error_datasource");
      return new ResponseUtilities().serialize(results);
    }
  }

  /**
   * Establishes the initial connection with the NWS API using the 'points' endpoint. This endpoint
   * then has its body contents deserialized using Moshi and the Grid class to obtain the NWS API's
   * grid endpoint that contains the forecast data at the inputted latitude and longitude
   * coordinates.
   *
   * @param latitude the latitude coordinate
   * @param longitude the longitude coordinate
   * @return the URL corresponding to the NWS API's endpoint that contains the necessary forecast
   *     data
   * @throws IOException when a connection is unable to be established with the NWS API client
   * @throws InterruptedException when the connection to the NWS API is interrupted
   * @throws URISyntaxException when the GET request to the NWS API is ill-formatted
   */
  public String retrieveGridURL(String latitude, String longitude)
      throws IOException, InterruptedException, URISyntaxException {
    String weatherURL = "https://api.weather.gov/points/" + latitude + "," + longitude;
    HttpResponse<String> gridEndPoint = APIUtilities.getURLResponse(weatherURL);
    String gridJsonText = gridEndPoint.body();
    Grid propertiesData = (Grid) deserialize(Grid.class, gridJsonText);
//    Moshi moshi = new Moshi.Builder().build();
//    JsonAdapter<Grid> gridAdapter = moshi.adapter(Grid.class);
//    Grid propertiesData = gridAdapter.fromJson(gridJsonText);
    String forecastURL = propertiesData.properties.forecast;
    return forecastURL;
  }

  /**
   * Establishes the secondary connection with the NWS API using the 'gridpoints' endpoint. This
   * endpoint then has its body contents deserialized using Moshi and the ForecastData class to
   * obtain the current temperature at that specified grid.
   *
   * @param forecastURL the URL address to establish a connection to
   * @return the current temperature as a string
   * @throws IOException when a connection is unable to be established with the NWS API client
   * @throws InterruptedException when the connection to the NWS API is interrupted
   * @throws URISyntaxException when the GET request to the NWS API is ill-formatted
   */
  public String retrieveForecast(String forecastURL)
      throws IOException, InterruptedException, URISyntaxException {
    HttpResponse<String> forecastEndPoint = APIUtilities.getURLResponse(forecastURL);
    String forecastJsonText = forecastEndPoint.body();
    ForecastData forecastData = (ForecastData) deserialize(ForecastData.class, forecastJsonText);
//    Moshi forecastMoshi = new Moshi.Builder().build();
//    JsonAdapter<ForecastData> forecastAdapter = forecastMoshi.adapter(ForecastData.class);
//    ForecastData forecastData = forecastAdapter.fromJson(forecastJsonText);
    Number todayTemp = forecastData.properties.periods.get(0).temperature;
    return todayTemp.toString();
  }
}
