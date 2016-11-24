package com.dengsn.dvs.train;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.dengsn.dvs.station.StationList;
import com.dengsn.dvs.util.json.JsonDeserializer;
import com.dengsn.dvs.util.json.JsonException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TrainDeserializer implements JsonDeserializer<Train>
{  
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
        .withDestination(StationList.getInstance().findOrFake(jsonTrain.get("bestemming").asString()))
        .withTime(ZonedDateTime.parse(jsonTrain.get("vertrek").asString(),DateTimeFormatter.ISO_DATE_TIME))
        .withDelay(Duration.ofMinutes((long)jsonTrain.get("vertraging").asDouble()));
    
      if (jsonTrain.get("via") != null && !jsonTrain.get("via").isNull())
      {
        String[] route = jsonTrain.get("via").asString().split(", ");
        train.withRoute(Arrays.stream(route)
          .map(name -> StationList.getInstance().findOrFake(name))
          .collect(Collectors.toList()));
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
    catch (UnsupportedOperationException | NullPointerException | DateTimeParseException ex)
    {
      throw new JsonException("Malformed JSON format: " + ex.getMessage(),ex);
    }
  }
}
