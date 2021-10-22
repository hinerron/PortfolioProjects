package com.sg.rha.superherotracker.model.organization;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;
import com.sg.rha.superherotracker.model.hero.Hero;

import java.util.List;

public interface OrganizationDao {

    Organization getOrganizationById(int id) throws ElementNotFoundException;
    List<Organization> getAllOrganizations();
    Organization addOrganization(Organization organization);
    void updateOrganization(Organization organization);
    void deleteOrganizationById(int id);

    public List<Organization> getAllOrganizationsForHero(int heroId);
    public List<Hero> getAllHeroesByOrganization(int id);
}
