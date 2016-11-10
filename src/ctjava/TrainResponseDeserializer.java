package ctjava;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import ctjava.Train;
import ctjava.util.json.JsonDeserializer;
import ctjava.util.json.JsonException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

public class TrainResponseDeserializer implements JsonDeserializer<List<Train>>
{
  // Deserialize to List<Train>
  @Override public List<Train> deserialize(JsonValue json) throws JsonException
  {
    try
    {
      // Get the root object
      JsonObject jsonObject = json.asObject();
      
      // Get the train array
      JsonArray jsonTrains = jsonObject.get("vertrektijden").asArray();
      
      // Create an empty list
      List<Train> trains = new LinkedList<>();
      
      // Iterate over the train array
      for (JsonValue jsonTrainsElement : jsonTrains)
      {
        // Get the train object
        JsonObject jsonTrain = jsonTrainsElement.asObject();
        
        // Create a train and fill it
        Train train = new Train()
          .withNumber(jsonTrain.get("treinNr").asString())
          .withType(jsonTrain.get("soort").asString())
          .withOperator(jsonTrain.get("vervoerder").asString())
          .withDestination(jsonTrain.get("bestemming"))
          .withRoute(jsonTrain.get("via"))
          .withTime(new SimpleDateFormat().parse(jsonTrain.get("vertrek").asString()))
          .withDelay(new Duration(jsonTrain.get("vertraging")))
          .withPlatform(jsonTrain.get("spoor").asString())
          .withInfo(jsonTrain.get("opmerkingen"))
          .withInfoOptional(jsonTrain.get("tips"));
                
        // Add the train to the list
        trains.add(train);
      }
      
      // Return the train list
      return trains;
    }
    catch (UnsupportedOperationException | NullPointerException ex)
    {
      throw new JsonException("Malformed JSON format: " + ex.getMessage(),ex);
    }
  }
}
