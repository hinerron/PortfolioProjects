package com.sg.rha.model.organization;

import com.sg.rha.superherosightings.model.hero.Hero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class Organization {

    private int organizationId;
    private String name;
    private String contact;
    private String description;

    private List<Hero> heroesOfOrg;
}
