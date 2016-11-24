package com.dengsn.dvs.station;

public final class StationListException extends Exception
{
  public StationListException(String message) 
  { 
    super(message); 
  }
  public StationListException(String message, Throwable cause) 
  { 
    super(message,cause); 
  }
}
