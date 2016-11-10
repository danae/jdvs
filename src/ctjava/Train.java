package ctjava;

import ctjava.station.Station;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Train
{
  // Variables
  private String number;
  private String type;
  private String operator;
  private Station destination;
  private List<Station> route = new LinkedList<>();
  private Date time;
  private Duration delay;
  private String platform;
  private List<String> info = new LinkedList<>();
  private List<String> infoOptional = new LinkedList<>();
  
  // Management
  public String getNumber()
  {
    return this.number;
  }
  public Train withNumber(String number)
  {
    this.number = number;
    return this;
  }
  public String getType()
  {
    return this.type;
  }
  public Train withType(String type)
  {
    this.type = type;
    return this;
  }
  public String getOperator()
  {
    return this.operator;
  }
  public Train withOperator(String operator)
  {
    this.operator = operator;
    return this;
  }
  public Station getDestination()
  {
    return this.destination;
  }
  public Train withDestination(Station destination)
  {
    this.destination = destination;
    return this;
  }
  public List<Station> getRoute()
  {
    return this.route;
  }
  public Train withRoute(List<Station> route)
  {
    this.route = route;
    return this;
  }
  public Date getTime()
  {
    return this.time;
  }
  public Train withTime(Date time)
  {
    this.time = time;
    return this;
  }
  public Duration getDelay()
  {
    return this.delay;
  }
  public Train withDelay(Duration delay)
  {
    this.delay = delay;
    return this;
  }
  public String getPlatform()
  {
    return this.platform;
  }
  public Train withPlatform(String platform)
  {
    this.platform = platform;
    return this;
  }
  public List<String> getInfo()
  {
    return this.info;
  }
  public Train withInfo(List<String> info)
  {
    this.info = info;
    return this;
  }
  public List<String> getInfoOptional()
  {
    return this.infoOptional;
  }
  public Train withInfoOptional(List<String> infoOptional)
  {
    this.infoOptional = infoOptional;
    return this;
  }
}
