package ctjava.station;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import ctjava.util.ListUtils;
import ctjava.util.http.HttpException;
import ctjava.util.http.HttpRequest;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class StationList
{
  // Variables
  private final Map<String,List<String>> names = new LinkedHashMap<>();
  private final Map<String,RealStation> queries = new LinkedHashMap<>();
  
  // Constructor
  public StationList() throws StationException
  {
    try
    {
      // Make request to the RDT-API
      HttpRequest http = new HttpRequest("http://api.rijdendetreinen.nl/v1/json/stations");
      JsonArray jsonArray = http.jsonRequest().asArray();
      
      // Parse the stations
      for (JsonValue jsonValue : jsonArray)
      {
        // Get a station object
        JsonObject jsonStation = jsonValue.asObject();
        
        // Get station data
        String code = jsonStation.get("code").asString();
        String name = jsonStation.get("value").asString();
        String country = jsonStation.get("land").asString();
        
        // Check country
        if (!country.equals("NL"))
          continue;
        
        // Create a new name list if not in the map
        if (!this.names.containsKey(code))
          this.names.put(code,new LinkedList<>());

        // Add names and synonyms
        this.names.get(code).add(name);
      }
      
      // Debug message
      System.out.println(this.names.size() + " stations loaded");
    }
    catch (HttpException | UnsupportedOperationException | NullPointerException ex)
    {
      throw new StationException("Could not load stations: " + ex.getMessage(),ex);
    }
  }
  
  // Returns a found station given a code or name query, or null if nothing found
  public Station find(String query)
  {
    // Check if already cached
    if (this.queries.containsKey(query))
      return this.queries.get(query);
 
    // Check for names
    for (Map.Entry<String,List<String>> nameEntry : this.names.entrySet())
    {
      if (nameEntry.getValue().contains(query))
      {
        RealStation station = new RealStation(nameEntry.getKey())
          .withName(ListUtils.first(nameEntry.getValue()))
          .withSynonyms(ListUtils.rest(nameEntry.getValue()));
        this.queries.put(query,station);
        return station;
      }
    }
  
    // Check for code
    for (String code : this.names.keySet())
    {
      if (code.equals(query))
      {
        RealStation station = new RealStation(code)
          .withName(ListUtils.first(this.names.get(code)))
          .withSynonyms(ListUtils.rest(this.names.get(code)));
        this.queries.put(query,station);
        return station;
      }
    }
  
    // No match, return null
    this.queries.put(query,null);
    return null;
  }
  
  // Returns a found station given a code or name query, or a placeholder is nothing found
  public Station findOrFake(String query)
  {
    Station station = this.find(query);
    return station == null ? new Station.Fake(query) : station;
  }
}
