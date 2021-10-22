package com.sg.rha.model.sighting;

import com.sg.rha.superherosightings.model.hero.Hero;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
public class Sighting {

    private int sightingId;
    private int locationId;
    private LocalDate sightingDate;
    private String description;

    private List<Hero> heroes;
}
