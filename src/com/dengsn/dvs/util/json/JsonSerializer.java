package com.dengsn.dvs.util.json;

import com.eclipsesource.json.JsonValue;

@FunctionalInterface
public interface JsonSerializer<T>
{
  public JsonValue serialize(T object);
}
