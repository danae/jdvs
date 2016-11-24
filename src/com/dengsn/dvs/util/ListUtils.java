package com.dengsn.dvs.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class ListUtils
{
  // Get the first element of a list
  public static <T> T first(List<T> list)
  {
    return list.isEmpty() ? null : list.get(0);
  }
  
  // Get a list without the first element
  public static <T> List<T> rest(List<T> list)
  {
    if (list.size() <= 1)
      return Collections.emptyList();
    
    List<T> restList = new LinkedList<>(list);
    list.remove(0);
    return restList;
  }
}
