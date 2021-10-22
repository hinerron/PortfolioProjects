package com.sg.rha.superherotracker.controller;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;
import com.sg.rha.superherotracker.model.hero.Hero;
import com.sg.rha.superherotracker.model.hero.HeroDao;
import com.sg.rha.superherotracker.model.location.Location;
import com.sg.rha.superherotracker.model.location.LocationDao;
import com.sg.rha.superherotracker.model.organization.Organization;
import com.sg.rha.superherotracker.model.organization.OrganizationDao;
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
public class OrganizationController {

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

    Set<ConstraintViolation<Organization>> violations = new HashSet<>();

    @GetMapping("organizations")
    public String displayOrganizations(Model model) {
        List<Organization> organizations = organizationDao.getAllOrganizations();
        List<Hero> heroes = heroDao.getAllHeroes();
        model.addAttribute("organizations", organizations);
        model.addAttribute("heroes", heroes);
        model.addAttribute("errors", violations);
        return "organizations";
    }

    @PostMapping("addOrganization")
    public String addOrganization(Organization organization, HttpServletRequest request) throws ElementNotFoundException {
        String organizationId = request.getParameter("organizationId");
        String name = request.getParameter("name");
        String contact = request.getParameter("contact");
        String description = request.getParameter("description");

        String[] heroIds = request.getParameterValues("heroId");
        List<Hero> heroList = new ArrayList<>();

        if (heroIds != null) {
            for (String heroId : heroIds) {
                heroList.add(heroDao.getHeroById(Integer.parseInt(heroId)));
            }
        }

        organization.setName(name);
        organization.setContact(contact);
        organization.setDescription(description);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(organization);

        if(violations.isEmpty()) {
            organizationDao.addOrganization(organization);
        }

        return "redirect:/organizations";
    }

    @GetMapping("deleteOrganization")
    public String deleteOrganization(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        organizationDao.deleteOrganizationById(id);

        return "redirect:/organizations";
    }

    @GetMapping("editOrganization")
    public String editOrganization(Integer id, Model model) throws ElementNotFoundException {
        Organization organization = organizationDao.getOrganizationById(id);
        List<Hero> heroes = heroDao.getAllHeroes();
        model.addAttribute("organization", organization);
        model.addAttribute("heroes", heroes);

        return "editOrganization";
    }

    @PostMapping("organizationEdit")
    public String performEditOrganization(Organization organization, HttpServletRequest request) throws ElementNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        organization.setOrganizationId(id);

        organizationDao.updateOrganization(organization);

        return "redirect:/organizations";

    }

    @GetMapping("organizationDetails")
    public String organizationDetail(Integer id, Model model) throws ElementNotFoundException {
        Organization organization = organizationDao.getOrganizationById(id);
        model.addAttribute("organization", organization);
        return "organizationDetails";
    }
}
