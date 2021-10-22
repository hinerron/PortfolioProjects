package com.sg.rha.superherotracker.model.hero;

import com.sg.rha.superherotracker.model.organization.Organization;
import com.sg.rha.superherotracker.model.power.Power;
import com.sg.rha.superherotracker.model.sighting.Sighting;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;
import org.hibernate.validator.internal.util.Version;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class Hero {
//    @NotNull(message = "Id cannot be blank")
    private int heroId;
    @NotBlank(message = "Name cannot be blank.")
    @Size(max = 30, message = "Name cannot be more than 30 characters.")
    private String name;
    @Size(max = 250, message = "Description cannot be more than 250 characters.")
    private String description;

    private List<Power> powers;
    private List<Organization> organizations;
    private List<Sighting> sightings;

}
