package hero;

import com.sg.rha.superherosightingtracker.exceptions.ElementNotFoundException;
import com.sg.rha.superherosightings.model.location.Location;
import com.sg.rha.superherosightings.model.location.LocationDao;
import com.sg.rha.superherosightings.model.organization.Organization;
import com.sg.rha.superherosightings.model.organization.OrganizationDao;
import com.sg.rha.superherosightings.model.power.Power;
import com.sg.rha.superherosightings.model.power.PowerDao;
import com.sg.rha.superherosightings.model.sighting.Sighting;
import com.sg.rha.superherosightings.model.sighting.SightingDao;
import com.sg.rha.model.superherosightingtracker.hero.Hero;
import com.sg.rha.model.superherosightingtracker.hero.HeroDao;
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
class HeroDaoDBTest {

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
    void testAddGetHeroById() throws ElementNotFoundException {
        Hero hero = new Hero();
        hero.setName("Test Hero Name");
        hero.setDescription("Description of Hero");
        hero = heroDao.addHero(hero);

        Hero heroFromDao = heroDao.getHeroById(hero.getHeroId());
        assertEquals(hero, heroFromDao);
    }

    @Test
    void testBadInputAddGetHeroById() throws ElementNotFoundException {
        Hero hero = new Hero();
        hero.setName("Test Hero Name");
        hero.setDescription("Description of Hero");
        hero = heroDao.addHero(hero);

        assertThrows(ElementNotFoundException.class, () -> {
            Hero hero1 = new Hero();
            hero1 = heroDao.getHeroById(100);
        });

    }

    @Test
    void testGetAllHeroes() {
        Hero hero = new Hero();
        hero.setName("Test Hero Name");
        hero.setDescription("Description of Hero");
        hero = heroDao.addHero(hero);

        Hero hero2 = new Hero();
        hero2.setName("Test Another Hero Name");
        hero2.setDescription("Description of Another Hero");
        hero2 = heroDao.addHero(hero);

        List<Hero> heroesList = heroDao.getAllHeroes();

        assertEquals(2, heroesList.size());
        assertTrue(heroesList.contains(hero));
        assertTrue(heroesList.contains(hero2));
    }

    @Test
    void testUpdateHero() throws ElementNotFoundException {
        Hero hero = new Hero();
        hero.setName("Test Hero Name");
        hero.setDescription("Description of Hero");
        hero = heroDao.addHero(hero);

        Hero heroFromDao = heroDao.getHeroById(hero.getHeroId());
        assertEquals(hero, heroFromDao);

        hero.setName("Different Hero Name Than First");
        heroDao.updateHero(hero);

        assertNotEquals(hero, heroFromDao);
    }

    @Test
    void testDeleteHeroById() {
        Hero hero = new Hero();
        hero.setName("Test Hero Name");
        hero.setDescription("Description of Hero");
        hero = heroDao.addHero(hero);

        Hero hero2 = new Hero();
        hero2.setName("Testy Name 2");
        hero2.setDescription("Description of second hero");
        hero2 = heroDao.addHero(hero2);

        heroDao.deleteHeroById(hero.getHeroId());

        assertEquals(1, heroDao.getAllHeroes().size());

    }

    @Test
    void testGetAllHeroesSightedAtLocation() {

        Hero hero1 = new Hero();
        hero1.setName("Test Hero Name");
        hero1.setDescription("Description of Hero");
        hero1 = heroDao.addHero(hero1);

        Hero hero2 = new Hero();
        hero2.setName("Testy Name 2");
        hero2.setDescription("Description of second hero");
        hero2 = heroDao.addHero(hero2);

        Hero hero3 = new Hero();
        hero3.setName("Dr. Doc");
        hero3.setDescription("The surgeon");
        hero3 = heroDao.addHero(hero3);

        List<Hero> sighting1HeroList = new ArrayList<>();
        List<Hero> sighting2HeroList = new ArrayList<>();
        List<Hero> sighting3HeroList = new ArrayList<>();

        sighting1HeroList.add(hero1);
        sighting1HeroList.add(hero2);

        sighting2HeroList.add(hero3);

        sighting3HeroList.add(hero1);

        Location location1 = new Location();
        location1.setName("Home");
        location1.setLongitude(23.2323);
        location1.setLatitude(45.4545);
        location1.setAddress("Unknown");
        location1.setDescription("Description Test");
        location1 = locationDao.addLocation(location1);

        Location location2 = new Location();
        location2.setName("Home2");
        location2.setLongitude(123.2323);
        location2.setLatitude(145.4545);
        location2.setAddress("Unknown");
        location2.setDescription("Description Test");
        location2 = locationDao.addLocation(location2);

        Sighting sighting1 = new Sighting();
        sighting1.setLocationId(location1.getLocationId());
        sighting1.setSightingDate(LocalDate.parse("1983-11-05"));
        sighting1.setDescription("First Sighting Test");
        sighting1.setHeroes(sighting1HeroList);
        sighting1 = sightingDao.addSighting(sighting1);

        Sighting sighting2 = new Sighting();
        sighting2.setLocationId(location1.getLocationId());
        sighting2.setSightingDate(LocalDate.parse("1983-11-06"));
        sighting2.setDescription("Second Sighting Test");
        sighting2.setHeroes(sighting2HeroList);
        sighting2 = sightingDao.addSighting(sighting2);

        //Different location that above 2
        Sighting sighting3 = new Sighting();
        sighting3.setLocationId(location2.getLocationId());
        sighting3.setSightingDate(LocalDate.parse("1983-11-04"));
        sighting3.setDescription("Third Sighting Test Different Location");
        sighting3.setHeroes(sighting3HeroList);
        sighting3 = sightingDao.addSighting(sighting3);

        List<Hero> location1Heroes = heroDao.GetAllHeroesSightedAtLocation(location1.getLocationId());
        List<Hero> location2Heroes = heroDao.GetAllHeroesSightedAtLocation(location2.getLocationId());

        assertEquals(3, location1Heroes.size());
        assertEquals(1, location2Heroes.size());
    }

    @Test
    void testGetAllOrganizationsForHero() {
        List<Organization> orgs = new ArrayList<>();

        Hero hero1 = new Hero();
        hero1.setName("Test Hero Name");
        hero1.setDescription("Description of Hero");
        hero1 = heroDao.addHero(hero1);

        Organization org1 = new Organization();
        org1.setName("Org Name Test");
        org1.setContact("Contact Test");
        org1.setDescription("Description Test");
        org1 = organizationDao.addOrganization(org1);

        Organization org2 = new Organization();
        org2.setName("Org Name Test2");
        org2.setContact("Contact Test2");
        org2.setDescription("Description Test2");
        org2 = organizationDao.addOrganization(org2);

        orgs.add(org1);
        orgs.add(org2);

        hero1.setOrganizations(orgs);

        assertEquals(2, hero1.getOrganizations().size());
    }
}