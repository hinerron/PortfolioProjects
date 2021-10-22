package location;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;
import com.sg.rha.superherosightings.model.hero.Hero;
import com.sg.rha.superherosightings.model.hero.HeroDao;
import com.sg.rha.superherosightings.model.organization.Organization;
import com.sg.rha.superherosightings.model.organization.OrganizationDao;
import com.sg.rha.superherosightings.model.power.Power;
import com.sg.rha.superherosightings.model.power.PowerDao;
import com.sg.rha.superherosightings.model.sighting.Sighting;
import com.sg.rha.superherosightings.model.sighting.SightingDao;
import com.sg.rha.model.location.Location;
import com.sg.rha.model.location.LocationDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class LocationDaoDBTest {

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
    void testAddGetLocationById() throws ElementNotFoundException {
        Location location = new Location();
        location.setName("Test Location Name");
        location.setDescription("Description of Location");
        location = locationDao.addLocation(location);

        Location locationFromDao = locationDao.getLocationById(location.getLocationId());
        assertEquals(location, locationFromDao);
    }

    @Test
    void testBadInputAddGetLocationById() throws ElementNotFoundException {
        Location location = new Location();
        location.setName("Test Location Name");
        location.setDescription("Description of Location");
        location = locationDao.addLocation(location);

            assertThrows(ElementNotFoundException.class, () -> {
            Location locationFromDao = locationDao.getLocationById(500);
        });

    }

    @Test
    void testGetAllLocations() {
        Location location = new Location();
        location.setName("Test Location Name");
        location.setDescription("Description of Location");
        location = locationDao.addLocation(location);

        Location location2 = new Location();
        location2.setName("Test Another Location Name");
        location2.setDescription("Description of Another Location");
        location2 = locationDao.addLocation(location);

        List<Location> locationsList = locationDao.getAllLocations();

        assertEquals(2, locationsList.size());
        assertTrue(locationsList.contains(location));
        assertTrue(locationsList.contains(location2));
    }

    @Test
    void testUpdateLocation() throws ElementNotFoundException {
        Location location = new Location();
        location.setName("Test Location Name");
        location.setDescription("Description of Location");
        location = locationDao.addLocation(location);

        Location locationFromDao = locationDao.getLocationById(location.getLocationId());
        assertEquals(location, locationFromDao);

        location.setName("Different Location Name Than First");
        locationDao.updateLocation(location);

        assertNotEquals(location, locationFromDao);

    }

    @Test
    void testDeleteLocationById() {
        Location location = new Location();
        location.setName("Test Location Name");
        location.setDescription("Description of Location");
        location = locationDao.addLocation(location);

        Location location2 = new Location();
        location2.setName("Test Location Name 2");
        location2.setDescription("Description 2");
        location2 = locationDao.addLocation(location2);

        locationDao.deleteLocationById(location.getLocationId());

        assertEquals(locationDao.getAllLocations().size(), 1);
    }

    @Test
    void locationsByHero() {
        List<Hero> heroes = new ArrayList<>();
        List<Hero> heroes2 = new ArrayList<>();

        Location location = new Location();
        location.setName("Test Location Name");
        location.setDescription("Description of Location");
        location = locationDao.addLocation(location);

        Location location2 = new Location();
        location2.setName("Test Another Location Name");
        location2.setDescription("Description of Another Location");
        location2 = locationDao.addLocation(location);

        Location location3 = new Location();
        location3.setName("Test A third Location Name");
        location3.setDescription("Description of A third Location");
        location3 = locationDao.addLocation(location);

        Hero hero1 = new Hero();
        hero1.setName("test name");
        hero1.setDescription("test description");
        hero1 = heroDao.addHero(hero1);

        heroes.add(hero1);

        Hero hero2 = new Hero();
        hero2.setName("test 2nd name");
        hero2.setDescription("test 2nd description");
        hero2 = heroDao.addHero(hero2);

        heroes2.add(hero2);

        Sighting sighting = new Sighting();
        sighting.setSightingDate(LocalDate.parse("1983-11-04"));
        sighting.setDescription("Description of Sighting");
        sighting.setLocationId(location.getLocationId());
        sighting.setHeroes(heroes);
        sighting = sightingDao.addSighting(sighting);

        Sighting sighting2 = new Sighting();
        sighting2.setSightingDate(LocalDate.parse("1983-11-05"));
        sighting2.setDescription("Description of Another Sighting");
        sighting2.setLocationId(location.getLocationId());
        sighting2.setHeroes(heroes);
        sighting2 = sightingDao.addSighting(sighting2);

        Sighting sighting3 = new Sighting();
        sighting3.setSightingDate(LocalDate.parse("1983-11-05"));
        sighting3.setDescription("Description of A third Sighting");
        sighting3.setLocationId(location2.getLocationId());
        sighting3.setHeroes(heroes2);
        sighting3 = sightingDao.addSighting(sighting3);

        List<Location> locationsForHero1 = locationDao.locationsByHero(hero1.getHeroId());

        assertEquals(2, locationsForHero1.size());
    }
}