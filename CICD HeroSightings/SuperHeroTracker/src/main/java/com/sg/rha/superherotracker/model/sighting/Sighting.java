package com.sg.rha.superherotracker.model.sighting;

import com.sg.rha.superherotracker.model.hero.Hero;
import com.sg.rha.superherotracker.model.location.Location;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class Sighting {
    private int sightingId;
    @Min(value = 1, message = "Must select a location.")
    private int locationId;
//    @PastOrPresent
    private LocalDate sightingDate;
    @Size(max = 250, message = "Description cannot be more than 250 characters.")
    private String description;
    private Location location;
    @NotEmpty(message = "You must select at least one hero.")
    private List<Hero> heroes;
}
