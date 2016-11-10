package ctjava.station;

import ctjava.train.Train;
import ctjava.train.TrainList;
import java.util.LinkedList;
import java.util.List;

public final class RealStation implements Station
{
  // Variables
  private final String code;
  private String name;
  private List<String> synonyms = new LinkedList<>();
  private List<Train> trains;
  
  // Constructor
  public RealStation(String code)
  {
    this.code = code;
    
    try
    {
      this.trains = new TrainList(this.code).getList();
    }
    catch (StationException ex)
    {
      throw new RuntimeException("Could not load station: " + ex.getMessage());
    }
  }
  
  // Management
  @Override public String getCode()
  {
    return this.code;
  }
  @Override public String getName()
  {
    return this.name;
  }
  public RealStation withName(String name)
  {
    this.name = name;
    return this;
  }
  @Override public List<String> getSynonyms()
  {
    return this.synonyms;
  }
  public RealStation withSynonyms(List<String> synonyms)
  {
    this.synonyms = synonyms;
    return this;
  }
  @Override public List<Train> getTrains()
  {
    return this.trains;
  }
}
