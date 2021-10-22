package com.sg.rha.superherotracker.model.sighting;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;
import com.sg.rha.superherotracker.model.hero.Hero;
import com.sg.rha.superherotracker.model.hero.HeroDaoDB;
import com.sg.rha.superherotracker.model.location.Location;
import com.sg.rha.superherotracker.model.location.LocationDaoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SightingDaoDB implements SightingDao{

    @Autowired
    JdbcTemplate jdbc;

    //****************************************************************************************************
    //  Basic CRUD Methods
    //****************************************************************************************************

    @Override
    public Sighting getSightingById(int id) throws ElementNotFoundException {
        try {
            final String GET_SIGHTING_BY_ID = "SELECT * FROM Sighting WHERE sightingId = ?";
            Sighting sighting = jdbc.queryForObject(GET_SIGHTING_BY_ID, new SightingMapper(), id);
            sighting.setLocation(getLocationOfSightingById(id));
            sighting.setHeroes(getAllHeroesBySightingId(id));
            return sighting;
        } catch(DataAccessException ex) {
            throw new ElementNotFoundException("Sighting Id Does Not Exist", ex);
        }
    }

    private Location getLocationOfSightingById(int id) {
        final  String GET_LOCATION_OF_SIGHTING = "SELECT * FROM Location l " +
                "JOIN sighting s ON s.locationId = l.locationId " +
                "WHERE sightingId = ?";

        return jdbc.queryForObject(GET_LOCATION_OF_SIGHTING, new LocationDaoDB.LocationMapper(), id);
    }

    private List<Hero> getAllHeroesBySightingId(int id) {
        final String GET_HEROES_BY_SIGHTING = "SELECT * FROM HeroSighting hs " +
                "JOIN Hero h ON hs.heroId = h.heroId " +
                "WHERE sightingId = ?";

        return jdbc.query(GET_HEROES_BY_SIGHTING, new HeroDaoDB.HeroMapper(), id);
    }

    @Override
    public List<Sighting> getAllSightings() {
        final String GET_ALL_SIGHTINGS = "SELECT * FROM Sighting " +
                "ORDER BY sightingDate DESC";

        List<Sighting> sightings =  jdbc.query(GET_ALL_SIGHTINGS, new SightingMapper());

        for (Sighting sighting : sightings) {
            List<Hero> heroesOfSighting = getHeroesBySighting(sighting);
            sighting.setHeroes(heroesOfSighting);
        }

        for (Sighting sighting : sightings) {
            sighting.setLocation(getLocationOfSighting(sighting));
        }

        return sightings;
    }

    private List<Hero> getHeroesBySighting(Sighting sighting) {
        final String GET_HEROES_BY_SIGHTING = "SELECT * FROM Hero h " +
                "JOIN HeroSighting hs USING (heroId) " +
                "JOIN Sighting s USING (sightingId) " +
                "WHERE sightingId = ?";
        int id = sighting.getSightingId();

        return jdbc.query(GET_HEROES_BY_SIGHTING, new HeroDaoDB.HeroMapper(), id);
    }

    private Location getLocationOfSighting(Sighting sighting) {
        final String GET_LOCATIONS_BY_SIGHTING = "SELECT * FROM Location l " +
                "JOIN Sighting USING(locationId)" +
                "WHERE sightingId = ?";

        int id = sighting.getSightingId();

        return jdbc.queryForObject(GET_LOCATIONS_BY_SIGHTING, new LocationDaoDB.LocationMapper(), id);
    }

    @Override
    @Transactional
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO Sighting(locationId, sightingDate, description)"
                + "VALUES(?,?,?)";

        jdbc.update(INSERT_SIGHTING,
                sighting.getLocationId(),
                sighting.getSightingDate(),
                sighting.getDescription());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setSightingId(newId);

        insertHeroSighting(sighting);

        return sighting;
    }

    private void insertHeroSighting(Sighting sighting) {
        final String INSERT_HERO_SIGHTING = "INSERT INTO HeroSighting(heroId, sightingId) VALUES(?,?)";

        for (Hero hero : sighting.getHeroes())
            jdbc.update(INSERT_HERO_SIGHTING, hero.getHeroId(), sighting.getSightingId());
    }

    @Override
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE Sighting SET locationId = ?, sightingDate = ?, description = ? WHERE sightingId = ?";

        jdbc.update(UPDATE_SIGHTING,
                sighting.getLocationId(),
                sighting.getSightingDate(),
                sighting.getDescription(),
                sighting.getSightingId());
    }

    @Override
    public void deleteSightingById(int id) {

        final String DELETE_HERO_SIGHTING = "DELETE FROM HeroSighting WHERE sightingId = ?";
        jdbc.update(DELETE_HERO_SIGHTING, id);

        final String DELETE_SIGHTING = "DELETE FROM Sighting WHERE sightingId = ?";
        jdbc.update(DELETE_SIGHTING, id);
    }

    //****************************************************************************************************
    //  Advanced CRUD Methods
    //****************************************************************************************************

    @Override
    public List<Sighting> getSightingsByDate(LocalDate date) throws ElementNotFoundException {

        List<Sighting> sightings = new ArrayList<>();

        final String GET_ALL_SIGHTINGS_BY_DATE = "SELECT * FROM Sighting s "
                + "JOIN HeroSighting hs ON s.sightingId = hs.sightingId "
                + "JOIN Hero h ON hs.heroId = h.heroId "
                + "JOIN Location l ON s.locationId = l.locationId "
                + "WHERE sightingDate = ?";

        try {
            sightings = jdbc.query(GET_ALL_SIGHTINGS_BY_DATE, new SightingMapper(), date);
        } catch (DateTimeParseException e) {
            throw new ElementNotFoundException("Improper Date Format", e);
        }

        if (sightings.size() == 0) {
            throw new ElementNotFoundException("No Sightings For This Date Are Found");
        } else return sightings;
    }

    //****************************************************************************************************
    //  Mapper
    //****************************************************************************************************

    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setSightingId(rs.getInt("sightingId"));
            sighting.setLocationId(rs.getInt("locationId"));
            sighting.setSightingDate(rs.getDate("sightingDate").toLocalDate());
            sighting.setDescription(rs.getString("description"));

            return sighting;
        }
    }
}
