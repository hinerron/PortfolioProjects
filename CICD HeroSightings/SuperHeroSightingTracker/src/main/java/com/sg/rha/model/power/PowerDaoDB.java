package com.sg.rha.model.power;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;
import com.sg.rha.superherosightings.model.sighting.Sighting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PowerDaoDB implements PowerDao {

    @Autowired
    JdbcTemplate jdbc;

    //****************************************************************************************************
    //  Basic CRUD Methods
    //****************************************************************************************************

    @Override
    public Power getPowerById(int id) throws ElementNotFoundException {
        try {
            final String GET_POWER_BY_ID = "SELECT * FROM Power WHERE powerId = ?";
            return jdbc.queryForObject(GET_POWER_BY_ID, new PowerMapper(), id);
        } catch(DataAccessException ex) {
            throw new ElementNotFoundException("Power Id Doesn't Exist");
        }
    }

    @Override
    public List<Power> getAllPowers() {
        final String GET_ALL_POWERS = "SELECT * FROM Power";

        return jdbc.query(GET_ALL_POWERS, new PowerMapper());
    }

    @Override
    @Transactional
    public Power addPower(Power power) {
        final String INSERT_POWER = "INSERT INTO Power(name, description) "
                + "VALUES(?,?)";

        jdbc.update(INSERT_POWER,
                power.getName(),
                power.getDescription());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        power.setPowerId(newId);

        return power;
    }

    @Override
    public void updatePower(Power power) {
        final String UPDATE_POWER = "UPDATE Power SET name = ?, description = ? WHERE powerId = ?";

        jdbc.update(UPDATE_POWER,
                power.getName(),
                power.getDescription(),
                power.getPowerId());
    }

    @Override
    public void deletePowerById(int id) {
        final String DELETE_HERO_POWER = "DELETE FROM HeroPower WHERE powerId = ?";
        jdbc.update(DELETE_HERO_POWER, id);

        final String DELETE_POWER = "DELETE FROM Power WHERE powerId = ?";
        jdbc.update(DELETE_POWER, id);
    }

    public static final class PowerMapper implements RowMapper<Power> {

        @Override
        public Power mapRow(ResultSet rs, int index) throws SQLException {
            Power power = new Power();
            power.setPowerId(rs.getInt("PowerId"));
            power.setName(rs.getString("name"));
            power.setDescription(rs.getString("description"));

            return power;
        }
    }
}
