package ctjava.station;

import ctjava.Train;
import java.util.LinkedList;
import java.util.List;

public class Station
{
  // Variables
  private String code;
  private String name;
  private List<String> synonyms = new LinkedList<>();
  private List<Train> trains = new LinkedList<>();
  
  // Management
  public String getCode()
  {
    return this.code;
  }
  public Station withCode(String code)
  {
    this.code = code;
    return this;
  }
  public String getName()
  {
    return this.name;
  }
  public Station withName(String name)
  {
    this.name = name;
    return this;
  }
  public List<String> getSynonyms()
  {
    return this.synonyms;
  }
  public Station withSynonyms(List<String> synonyms)
  {
    this.synonyms = synonyms;
    return this;
  }
  public List<Train> getTrains()
  {
    return this.trains;
  }
  public Station withTrains(List<Train> trains)
  {
    this.trains = trains;
    return this;
  }
  
  // Load a station
}
