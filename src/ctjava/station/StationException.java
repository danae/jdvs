package ctjava.station;

public final class StationException extends Exception
{
  public StationException(String message) 
  { 
    super(message); 
  }
  public StationException(String message, Throwable cause) 
  { 
    super(message,cause); 
  }
}
