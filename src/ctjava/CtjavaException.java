package ctjava;

public class CtjavaException extends Exception
{
  public CtjavaException(String message) 
  { 
    super(message); 
  }
  public CtjavaException(String message, Throwable cause) 
  { 
    super(message,cause); 
  }
}
