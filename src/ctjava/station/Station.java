package ctjava.station;

import ctjava.train.Train;
import java.util.Collections;
import java.util.List;

public interface Station
{
  // Fake station class
  public class Fake implements Station
  {
    // Variables
    private final String name;
    
    // Constructor
    public Fake(String name)
    {
      this.name = name;
    }
    
    // Management
    @Override public String getCode()
    {
      return null;
    }
    @Override public String getName()
    {
      return this.name;
    }
    @Override public List<String> getSynonyms()
    {
      return Collections.emptyList();
    }
    @Override public List<Train> getTrains()
    {
      return Collections.emptyList();
    }
  }
  
  // Return the code of the station
  public String getCode();
  
  // Return the name of the station
  public String getName();
  
  // Return the synonyms of the station
  public List<String> getSynonyms();
  
  // Return the trains departing at the station
  public List<Train> getTrains();
}
