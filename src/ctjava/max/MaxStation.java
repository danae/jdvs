package ctjava.max;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;
import ctjava.station.Station;
import ctjava.station.StationList;
import ctjava.station.StationListException;
import ctjava.train.TrainListException;
import ctjava.train.TrainSerializer;

public class MaxStation extends MaxObject
{
  // Variables
  private StationList stationList;
  private Station station;
  
  // Constructor
  public MaxStation()
  {
    try
    {
      // Initialize Max object
      this.declareInlets(new int[]{DataTypes.MESSAGE});
      this.setInletAssist(new String[]{"Message containing the station name to get the departing trains, bang to refresh the departing trains"});
      this.declareOutlets(new int[]{DataTypes.LIST});
      this.setOutletAssist(new String[]{"List of trains departing as JSON strings (to use in dict.deserialize)"});
      this.createInfoOutlet(false);
      
      // Load station list
      this.stationList = new StationList();
    }
    catch (StationListException ex)
    {
      MaxObject.bail(ex.getMessage());
    }
  }
  
  // When a message is received
  @Override public void anything(String message, Atom[] args)
  {
    // Set the station
    this.station = this.stationList.find(message);
  }
  
  // When a bang is received
  @Override public void bang()
  {
    // Check if the station is already defined
    if (this.station == null)
      return;
    
    try
    {
      // Create an array of the departing trains
      String[] trains = this.station.getTrains().stream()
        .map(train -> new TrainSerializer().serialize(train).toString())
        .toArray(String[]::new);
      
      // Output
      this.outlet(0,trains);
    }
    catch (TrainListException ex)
    {
      MaxObject.getErrorStream().println(ex.getMessage());
    }
  }
}
