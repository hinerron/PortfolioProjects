package com.sg.rha.superherotracker.model.organization;

import com.sg.rha.superherotracker.model.hero.Hero;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class Organization {
//    @NotNull(message = "ID cannot be blank.")
    private int organizationId;
    @NotBlank(message = "Name cannot be blank.")
    @Size(max = 45, message = "Name cannot be longer than 30 characters.")
    private String name;
    @NotBlank(message = "Contact cannot be blank.")
    @Size(max = 45, message = "Contact cannot be longer than 45 characters.")
    private String contact;
    @Size(max = 250, message = "Description cannot be longer than 250 characters.")
    private String description;

    private List<Hero> heroesOfOrg;
}