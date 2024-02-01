package mono.connect.kit;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {
  String type;
  JSONObject data;

  public Event(String type, JSONObject data) {
    this.type = type;
    this.data = data;
  }

  public static Event fromString(String data) throws JSONException {
    JSONObject event = new JSONObject(data);
    JSONObject body = null;
    String type = event.getString("type");

    if (event.has("data")) {
      body = event.getJSONObject("data");
    }

    return new Event(type, body);
  }

  public String getType() {
    return type;
  }

  public JSONObject getData() {
    return data;
  }
}
