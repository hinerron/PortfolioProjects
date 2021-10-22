package com.sg.rha.model.power;

import com.sg.rha.superherosightings.model.hero.Hero;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class Power {

    private int powerId;
    private String name;
    private String description;

    private List<Hero> heroes;
}
