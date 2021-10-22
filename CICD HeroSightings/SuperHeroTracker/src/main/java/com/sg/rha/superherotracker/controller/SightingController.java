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
import com.sg.rha.superherotracker.model.sighting.Sighting;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Autowired
    Validator validator;

    Set<ConstraintViolation<Sighting>> violations = new HashSet<>();

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = sightingDao.getAllSightings();
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();

        model.addAttribute("sightings", sightings);
        model.addAttribute("heroes",heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("errors", violations);

        return "sightings";
    }

    @PostMapping("addSighting")
    public String addSighting(Sighting sighting, HttpServletRequest request) throws ElementNotFoundException {
        String sightingId = request.getParameter("sightingId");
        String dateString = request.getParameter("date");
        String[] heroIdStrings = request.getParameterValues("heroId");
        List<Hero> heroes = new ArrayList<>();

        if(heroIdStrings != null) {
            for (String heroIdString : heroIdStrings) {
                int heroId = Integer.parseInt(heroIdString);
                heroes.add(heroDao.getHeroById(heroId));
            }
            sighting.setHeroes(heroes);
        }

        //Get location from Google Maps API.  Pass to back-end as lat/long, check to make sure it doesn't already exist
        //if exists, add location id to sighting, otherwise first create location.

        String description = request.getParameter("description");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);

        sighting.setSightingDate(date);
        sighting.setDescription(description);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(sighting);

        if(violations.isEmpty()) {
            sightingDao.addSighting(sighting);
        }
        return "redirect:/sightings";
    }

    @GetMapping("deleteSighting")
    public String deleteSighting(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        sightingDao.deleteSightingById(id);

        return "redirect:/sightings";
    }

    @GetMapping("sightingDetails")
    public String sightingDetail(Integer id, Model model) throws ElementNotFoundException {
        Sighting sighting = sightingDao.getSightingById(id);
        model.addAttribute("sighting", sighting);
        return "sightingDetails";
    }

    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model) throws ElementNotFoundException {
        Sighting sighting = sightingDao.getSightingById(id);
        List<Power> powers = powerDao.getAllPowers();
        List<Organization> organizations = organizationDao.getAllOrganizations();
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("sighting", sighting);
        model.addAttribute("powers", powers);
        model.addAttribute("organizations", organizations);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        return "editSighting";
    }

    @PostMapping("sightingEdit")
    public String performEditSighting(Sighting sighting, HttpServletRequest request) throws ElementNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        sighting.setSightingId(id);

        sightingDao.updateSighting(sighting);

        return "redirect:/sightings";

    }
}
