package ctjava.train;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import ctjava.station.Station;
import ctjava.util.json.JsonDeserializer;
import ctjava.util.json.JsonException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class Train
{
  // Variables
  private String number;
  private String type;
  private String operator;
  private Station destination;
  private List<Station> route = new LinkedList<>();
  private ZonedDateTime time;
  private Duration delay;
  private String platform;
  private List<String> info = new LinkedList<>();
  private List<String> tips = new LinkedList<>();
  
  // Management
  public String getNumber()
  {
    return this.number;
  }
  public Train withNumber(String number)
  {
    this.number = number;
    return this;
  }
  public String getType()
  {
    return this.type;
  }
  public Train withType(String type)
  {
    this.type = type;
    return this;
  }
  public String getOperator()
  {
    return this.operator;
  }
  public Train withOperator(String operator)
  {
    this.operator = operator;
    return this;
  }
  public Station getDestination()
  {
    return this.destination;
  }
  public Train withDestination(Station destination)
  {
    this.destination = destination;
    return this;
  }
  public List<Station> getRoute()
  {
    return this.route;
  }
  public Train withRoute(List<Station> route)
  {
    this.route = route;
    return this;
  }
  public ZonedDateTime getTime()
  {
    return this.time;
  }
  public Train withTime(ZonedDateTime time)
  {
    this.time = time;
    return this;
  }
  public Duration getDelay()
  {
    return this.delay;
  }
  public Train withDelay(Duration delay)
  {
    this.delay = delay;
    return this;
  }
  public String getPlatform()
  {
    return this.platform;
  }
  public Train withPlatform(String platform)
  {
    this.platform = platform;
    return this;
  }
  public List<String> getInfo()
  {
    return this.info;
  }
  public Train withInfo(List<String> info)
  {
    this.info = info;
    return this;
  }
  public List<String> getTips()
  {
    return this.tips;
  }
  public Train withTips(List<String> tips)
  {
    this.tips = tips;
    return this;
  }
  
  // Return the time including delay
  public ZonedDateTime getActualTime()
  {
    return this.time.plus(this.delay);
  }
  
  // To String
  @Override public String toString()
  {
    return this.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " " + this.getType() + " " + this.getDestination().getName() + (!this.getDelay().isZero() ? " +" + this.getDelay().toMinutes() : "");
  }
  
  // Train deserializer class
  public static class Deserializer implements JsonDeserializer<Train>
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
          .withDestination(new Station().withName(jsonTrain.get("bestemming").asString()))
          .withTime(ZonedDateTime.parse(jsonTrain.get("vertrek").asString(),DateTimeFormatter.ISO_DATE_TIME))
          .withDelay(Duration.ofMinutes((long)jsonTrain.get("vertraging").asDouble()));
      
        if (jsonTrain.get("via") != null && !jsonTrain.get("via").isNull())
        {
          String[] route = jsonTrain.get("via").asString().split(", ");
          train.withRoute(Arrays.stream(route)
            .map(name -> new Station().withName(name)).collect(Collectors.toList()));
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
}
