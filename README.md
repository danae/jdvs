# Jdvs

[![GitHub release](https://img.shields.io/github/release/dengsn/Jdvs.svg)](https://github.com/dengsn/Ctjava/releases) [![Github Releases](https://img.shields.io/github/downloads/dengsn/Jdvs/latest/total.svg)](https://github.com/dengsn/Ctjava/releases)

**Jdvs** is a simple Java library to fetch Dutch train stations and their departing trains using the [Rijden De Treinen API](https://github.com/geertw/rdt-infoplus-dvs).

## Download and installation

The latest release of Jdvs is available under the [releases](https://github.com/dengsn/Jdvs/releases) tab in the [GitHub repository](https://github.com/dengsn/Jdvs). Jdvs depends on the following external libraries, make sure you download and include them in your project as well:

- [minimal-json](https://github.com/ralfstx/minimal-json) >= `0.9.4`

## Usage

The Jdvs library exists of several classes to minimize the effort of fetching stations and trains. These are:

- `StationList` to store all station names as well as to find stations using this information.
- `Station` for storing the code and names of a station and the departing trains.
- `Train` to store information of a departing train, such as time, platform and route.

In the `examples` folder you can find a basic example on how to use the library.





## Documentation

This documentation only includes the functions in the public API. For a full documentation, please refer to the source code, where comments are placed along with each function.

### StationList class

The `StationList` class is used to find stations vy query and caches the station names and queries internally. You must always use this class to fetch stations from the API.

To create a new station list, use the `public StationList() throws StationListException` constructor.

#### Find a station

    public Station find(String query)

The `find` function returns a `Station` object based on a query. The query is tested against the code and all the names of the station. The **`query`** parameter defines the query to search for.  If a station is found, this is returned as a `Station` object, otherwise the function returns `null`.

    public Station findOrFake(String query)

The `findOrFake` function always returns a `Station` object, whether the `find` function finds a station or not. If no station is found, the query is filled in in the `name` property of the `Station` object.

#### Select a random station

    public Station random()

The `random` function returns a random `Station` object representing one of the stations in the Netherlands.

### Station class

The `Station` class contains information about a Dutch railway station, such as its name, code and synonyms. The `Station` class has the following properties:

- `public String getCode()`  returns the internal station code.
- `public String getName()` returns the name of the station
- `public List<String> getSynonyms()` returns the synonyms of the station.
- `public List<Trains> getTrains() throws TrainListException` returns the departing trains at this station.

To create a new station, use the `public Station()` constructor.

### Train class

The `Train` class contains information about a departing train, such as time, platform and route. The `Train` class has the following methods to get its info:

 - `public String getNumber()` returns the internal train number.
 - `public String getType()` returns the type of the train, like Intercity or Sprinter.
 - `public String getOperator` returns the operator of this line, like NS or Arriva.
 - `public Station getDestination()` returns the destination station.
 - `public List<Station> getRoute()` returns the via-stations of this line.
 - `public ZonedDateTime getTime()` returns the departure time.
 - `public Duration getDelay()` returns the departure delay in minutes.
 - `public ZonedDateTime getActualTime()` returns the departure time including the delay.
 - `public String getPlatform()` returns the departure platform.
 - `public List<String> getInfo()` returns obligaroty information about the departure (`opmerkingen` in RDT-API).
 - `public List<string> getTips()` returns optional information about the line (`tips` in RDT-API).
 
To create a new train, use the `public Train()` constructor.

## Exceptions

The functions described above throw exceptions if something goes wrong. A message and cause is always provided. THe following exceptions can be thrown:

 - `StationListException` if the `StationList` class constructor fails to create a new object.
 - `TrainListException` if the `Station.getTrains()` function fails to fetch the departing trains.
