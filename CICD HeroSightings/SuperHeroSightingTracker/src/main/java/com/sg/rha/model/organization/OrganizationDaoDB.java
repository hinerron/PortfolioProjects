package com.sg.rha.model.organization;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;
import com.sg.rha.superherosightings.model.hero.Hero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrganizationDaoDB implements OrganizationDao {

    @Autowired
    JdbcTemplate jdbc;

    //****************************************************************************************************
    //  Basic CRUD Methods
    //****************************************************************************************************

    @Override
    public Organization getOrganizationById(int id) throws ElementNotFoundException {
        try {
            final String GET_ORGANIZATION_BY_ID = "SELECT * FROM Organization WHERE organizationId = ?";
            return jdbc.queryForObject(GET_ORGANIZATION_BY_ID, new OrganizationMapper(), id);
        } catch(DataAccessException ex) {
            throw new ElementNotFoundException("Organization Id Doesn't Exist", ex);
        }
    }

    @Override
    public List<Organization> getAllOrganizations() {
        final String GET_ALL_ORGANIZATIONS = "SELECT * FROM Organization";

        return jdbc.query(GET_ALL_ORGANIZATIONS, new OrganizationMapper());
    }

    @Override
    public Organization addOrganization(Organization organization) {
        final String INSERT_ORGANIZATION = "INSERT INTO Organization(name, contact, description) " +
                "VALUES(?,?,?)";
        final String INSERT_HERO_ORG = "INSERT INTO HeroOrganization(heroId, organizationID) VALUES (?,?)";

        jdbc.update(INSERT_ORGANIZATION,
                organization.getName(),
                organization.getContact(),
                organization.getDescription());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setOrganizationId(newId);

        if (organization.getHeroesOfOrg() != null) {
            for (Hero hero : organization.getHeroesOfOrg()) {

                jdbc.update(INSERT_HERO_ORG,
                        hero.getHeroId(),
                        organization.getOrganizationId());
            }
        }

        return organization;
    }

    @Override
    public void updateOrganization(Organization organization) {
        final String UPDATE_ORGANIZATION = "UPDATE Organization SET name = ?, contact = ?, description = ? WHERE organizationId = ?";

        jdbc.update(UPDATE_ORGANIZATION,
                organization.getName(),
                organization.getContact(),
                organization.getDescription(),
                organization.getOrganizationId());
    }

    @Override
    public void deleteOrganizationById(int id) {
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM HeroOrganization WHERE organizationId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, id);

        final String DELETE_ORGANIZATION = "DELETE FROM Organization WHERE organizationId = ?";
        jdbc.update(DELETE_ORGANIZATION, id);

    }

    //****************************************************************************************************
    //  Advanced CRUD Methods
    //****************************************************************************************************

    @Override
    public List<Organization> getAllOrganizationsForHero(int heroId) {
        List<Organization> organizationsByHero = new ArrayList<>();

        final String GET_ALL_ORGANIZATIONS_BY_HERO = "SELECT * FROM HeroOrganization ho "
                + "JOIN Organization o ON ho.organizationId = o.organizationId "
                + "WHERE heroId = ?";

        organizationsByHero = jdbc.query(GET_ALL_ORGANIZATIONS_BY_HERO, new OrganizationMapper(), heroId);

        return organizationsByHero;
    }

    //****************************************************************************************************
    //  Mapper
    //****************************************************************************************************
    
    public static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int index) throws SQLException {
            Organization organization = new Organization();
            organization.setOrganizationId(rs.getInt("organizationId"));
            organization.setName(rs.getString("name"));
            organization.setContact(rs.getString("contact"));
            organization.setDescription(rs.getString("description"));

            return organization;
        }
    }
}
