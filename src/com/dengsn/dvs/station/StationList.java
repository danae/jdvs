package com.dengsn.dvs.station;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.dengsn.dvs.util.ListUtils;
import com.dengsn.dvs.util.http.HttpException;
import com.dengsn.dvs.util.http.HttpRequest;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StationList
{
  // Static instance
  private static StationList instance;
  
  // Get static instance
  public static StationList getInstance()
  {
    return StationList.instance;
  }
  
  // Variables
  private final Map<String,List<String>> names = new LinkedHashMap<>();
  private final Map<String,Station> queries = new LinkedHashMap<>();
  
  // Constructor
  public StationList() throws StationListException
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
        
        // Insert the new entry
        this.insert(code,name);
      }
      
      // Add Centraal/Centrum synonyms
      Pattern centraal = Pattern.compile("(\\w+) Centraal");
      Pattern centrum = Pattern.compile("(\\w+) Centrum");
      for (Map.Entry<String,List<String>> entry : this.names.entrySet())
      {
        // Check for centraal
        for (String name : entry.getValue())
        {
          Matcher centraalMatcher = centraal.matcher(name);
          if (centraalMatcher.matches())
          {
            entry.getValue().add(centraalMatcher.group(1) + " CS");
            entry.getValue().add(centraalMatcher.group(1) + " C.");
            entry.getValue().add(centraalMatcher.group(1) + " C");
            break;
          }
          
          Matcher centrumMatcher = centrum.matcher(name);
          if (centrumMatcher.matches())
          {
            entry.getValue().add(centrumMatcher.group(1) + " C.");
            entry.getValue().add(centrumMatcher.group(1) + " C");
            break;
          }
        }
      }
      
      // Set the static instance
      StationList.instance = this;
    }
    catch (HttpException ex)
    {
      throw new StationListException("Could not load stations because the API request failed",ex);
    }
    catch (UnsupportedOperationException | NullPointerException ex)
    {
      throw new StationListException("Could not load stations because the API response was invalid or incomplete ",ex);
    }
  }
  
  // Insert a station with names
  public StationList insert(String code, String name)
  {
    // Create a new name list if not in the map
    if (!this.names.containsKey(code))
      this.names.put(code,new LinkedList<>());

    // Add names and synonyms
    this.names.get(code).add(name);
    
    return this;
  }
  public StationList insert(String code, List<String> names)
  {
    for (String name : names)
      this.insert(code,name);
    
    return this;
  }
  public StationList insert(Station station)
  {
    this.insert(station.getCode(),station.getName());
    this.insert(station.getCode(),station.getSynonyms());
    
    return this;
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
        Station station = new Station()
          .withCode(nameEntry.getKey())
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
        Station station = new Station()
          .withCode(code)
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
    return station == null ? new Station().withName(query) : station;
  }
  
  // Returns a random station in the Netherlands
  public Station random()
  {
    List<String> codes = new LinkedList<>(this.names.keySet());
    return this.find(codes.get(new Random().nextInt(codes.size())));
  }
}
