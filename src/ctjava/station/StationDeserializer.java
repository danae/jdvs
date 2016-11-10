package ctjava.station;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import ctjava.util.json.JsonDeserializer;
import ctjava.util.json.JsonException;

public class StationDeserializer implements JsonDeserializer<Station>
{
  // Deserialize to Station
  @Override public Station deserialize(JsonValue json) throws JsonException
  {
    if (!json.isObject())
      throw new JsonException("JSON is not an object");
    
    JsonObject jsonObject = json.asObject();
    
    Station station = new Station();
    if (jsonObject.get("code") != null)
      station.withCode(jsonObject.get("code").toString());
    if (jsonObject.get("name") != null)
      station.withName(jsonObject.get("name").toString());
    if (jsonObject.get("synonyms") != null)
      station.withName(jsonObject.get("name").toString());
    
    return station;
  }
}
