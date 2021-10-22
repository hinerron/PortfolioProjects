package com.sg.rha.superherotracker.model.location;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
public class Location {

//    @NotNull(message = "Id cannot be blank.")
    private int locationId;
    @NotBlank(message = "Name cannot be blank.")
    @Size(message = "Name cannot be more than 30 characters.")
    private String name;
//    @NotNull(message = "Latitude cannot be blank.")
    @Digits(integer = 3, fraction = 6, message = "latitude must have no more the 3 integral digits and 6 fractional digits.")
    @Min(value = -90,  message = "Please use a coordinate system of -90 to 90 degrees")
    @Max(value = 90,   message = "Please use a coordinate system of -90 to 90 degrees")
    private double latitude;
    @Min(value = -180, message = "Please use a coordinate system of -180 to 180 degrees")
    @Max(value = 180,  message = "Please use a coordinate system of -180 to 180 degrees")
    @Digits(integer = 3, fraction = 6, message = "Longitude must have no more the 3 integral digits and 6 fractional digits.")
    private double longitude;
    @Size(max = 250, message = "Address cannot be more than 250 characters.")
    private String address;
    @Size(max = 250, message = "Description cannot be more than 250 characters.")
    private String description;
}

