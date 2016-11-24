package com.dengsn.dvs.train;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import com.dengsn.dvs.util.http.HttpException;
import com.dengsn.dvs.util.http.HttpRequest;
import com.dengsn.dvs.util.json.JsonException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class TrainList implements Iterable<Train>
{
  // Variables
  private final List<Train> trains = new LinkedList<>();
  
  // Constructor
  public TrainList(String code) throws TrainListException
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
    catch (HttpException ex)
    {
      throw new TrainListException("Could not load trains because the API request failed ",ex);
    }
    catch (JsonException ex)
    {
      throw new TrainListException("Could not load trains because the API response was invalid or incomplete",ex);
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
