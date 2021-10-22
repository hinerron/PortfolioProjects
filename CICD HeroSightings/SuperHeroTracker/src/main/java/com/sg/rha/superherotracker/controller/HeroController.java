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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import javax.validation.metadata.BeanDescriptor;


@Controller
public class HeroController {

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


    @GetMapping("heroes")
    public String displayHeroes(Model model) {
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Power> powers = powerDao.getAllPowers();
        List<Organization> organizations = organizationDao.getAllOrganizations();
        List<Sighting> sightings = sightingDao.getAllSightings();
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("heroes", heroes);
        model.addAttribute("powers", powers);
        model.addAttribute("organizations", organizations);
        model.addAttribute("sightings", sightings);
        model.addAttribute("locations", locations);
        model.addAttribute("errors", violations);
        return "heroes";
    }

    @PostMapping("addHero")
    public String addHero(Hero hero, HttpServletRequest request) throws ElementNotFoundException {
        String heroId = request.getParameter("heroId");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String[] powerIds = request.getParameterValues("powerId");
        List<Power> powers = new ArrayList<>();
        String[] organizationIds = request.getParameterValues("organizationId");
        List<Organization> organizations = new ArrayList<>();

        hero.setName(name);
        hero.setDescription(description);

        if(powerIds != null) {
            for (String powerId : powerIds) {
                powers.add(powerDao.getPowerById(Integer.parseInt(powerId)));
            }
            hero.setPowers(powers);
        }

        if(organizationIds != null) {
            for (String organizationId : organizationIds) {
                organizations.add(organizationDao.getOrganizationById(Integer.parseInt(organizationId)));
            }
            hero.setOrganizations(organizations);
        }

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(hero);

        if(violations.isEmpty()) {
            heroDao.addHero(hero);
        }
        return "redirect:/heroes";
    }

    @GetMapping("deleteHero")
    public String deleteHero(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        heroDao.deleteHeroById(id);

        return "redirect:/heroes";
    }

    @GetMapping("heroDetails")
    public String heroDetail(Integer id, Model model) throws ElementNotFoundException {
        Hero hero = heroDao.getHeroById(id);
        model.addAttribute("hero", hero);
        return "heroDetails";
    }

    @GetMapping("editHero")
    public String editHero(Integer id, Model model) throws ElementNotFoundException {
        Hero hero = heroDao.getHeroById(id);
        List<Power> powers = powerDao.getAllPowers();
        List<Organization> organizations = organizationDao.getAllOrganizations();
        model.addAttribute("hero", hero);
        model.addAttribute("powers", powers);
        model.addAttribute("organizations", organizations);
        return "editHero";
    }

    @PostMapping("heroEdit")
    public String performEditHero(@Valid Hero hero, HttpServletRequest request, BindingResult result) throws ElementNotFoundException {
        int heroId = Integer.parseInt(request.getParameter("id"));
        String[] powerIds = request.getParameterValues("powerId");
        List<Power>  powers = new ArrayList<>();
        String[] orgIds = request.getParameterValues("organizationId");
        List<Organization> organizations = new ArrayList<>();

        hero.setHeroId(heroId);

        if(powerIds != null) {
            for (String powerId : powerIds) {
                powers.add(powerDao.getPowerById(Integer.parseInt(powerId)));
            }
            hero.setPowers(powers);
        }

        if(orgIds != null) {
            for (String organizationId : orgIds) {
                organizations.add(organizationDao.getOrganizationById(Integer.parseInt(organizationId)));
            }
            hero.setOrganizations(organizations);
        }
        if(result.hasErrors()) {
            return "editHero";
        }
        heroDao.updateHero(hero);

        return "redirect:/heroes";
    }
}
