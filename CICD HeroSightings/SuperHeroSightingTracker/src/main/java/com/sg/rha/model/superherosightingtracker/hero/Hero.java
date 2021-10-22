package com.sg.rha.model.superherosightingtracker.hero;


import com.sg.rha.superherosightings.model.organization.Organization;
import com.sg.rha.superherosightings.model.power.Power;
import com.sg.rha.superherosightings.model.sighting.Sighting;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class Hero {

    private int heroId;
    private String name;
    private String description;

    private List<Power> powers;
    private List<Organization> organizations;
    private List<Sighting> sightings;
}
