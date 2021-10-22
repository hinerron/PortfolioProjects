package com.sg.rha.superherotracker.model.sighting;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface SightingDao {

    Sighting getSightingById(int id) throws ElementNotFoundException;
    List<Sighting> getAllSightings();
    Sighting addSighting(Sighting sighting);
    void updateSighting(Sighting sighting);
    void deleteSightingById(int id);
    List<Sighting> getSightingsByDate(LocalDate date) throws ElementNotFoundException;
}
