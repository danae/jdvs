package ctjava.train;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import ctjava.station.StationException;
import ctjava.util.http.HttpException;
import ctjava.util.http.HttpRequest;
import ctjava.util.json.JsonException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class TrainList implements Iterable<Train>
{
  // Variables
  private final List<Train> trains = new LinkedList<>();
  
  // Constructor
  public TrainList(String code) throws StationException
  {
    try
    {
      // Make request to the RDT-API
      HttpRequest http = new HttpRequest("http://api.rijdendetreinen.nl/v2/json/vertrektijden");
      http.setParameter("station",code);
      JsonValue json = http.jsonRequest();
      
       // Get the train data
      JsonArray jsonTrains = json.asObject().get("vertrektijden").asArray();
      
      // Add each train to the list
      for (JsonValue jsonTrain : jsonTrains)
        this.trains.add(new TrainDeserializer().deserialize(jsonTrain));
    }
    catch (HttpException | JsonException ex)
    {
      throw new StationException("Could not load trains: " + ex.getMessage(),ex);
    }
  }
  
  // Management
  public List<Train> getList()
  {
    return this.trains;
  }

  // Get the iterator for this train list
  @Override public Iterator<Train> iterator()
  {
    return this.trains.iterator();
  }
}
