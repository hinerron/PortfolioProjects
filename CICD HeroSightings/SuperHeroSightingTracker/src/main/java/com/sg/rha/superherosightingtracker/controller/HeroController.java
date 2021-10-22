package com.sg.rha.superherosightingtracker.controller;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;
import com.sg.rha.model.superherosightingtracker.hero.Hero;
import com.sg.rha.model.superherosightingtracker.hero.HeroDao;
import com.sg.rha.model.location.Location;
import com.sg.rha.model.location.LocationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.sg.rha.model.organization.Organization;
import com.sg.rha.model.organization.OrganizationDao;
import com.sg.rha.model.power.Power;
import com.sg.rha.model.power.PowerDao;
import com.sg.rha.model.sighting.Sighting;
import com.sg.rha.model.sighting.SightingDao;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

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

        for(String powerId : powerIds)
            powers.add(powerDao.getPowerById(Integer.parseInt(powerId)));

        hero.setPowers(powers);

        for(String organizationId : organizationIds)
            organizations.add(organizationDao.getOrganizationById(Integer.parseInt(organizationId)));

        hero.setOrganizations(organizations);

        heroDao.addHero(hero);

        return "redirect:/heroes";
    }

    @GetMapping("deleteHero")
    public String deleteHero(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        heroDao.deleteHeroById(id);

        return "redirect:/heroes";
    }

    @PostMapping("editHero")
    public String performEditHero(HttpServletRequest request) throws ElementNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        Hero hero = heroDao.getHeroById(id);

        hero.setName(request.getParameter("firstName"));
        hero.setDescription(request.getParameter("lastName"));

        heroDao.updateHero(hero);

        return "redirect:/heroes";
    }

    @GetMapping("heroDetail")
    public String heroDetail(Integer id, Model model) throws ElementNotFoundException {
        Hero hero = heroDao.getHeroById(id);
        model.addAttribute("hero", hero);
        return "heroDetails";
    }
}
