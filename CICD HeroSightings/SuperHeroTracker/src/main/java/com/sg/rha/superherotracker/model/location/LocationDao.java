package com.sg.rha.superherotracker.model.location;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;

import java.util.List;

public interface LocationDao {

    Location getLocationById(int id) throws ElementNotFoundException;
    List<Location> getAllLocations();
    Location addLocation(Location location);
    void updateLocation(Location location);
    void deleteLocationById(int id);

    public List<Location> locationsByHero(int heroId);
}
