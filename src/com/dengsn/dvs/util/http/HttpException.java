package com.dengsn.dvs.util.http;

public class HttpException extends Exception
{
  // Constructor
  public HttpException(String message) 
  { 
    super(message); 
  }
  public HttpException(String message, Throwable cause) 
  { 
    super(message,cause); 
  }
}
