package com.sg.rha.model.power;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;
import com.sg.rha.superherosightings.model.hero.Hero;

import java.util.List;

public interface PowerDao {

        Power getPowerById(int id) throws ElementNotFoundException;
        List<Power> getAllPowers();
        Power addPower(Power power);
        void updatePower(Power power);
        void deletePowerById(int id);

}
