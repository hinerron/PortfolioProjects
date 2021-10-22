package com.sg.rha.model.organization;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;

import java.util.List;

public interface OrganizationDao {

        Organization getOrganizationById(int id) throws ElementNotFoundException;
        List<Organization> getAllOrganizations();
        Organization addOrganization(Organization organization);
        void updateOrganization(Organization organization);
        void deleteOrganizationById(int id);

        public List<Organization> getAllOrganizationsForHero(int heroId);

}
