package ctjava.train;

public final class TrainListException extends Exception
{
  public TrainListException(String message) 
  { 
    super(message); 
  }
  public TrainListException(String message, Throwable cause) 
  { 
    super(message,cause); 
  }
}
