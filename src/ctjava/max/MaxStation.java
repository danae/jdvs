package ctjava.max;

import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.max.MaxObject;
import ctjava.station.Station;
import ctjava.station.StationList;
import ctjava.station.StationListException;
import ctjava.train.Train;
import ctjava.train.TrainListException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

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
      this.declareOutlets(new int[]{DataTypes.LIST,DataTypes.INT});
      this.setOutletAssist(new String[]{"List of trains departing as strings","Number of trains departing"});
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
      // Return a new list of trains of the current station
      List<String> result = new LinkedList<>();
      List<Train> trains = this.station.getTrains();
      trains.stream()
        .map(this::format)
        .forEach(result::add);
      
      // Output
      this.outlet(0,result.toArray(new String[0]));
      this.outlet(1,trains.size());
    }
    catch (TrainListException ex)
    {
      MaxObject.getErrorStream().println(ex.getMessage());
    }
  }
  
  // Format a train to return
  protected String format(Train train)
  {
    StringBuilder sb = new StringBuilder();
      
    // Append train info
    sb.append(train.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    sb.append(" ").append(train.getOperator());
    sb.append(" ").append(train.getType());
    sb.append(" ").append(train.getNumber());
      
    // Append route
    for (ctjava.station.Station route : train.getRoute())
      sb.append(" \"").append(route.getName()).append("\"");
    sb.append(" \"").append(train.getDestination().getName()).append("\"");
    
    return sb.toString();
  }
}
