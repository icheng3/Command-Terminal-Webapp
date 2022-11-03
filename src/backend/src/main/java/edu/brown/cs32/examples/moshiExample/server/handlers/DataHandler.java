package edu.brown.cs32.examples.moshiExample.server.handlers;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * A request handler that is involved with retrieving and relaying data
 * stored locally.
 */
public interface DataHandler extends Route {
  /**
   * Handles the request.
   * @param request - the request given.
   * @param response - the response to be modified.
   * @return the response content.
   */
  Object handle(Request request, Response response);

  /**
   * Handles the data-driven end of the request.
   * @param queryMap - the hashmap containing the query parameter and its inputs.
   * @return - the response content.
   */
  String handleData(QueryParamsMap queryMap);
}
