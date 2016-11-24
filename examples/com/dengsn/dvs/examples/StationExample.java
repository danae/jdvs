package com.dengsn.dvs.examples;

import com.dengsn.dvs.station.Station;
import com.dengsn.dvs.station.StationList;
import com.dengsn.dvs.station.StationListException;
import com.dengsn.dvs.train.Train;
import com.dengsn.dvs.train.TrainListException;

public class StationExample
{
  // Main method
  public static void main(String[] args)
  {
    try
    {
      // Create a station list to fetch a station from
      StationList stationList = new StationList();
      
      // Now search for a station
      Station station = stationList.find("ht");
      
      // Print the station info
      System.out.println("This is station " + station.getName());
      System.out.println("Synonyms for this station are " + station.getSynonyms().stream().reduce((a, b) -> a + ", " + b).orElse("none"));
      System.out.println("The code for this station is " + station.getCode());
      System.out.println();
      
      // Print all departing trains
      for (Train train : station.getTrains())
        System.out.println(train);
    }
    catch (StationListException | TrainListException ex)
    {
      System.out.println(ex.getMessage());
      System.exit(-1);
    }
  }
}
