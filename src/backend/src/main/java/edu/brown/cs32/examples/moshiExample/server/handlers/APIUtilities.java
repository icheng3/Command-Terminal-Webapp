package edu.brown.cs32.examples.moshiExample.server.handlers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * Class that establishes a connection between the server and a URL endpoint.
 */
public class APIUtilities {

  /**
   * Establishes the server's connection with a provided URL endpoint and
   * returns the contents of the endpoint.
   * @param url - the URL to establish a connection with
   * @return - the contents of the enpoint
   * @throws IOException when a connection is unable to be established with the API
   * @throws InterruptedException when the connection to the API is interrupted
   * @throws URISyntaxException when the GET request to the API is ill-formatted
   */
  public static HttpResponse<String> getURLResponse(String url)
      throws IOException, URISyntaxException, InterruptedException {
    URL requestURL = new URL(url);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    clientConnection.connect();
    HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
    HttpResponse<String> endPoint =
        HttpClient.newBuilder().build().send(httpRequest, BodyHandlers.ofString());
    clientConnection.disconnect();
    return endPoint;
  }
}
