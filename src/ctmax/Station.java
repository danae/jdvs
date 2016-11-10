package ctmax;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;
import ctjava.station.StationException;
import ctjava.station.StationList;
import ctjava.train.Train;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class Station extends MaxObject
{
  // Variables
  private StationList stationList;
  
  // Constructor
  public Station()
  {
    try
    {
      this.stationList = new StationList();
    
      // Initialize Max object
      this.declareInlets(new int[]{
        DataTypes.MESSAGE
      });
      this.setInletAssist(new String[]{
        "Message containing the station query to fetch"
      });
      this.declareOutlets(new int[]{
        DataTypes.LIST,
        DataTypes.INT
      });
      this.setOutletAssist(new String[]{
        "List of trains departing as strings",
        "Number of trains departing"
      });
      this.createInfoOutlet(false);
    }
    catch (StationException ex)
    {
      System.err.println("Could not load stations: " + ex.getMessage());
    }
  }

  // When a message or bangis received
  @Override public void anything(String message, Atom[] args)
  {
    // Find the new station
    ctjava.station.Station station = this.stationList.find(message);
    if (station == null)
    {
      System.err.println("Station not found: " + message);
      return;
    }
    
    // Return a new list of trains of the current station
    List<String> result = new LinkedList<>();
    List<Train> trains = station.getTrains();
    for (Train train : trains)
    {
      StringBuilder sb = new StringBuilder();
      
      // Append train info
      sb.append(new SimpleDateFormat("HH:mm").format(train.getTime()));
      sb.append(" ").append(train.getOperator());
      sb.append(" ").append(train.getType());
      sb.append(" ").append(train.getNumber());
      
      // Append route
      for (ctjava.station.Station route : train.getRoute())
        sb.append(" \"").append(route.getName()).append("\"");
      sb.append(" \"").append(train.getDestination().getName()).append("\"");
      
      // Append the atom
      result.add(sb.toString());
    }
      
    this.outlet(0,result.toArray(new String[0]));
    this.outlet(1,trains.size());
  }
}
