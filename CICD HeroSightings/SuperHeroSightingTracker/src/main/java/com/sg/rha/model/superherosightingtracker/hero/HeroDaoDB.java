package com.sg.rha.model.superherosightingtracker.hero;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;
import com.sg.rha.superherosightings.model.organization.Organization;
import com.sg.rha.superherosightings.model.organization.OrganizationDaoDB;
import com.sg.rha.superherosightings.model.power.Power;
import com.sg.rha.superherosightings.model.sighting.Sighting;
import com.sg.rha.superherosightings.model.sighting.SightingDaoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HeroDaoDB implements HeroDao {

    @Autowired
    JdbcTemplate jdbc;

    //****************************************************************************************************
    //  Basic CRUD Methods
    //****************************************************************************************************

    @Override
    public Hero getHeroById(int id) throws ElementNotFoundException {
        try {
            final String GET_HERO_BY_ID = "SELECT * FROM Hero WHERE heroId = ?";
            Hero hero = jdbc.queryForObject(GET_HERO_BY_ID, new HeroMapper(), id);
            return hero;
            } catch(DataAccessException ex) {
            throw  new ElementNotFoundException("Hero Id Does Not Exist", ex);
        }
    }

    @Override
    public List<Hero> getAllHeroes() {
        final String GET_ALL_HEROES = "SELECT * FROM Hero";
        return jdbc.query(GET_ALL_HEROES, new HeroMapper());
    }

    @Override
    @Transactional
    public Hero addHero(Hero hero) {
        final String INSERT_HERO = "INSERT INTO Hero(name, description) VALUES(?,?)";

        jdbc.update(INSERT_HERO,
                hero.getName(),
                hero.getDescription());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero.setHeroId(newId);

        insertHeroOrg(hero);

        insertHeroPower(hero);

        return hero;
    }

    private void insertHeroOrg(Hero hero) {
        final String INSERT_HERO_ORG = "INSERT INTO HeroOrganization(heroId, organizationId) VALUES(?,?)";

        if (hero.getOrganizations() != null) {
            for(Organization org : hero.getOrganizations()) {
                jdbc.update(INSERT_HERO_ORG,
                        hero.getHeroId(),
                        org.getOrganizationId());
            }
        }
    }

    private void insertHeroPower(Hero hero) {
        final String INSERT_HERO_POWER = "INSERT INTO HeroPower(heroId, powerId) VALUES(?,?)";

        if (hero.getPowers() != null) {
            for(Power power : hero.getPowers()) {
                jdbc.update(INSERT_HERO_POWER,
                        hero.getHeroId(),
                        power.getPowerId());
            }
        }
    }

    @Override
    public void updateHero(Hero hero) {
        final String UPDATE_HERO = "UPDATE Hero SET name = ?, description = ? WHERE heroId = ?";

        jdbc.update(UPDATE_HERO,
                hero.getName(),
                hero.getDescription(),
                hero.getHeroId());
    }

    @Override
    @Transactional
    public void deleteHeroById(int id) {

            final String DELETE_HERO_ORGANIZATION = "DELETE ho.* FROM HeroOrganization ho " +
                    "JOIN Organization o ON ho.organizationId = o.organizationId WHERE heroId = ?";
            jdbc.update(DELETE_HERO_ORGANIZATION, id);

            final String DELETE_HERO_POWER = "DELETE hp.* FROM HeroPower hp " +
                    "JOIN Power p ON hp.powerId = p.powerId WHERE heroId = ?";
            jdbc.update(DELETE_HERO_POWER, id);

            final String DELETE_HERO_SIGHTING = "DELETE FROM HeroSighting WHERE heroId = ?";
            jdbc.update(DELETE_HERO_SIGHTING, id);

            final String DELETE_HERO = "DELETE FROM Hero WHERE heroId = ?";
            jdbc.update(DELETE_HERO, id);


        deleteSightingsAssociatedOnlyWithHero(id);
    }

    private void deleteSightingsAssociatedOnlyWithHero(int heroId) {
        //before deleting the sightings, generate ArrayList of all sightings for hero
        String GET_ALL_SIGHTINGS_FOR_HERO = "SELECT * FROM HeroSighting hs "
                + "JOIN Sighting s ON s.sightingId = hs.sightingId WHERE heroId = ?";

        List<Sighting> allSightingsForHero;

        allSightingsForHero = jdbc.query(GET_ALL_SIGHTINGS_FOR_HERO, new SightingDaoDB.SightingMapper(), heroId);

        //for each sighting check to see if any other heroes are associated with it
        //if not then delete the sighting
        for (Sighting sighting : allSightingsForHero) {
            List<Hero> heroesAssociatedWithSighting;
            int sightingId = sighting.getSightingId();

            final String GET_HEROES_BY_SIGHTING = "SELECT * FROM HeroSighting hs "
                    + "JOIN Hero h ON h.heroId = hs.heroId WHERE sightingId = ?";

            heroesAssociatedWithSighting = jdbc.query(GET_HEROES_BY_SIGHTING, new HeroMapper(), sightingId);

            final String DELETE_SIGHTING = "DELETE FROM Sighting WHERE sightingId = ?";

            if (heroesAssociatedWithSighting.size() == 0) {
                jdbc.update(DELETE_SIGHTING, sightingId);
            }
        }
    }

    //****************************************************************************************************
    //  Advanced CRUD Methods
    //****************************************************************************************************

    @Override
    public List<Hero> GetAllHeroesSightedAtLocation(int locationId) {
        List<Hero> heroesByLocation = new ArrayList<>();

        final String GET_ALL_HEROES_BY_LOCATION = "Select * FROM HeroSighting hs "
            + "JOIN Hero h ON hs.heroId = h.heroId "
            + "JOIN Sighting s ON hs.sightingId = s.sightingId "
            + "JOIN Location l ON s.locationId = l.locationId "
            + "WHERE s.locationId = ?";

        heroesByLocation = jdbc.query(GET_ALL_HEROES_BY_LOCATION, new HeroMapper(), locationId);

        return heroesByLocation;
    }
    @Override
    public List<Hero> getAllHeroesByOrganization(int orgId) {
        List<Hero> heroesByOrganization = new ArrayList<>();

        final String GET_ALL_HEROES_BY_ORG = "SELECT ho.* FROM HeroOrganization ho "
                + "JOIN Organization o ON ho.organizationId = o.organizationId "
                + "WHERE heroId = ?";

        heroesByOrganization = jdbc.query(GET_ALL_HEROES_BY_ORG, new HeroMapper(), orgId);

        return heroesByOrganization;
    }

    //****************************************************************************************************
    //  Mapper
    //****************************************************************************************************

    public static final class HeroMapper implements RowMapper<Hero> {

        @Override
        public Hero mapRow(ResultSet rs, int index) throws SQLException {
            Hero hero = new Hero();
            hero.setHeroId(rs.getInt("heroId"));
            hero.setName(rs.getString("name"));
            hero.setDescription(rs.getString("description"));

            return hero;
        }
    }


}
