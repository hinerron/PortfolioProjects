package com.sg.rha.model.location;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Location {

    private int locationId;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String description;
}
