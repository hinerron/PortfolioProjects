package com.sg.rha.superherosightingtracker.controller;

import com.sg.rha.model.superherosightingtracker.hero.HeroDao;
import com.sg.rha.model.location.LocationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sg.rha.model.organization.Organization;
import com.sg.rha.model.organization.OrganizationDao;
import com.sg.rha.model.power.PowerDao;
import com.sg.rha.model.sighting.SightingDao;

import java.util.List;

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

    @GetMapping("organizations")
    public String displayOrganizations(Model model) {
        List<Organization> organizations = organizationDao.getAllOrganizations();
        model.addAttribute("organizations", organizations);
        return "organizations";
    }
}
