package com.dengsn.dvs.station;

import com.dengsn.dvs.train.TrainListException;
import com.dengsn.dvs.train.Train;
import com.dengsn.dvs.train.TrainList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class Station
{
  // Variables
  private String code;
  private String name;
  private List<String> synonyms = new LinkedList<>();
  
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
  public List<Train> getTrains() throws TrainListException
  {
    return this.code == null ? Collections.emptyList() : new TrainList(this.code).getList();
  }
}
