package ctjava;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonValue;
import ctjava.util.http.HttpException;
import ctjava.util.http.HttpRequest;
import ctjava.util.json.JsonException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class TrainRequest
{
  // Load stations
  public static List<Train> load(String code) throws CtjavaException
  {
    try
    {
      HttpRequest http = new HttpRequest("http://api.rijdendetreinen.nl/v2/json/vertrektijden");
      http.setParameter("station",code);
      
      InputStream in = http.request();
      InputStreamReader reader = new InputStreamReader(in);
      
      JsonValue json = Json.parse(reader);
      return new TrainResponseDeserializer().deserialize(json);
    }
    catch (HttpException ex)
    {
      throw new CtjavaException("Error in request: " + ex.getMessage(),ex);
    }
    catch (JsonException ex)
    {
      throw new CtjavaException("Error in response: " + ex.getMessage(),ex);
    }
    catch (IOException ex)
    {
      throw new CtjavaException("Input/output error: " + ex.getMessage(),ex);
    }
  }
}
