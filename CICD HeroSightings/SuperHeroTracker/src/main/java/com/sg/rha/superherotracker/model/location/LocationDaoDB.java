package com.sg.rha.superherotracker.model.location;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;
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
public class LocationDaoDB implements LocationDao {

    @Autowired
    JdbcTemplate jdbc;

    //****************************************************************************************************
    //  Basic CRUD Methods
    //****************************************************************************************************

    @Override
    @Transactional
    public Location getLocationById(int id) throws ElementNotFoundException {
        try {
            final String GET_LOCATION_BY_ID = "SELECT * FROM Location WHERE LocationId = ?";
            return jdbc.queryForObject(GET_LOCATION_BY_ID, new LocationMapper(), id);
        } catch(DataAccessException ex) {
            throw new ElementNotFoundException("Location Id Doesn't Exist");
        }

    }

    @Override
    public List<Location> getAllLocations() {
        final String GET_ALL_LOCATIONS = "SELECT * FROM Location";

        return jdbc.query(GET_ALL_LOCATIONS, new LocationMapper());
    }

    @Override
    @Transactional
    public Location addLocation(Location location) {
        final String INSERT_LOCATION = "INSERT INTO Location(name, latitude, longitude, address, description) "
                + "VALUES(?,?,?,?,?)";

        jdbc.update(INSERT_LOCATION,
                location.getName(),
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress(),
                location.getDescription());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setLocationId(newId);

        return location;
    }

    @Override
    public void updateLocation(Location location) {
        final String UPDATE_LOCATION = "UPDATE Location SET name = ?, latitude = ?, longitude = ?, address = ?, " +
                "description = ? WHERE locationId = ?";
        jdbc.update(UPDATE_LOCATION,
                location.getName(),
                location.getLatitude(),
                location.getLongitude(),
                location.getAddress(),
                location.getDescription(),
                location.getLocationId());
    }

    @Override
    @Transactional
    public void deleteLocationById(int id) {

        final String DELETE_HERO_SIGHTING = "DELETE hs.* FROM HeroSighting hs "
                + "JOIN Sighting s ON hs.sightingId = s.sightingId WHERE s.locationId = ?";
        jdbc.update(DELETE_HERO_SIGHTING, id);

        final String DELETE_SIGHTING = "DELETE FROM Sighting WHERE locationId = ?";
        jdbc.update(DELETE_SIGHTING, id);

        final String DELETE_LOCATION = "DELETE FROM Location WHERE locationId = ?";
        jdbc.update(DELETE_LOCATION, id);
    }

    //****************************************************************************************************
    //  Advanced CRUD Methods
    //****************************************************************************************************

    @Override
    public List<Location> locationsByHero(int heroId) {
        List<Location> locationsByHero = new ArrayList<>();

        final String GET_LOCATIONS_BY_HERO = "SELECT * FROM HeroSighting hs "
                + "JOIN Hero h ON hs.heroId = h.heroId "
                + "JOIN Sighting s ON hs.sightingId = s.sightingId "
                + "JOIN Location l ON s.locationId = l.locationId "
                + "WHERE hs.heroId = ? ";

        locationsByHero = jdbc.query(GET_LOCATIONS_BY_HERO, new LocationMapper(), heroId);

        return locationsByHero;
    }

    //****************************************************************************************************
    //  Mapper
    //****************************************************************************************************
    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int index) throws SQLException {
            Location location = new Location();
            location.setLocationId(rs.getInt("locationId"));
            location.setName(rs.getString("name"));
            location.setLatitude(rs.getDouble("latitude"));
            location.setLongitude(rs.getDouble("longitude"));
            location.setAddress(rs.getString("address"));
            location.setDescription(rs.getString("description"));

            return location;
        }
    }
}
