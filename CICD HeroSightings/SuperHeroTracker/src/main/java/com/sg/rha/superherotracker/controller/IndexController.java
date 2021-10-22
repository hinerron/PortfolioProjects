package com.sg.rha.superherotracker.controller;

import com.sg.rha.superherotracker.model.hero.Hero;
import com.sg.rha.superherotracker.model.hero.HeroDao;
import com.sg.rha.superherotracker.model.location.Location;
import com.sg.rha.superherotracker.model.location.LocationDao;
import com.sg.rha.superherotracker.model.organization.Organization;
import com.sg.rha.superherotracker.model.organization.OrganizationDao;
import com.sg.rha.superherotracker.model.power.Power;
import com.sg.rha.superherotracker.model.power.PowerDao;
import com.sg.rha.superherotracker.model.sighting.Sighting;
import com.sg.rha.superherotracker.model.sighting.SightingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class IndexController {

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

    Set<ConstraintViolation<Hero>> violations = new HashSet<>();

    @GetMapping("index")
    public String displaySightings(Model model) {
        List<Sighting> sightings = sightingDao.getAllSightings();
        model.addAttribute("sightings", sightings);
        return "index";
    }


}
