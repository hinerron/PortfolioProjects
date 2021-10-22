package com.sg.rha.superherotracker.controller;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;
import com.sg.rha.superherotracker.model.hero.Hero;
import com.sg.rha.superherotracker.model.hero.HeroDao;
import com.sg.rha.superherotracker.model.location.Location;
import com.sg.rha.superherotracker.model.location.LocationDao;
import com.sg.rha.superherotracker.model.organization.Organization;
import com.sg.rha.superherotracker.model.organization.OrganizationDao;
import com.sg.rha.superherotracker.model.power.Power;
import com.sg.rha.superherotracker.model.power.PowerDao;
import com.sg.rha.superherotracker.model.sighting.SightingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class LocationController {

    @Autowired
    HeroDao heroDao;
    @Autowired
    LocationDao locationDao;
    @Autowired
    OrganizationDao organizationDao;
    @Autowired
    PowerDao powerDao;
    @Autowired
    SightingDao sightingDao;
    @Autowired
    Validator validator;

    Set<ConstraintViolation<Location>> violations = new HashSet<>();


    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationDao.getAllLocations();
        List<Hero> heroes = heroDao.getAllHeroes();
        model.addAttribute("locations", locations);
        model.addAttribute("heroes", heroes);
        model.addAttribute("errors", violations);
        return "locations";
    }

    @PostMapping("addLocation")
    public String addLocation(Location location, HttpServletRequest request) throws ElementNotFoundException {
        String locationId = request.getParameter("locationId");
        String name = request.getParameter("name");
        String latitudeString = request.getParameter("latitude");
        String longitudeString = request.getParameter("longitude");
        Double latitude = Double.parseDouble(latitudeString);
        Double longitude = Double.parseDouble(longitudeString);
        String address = request.getParameter("address");
        String description = request.getParameter("description");

        location.setName(name);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAddress(address);
        location.setDescription(description);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(location);

        if(violations.isEmpty()) {
            locationDao.addLocation(location);
        }

        return "redirect:/locations";
    }

    @GetMapping("deleteLocation")
    public String deleteLocation(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        locationDao.deleteLocationById(id);

        return "redirect:/locations";
    }

    @GetMapping("editLocation")
    public String editLocation(Integer id, Model model) throws ElementNotFoundException {
        Location location = locationDao.getLocationById(id);
        model.addAttribute("location", location);

        return "editLocation";
    }

    @PostMapping("locationEdit")
    public String editLocation(Location location, HttpServletRequest request) throws ElementNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        location.setLocationId(id);

        locationDao.updateLocation(location);

        return "redirect:/locations";
    }

    @GetMapping("locationDetails")
    public String locationDetail(Integer id, Model model) throws ElementNotFoundException {
        Location location = locationDao.getLocationById(id);
        model.addAttribute("location", location);
        return "locationDetails";
    }
}
