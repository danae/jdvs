package ctjava.train;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import ctjava.station.Station;
import ctjava.util.json.JsonException;
import ctjava.util.json.JsonSerializer;
import java.time.format.DateTimeFormatter;

public class TrainSerializer implements JsonSerializer<Train>
{
  @Override public JsonValue serialize(Train train)
  {
    return Json.object()
      .add("number",train.getNumber())
      .add("type",train.getType())
      .add("operator",train.getOperator())
      .add("destination",train.getDestination().getName())
      .add("route",Json.array(train.getRoute().stream().map(Station::getName).toArray(String[]::new)))
      .add("time",train.getTime().format(DateTimeFormatter.ofPattern("HH:mm")))
      .add("delay",train.getDelay().toMinutes())
      .add("platform",train.getPlatform())
      .add("info",Json.array(train.getInfo().toArray(new String[0])))
      .add("tips",Json.array(train.getTips().toArray(new String[0])));
  }  
}
