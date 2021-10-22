package com.sg.rha.superherosightingtracker.controller;

import com.sg.rha.model.superherosightingtracker.hero.HeroDao;
import com.sg.rha.model.location.LocationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sg.rha.model.organization.OrganizationDao;
import com.sg.rha.model.power.PowerDao;
import com.sg.rha.model.sighting.Sighting;
import com.sg.rha.model.sighting.SightingDao;

import java.util.List;

@Controller
public class SightingController {

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

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = sightingDao.getAllSightings();
        model.addAttribute("sightings", sightings);
        return "sigthings";
    }
}
