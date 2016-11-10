package ctjava.train;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import ctjava.station.Station;
import ctjava.util.json.JsonDeserializer;
import ctjava.util.json.JsonException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class TrainDeserializer implements JsonDeserializer<Train>
{
  // Constants
  private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
  
  // Deserialize to Train
  @Override public Train deserialize(JsonValue json) throws JsonException
  {
    try
    {
      // Get the train object
      JsonObject jsonTrain = json.asObject();
      
      // Create a train and fill it
      Train train = new Train()
        .withNumber(jsonTrain.get("treinNr").asString())
        .withType(jsonTrain.get("soort").asString())
        .withOperator(jsonTrain.get("vervoerder").asString())
        .withDestination(new Station.Fake(jsonTrain.get("bestemming").asString()))
        .withTime(new SimpleDateFormat(ISO_DATE_FORMAT).parse(jsonTrain.get("vertrek").asString()))
        .withDelay(Duration.ofMinutes((long)jsonTrain.get("vertraging").asDouble()));
      
      if (jsonTrain.get("via") != null && !jsonTrain.get("via").isNull())
      {
        String[] route = jsonTrain.get("via").asString().split(", ");
        train.withRoute(Arrays.stream(route)
          .map(Station.Fake::new).collect(Collectors.toList()));
      }
    
      for (JsonValue jsonValue : jsonTrain.get("opmerkingen").asArray())
        train.getInfo().add(jsonValue.asString());
      
      for (JsonValue jsonValue : jsonTrain.get("tips").asArray())
        train.getTips().add(jsonValue.asString());
      
      if (!jsonTrain.get("spoor").isNull())
        train.withPlatform(jsonTrain.get("spoor").asString());
      
      // Return the train
      return train;
    }
    catch (UnsupportedOperationException | NullPointerException | ParseException ex)
    {
      throw new JsonException("Malformed JSON format: " + ex.getMessage(),ex);
    }
  }
}
