package com.sg.rha.superherotracker.model.power;

import com.sg.rha.superherotracker.model.hero.Hero;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
public class Power {
    private int powerId;
    @NotBlank(message = "name cannot be blank.")
    @Size(max = 45, message = "Name cannot be longer than 45 characters.")
    private String name;
    @Size(max = 250, message = "Description cannot be longer than 250 characters.")
    private String description;

    private List<Hero> heroes;
}

