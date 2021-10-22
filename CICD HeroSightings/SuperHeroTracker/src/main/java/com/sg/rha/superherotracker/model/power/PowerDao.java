package com.sg.rha.superherotracker.model.power;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;
import com.sg.rha.superherotracker.model.hero.Hero;

import java.util.List;

public interface PowerDao {

    Power getPowerById(int id) throws ElementNotFoundException;
    List<Power> getAllPowers();
    Power addPower(Power power);
    void updatePower(Power power);
    void deletePowerById(int id);

    List<Hero> getAllHeroesByPower(int id);
}
