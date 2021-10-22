package com.sg.rha.superherotracker.controller;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;
import com.sg.rha.superherotracker.model.hero.Hero;
import com.sg.rha.superherotracker.model.hero.HeroDao;
import com.sg.rha.superherotracker.model.location.Location;
import com.sg.rha.superherotracker.model.location.LocationDao;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class PowerController {

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

    Set<ConstraintViolation<Power>> violations = new HashSet<>();

    @GetMapping("powers")
    public String displayPowers(Model model) {
        List<Power> powers = powerDao.getAllPowers();
        model.addAttribute("powers", powers);
        model.addAttribute("errors", violations);
        return "powers";
    }

    @PostMapping("addPower")
    public String addPower(Power power, HttpServletRequest request) throws ElementNotFoundException {
        String powerId = request.getParameter("powerId");
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        power.setName(name);
        power.setDescription(description);

        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(power);

        if(violations.isEmpty()) {
            powerDao.addPower(power);
        }

        return "redirect:/powers";
    }

    @GetMapping("deletePower")
    public String deletePower(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        powerDao.deletePowerById(id);

        return "redirect:/powers";
    }

    @GetMapping("editPower")
    public String editPower(Integer id, Model model) throws ElementNotFoundException {
        Power power = powerDao.getPowerById(id);
        List<Hero> heroes = heroDao.getAllHeroes();
        model.addAttribute("power", power);
        model.addAttribute("heroes", heroes);

        return "editPower";
    }

    @PostMapping("powerEdit")
    public String performEditPower(Power power, HttpServletRequest request) throws ElementNotFoundException {
        int id = Integer.parseInt(request.getParameter("id"));
        power.setPowerId(id);

        powerDao.updatePower(power);

        return "redirect:/powers";
    }


    @GetMapping("powerDetails")
    public String powerDetail(Integer id, Model model) throws ElementNotFoundException {
        Power power = powerDao.getPowerById(id);
        model.addAttribute("power", power);
        return "powerDetails";
    }
}
