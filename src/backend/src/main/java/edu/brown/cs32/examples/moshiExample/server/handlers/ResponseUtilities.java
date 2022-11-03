package edu.brown.cs32.examples.moshiExample.server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Class for utilities needed in generating a response to the server.
 */
public class ResponseUtilities {

  /**
   * Takes in a hashmap and serializes said hashmap into a 2d json array.
   * @param results - the hashmap to be serialized
   * @return a 2d json array displayed as a response
   */
  public String serialize(Map<String, Object> results) {
    try {
      Moshi moshi = new Moshi.Builder().build();
      Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
      JsonAdapter<Map<String, Object>> jsonAdapter = moshi.adapter(type);
      return jsonAdapter.toJson(results);

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

}
