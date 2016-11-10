package ctjava.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequest
{
  // Static variables
  public static final int MAX_TIMEOUTS = 3;
  
  // Variables
  private final String url;
  private final Map<String,String> parameters;
  
  // Constructor
  public HttpRequest(String url) throws HttpException
  {
    this.url = url;
    this.parameters = new LinkedHashMap<>();
  }
  
  // Management
  public String getUrl()
  {
    return this.url;
  }
  public String getParameter(String key)
  {
    return this.parameters.get(key);
  }
  public void setParameter(String key, String value)
  {
    try
    {
      this.parameters.put(key,URLEncoder.encode(value,"UTF-8"));
    }
    catch (UnsupportedEncodingException ex)
    {
      this.parameters.put(key,value);
    }
  }
  public URL toUrl()
  {
    try
    {
      StringBuilder sb = new StringBuilder(this.url);
      if (this.parameters != null && this.parameters.entrySet().size() > 0)
      {
        sb.append("?");
        boolean first = true;
        for (Map.Entry<String,String> parameter : this.parameters.entrySet())
        {
          if (first)
            first = false;
          else
            sb.append("&");
          sb.append(parameter.getKey()).append("=").append(parameter.getValue());
        }
      }
      return new URL(sb.toString());
    }
    catch (MalformedURLException ex)
    {
      return null;
    }
  }
  
  // Request
  public InputStream request() throws HttpException
  {
    return this.request(0);
  }
  public InputStream request(int timeouts) throws HttpException
  {
    try
    {
      URL url = this.toUrl();
      System.out.println("  Requesting " + url.toString() + " (" + timeouts + " timeouts)");
      
      HttpURLConnection request = (HttpURLConnection)url.openConnection();
      request.setRequestMethod("GET");

      int status = request.getResponseCode();
      if (status != 200)
      {
        if (++timeouts < MAX_TIMEOUTS)
          return this.request(timeouts);
        else
          throw new HttpException("Unsuccesful request with status " + status + " (" + url.toString() + ")");
      }

      return request.getInputStream();
    }
    catch (IOException ex)
    {
      throw new HttpException("I/O error: " + ex.getMessage(),ex);
    }
  }
  
  // Object
  @Override public String toString()
  {
    return this.toUrl().toString();
  }
}
