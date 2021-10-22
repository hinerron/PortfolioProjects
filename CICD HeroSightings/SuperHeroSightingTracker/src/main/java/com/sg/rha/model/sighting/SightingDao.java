package com.sg.rha.model.sighting;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;
import com.sg.rha.superherosightings.model.hero.Hero;
import com.sg.rha.superherosightings.model.sighting.Sighting;

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
