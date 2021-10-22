package power;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;
import com.sg.rha.model.power.Power;
import com.sg.rha.model.power.PowerDao;
import com.sg.rha.superherosightings.model.hero.Hero;
import com.sg.rha.superherosightings.model.hero.HeroDao;
import com.sg.rha.superherosightings.model.location.Location;
import com.sg.rha.superherosightings.model.location.LocationDao;
import com.sg.rha.superherosightings.model.organization.Organization;
import com.sg.rha.superherosightings.model.organization.OrganizationDao;
import com.sg.rha.superherosightings.model.sighting.Sighting;
import com.sg.rha.superherosightings.model.sighting.SightingDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PowerDaoDBTest {

    @Autowired
    HeroDao heroDao;
    @Autowired
    LocationDao locationDao;
    @Autowired
    OrganizationDao organizationDao;
    @Autowired
    SightingDao sightingDao;
    @Autowired
    PowerDao powerDao;

    @BeforeEach
    void setUp() {
        List<Hero> heroes = heroDao.getAllHeroes();
        for(Hero hero : heroes) {
            heroDao.deleteHeroById(hero.getHeroId());
        }

        List<Location> locations = locationDao.getAllLocations();
        for(Location location : locations) {
            locationDao.deleteLocationById(location.getLocationId());
        }

        List<Organization> organizations = organizationDao.getAllOrganizations();
        for(Organization organization : organizations) {
            organizationDao.deleteOrganizationById(organization.getOrganizationId());
        }

        List<Power> powers = powerDao.getAllPowers();
        for(Power power : powers) {
            powerDao.deletePowerById(power.getPowerId());
        }

        List<Sighting> sightings = sightingDao.getAllSightings();
        for(Sighting sighting : sightings) {
            sightingDao.deleteSightingById(sighting.getSightingId());
        }
    }

    @Test
    void testAddGetPowerById() throws ElementNotFoundException {
        Power power = new Power();
        power.setName("Test Power Name");
        power.setDescription("Description of Power");
        power = powerDao.addPower(power);

        Power powerFromDao = powerDao.getPowerById(power.getPowerId());
        assertEquals(power, powerFromDao);
    }

    @Test
    void testBadInputAddGetPowerById() {
        Power power = new Power();
        power.setName("Test Power Name");
        power.setDescription("Description of Power");
        power = powerDao.addPower(power);

        assertThrows(ElementNotFoundException.class, () -> {
            Power powerFromDao = powerDao.getPowerById(500);
        });
    }

    @Test
    void testGetAllPowers() {
        Power power = new Power();
        power.setName("Test Power Name");
        power.setDescription("Description of Power");
        power = powerDao.addPower(power);

        Power power2 = new Power();
        power2.setName("Test Another Power Name");
        power2.setDescription("Description of Another Power");
        power2 = powerDao.addPower(power);

        List<Power> poweresList = powerDao.getAllPowers();

        assertEquals(2, poweresList.size());
        assertTrue(poweresList.contains(power));
        assertTrue(poweresList.contains(power2));
    }

    @Test
    void testUpdatePower() throws ElementNotFoundException {
        Power power = new Power();
        power.setName("Test Power Name");
        power.setDescription("Description of Power");
        power = powerDao.addPower(power);

        Power powerFromDao = powerDao.getPowerById(power.getPowerId());
        assertEquals(power, powerFromDao);

        power.setName("Different Power Name Than First");
        powerDao.updatePower(power);

        assertNotEquals(power, powerFromDao);
    }

    @Test
    void testDeletePowerById() {

        Power power1 = new Power();
        power1.setDescription("Testing Lasers");
        power1.setName("EyeLaze");
        powerDao.addPower(power1);

        Power power2 = new Power();
        power2.setDescription("Test Description");
        power2.setName("Freeze Ray Test");
        powerDao.addPower(power2);

        powerDao.deletePowerById(power1.getPowerId());

        assertEquals(1, powerDao.getAllPowers().size());
    }
}