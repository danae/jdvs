package ctjava.util.json;

import com.eclipsesource.json.JsonValue;

@FunctionalInterface
public interface JsonDeserializer<T>
{
  public T deserialize(JsonValue json) throws JsonException;
}
