package com.sg.rha.superherosightingtracker.controller;

import com.sg.rha.model.superherosightingtracker.hero.HeroDao;
import com.sg.rha.model.location.Location;
import com.sg.rha.model.location.LocationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sg.rha.model.organization.OrganizationDao;
import com.sg.rha.model.power.PowerDao;
import com.sg.rha.model.sighting.SightingDao;

import java.util.List;

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

    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations";
    }
}
