package com.sg.rha.superherotracker.model.organization;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;
import com.sg.rha.superherotracker.model.hero.Hero;
import com.sg.rha.superherotracker.model.hero.HeroDao;
import com.sg.rha.superherotracker.model.location.Location;
import com.sg.rha.superherotracker.model.location.LocationDao;
import com.sg.rha.superherotracker.model.power.Power;
import com.sg.rha.superherotracker.model.power.PowerDao;
import com.sg.rha.superherotracker.model.sighting.Sighting;
import com.sg.rha.superherotracker.model.sighting.SightingDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrganizationDaoDBTest {

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
    void testAddGetOrganizationById() throws ElementNotFoundException {
        Organization organization = new Organization();
        organization.setName("Test Organization Name");
        organization.setContact("1-800-97-JENNY");
        organization.setDescription("Description of Organization");
        organization = organizationDao.addOrganization(organization);

        Organization organizationFromDao = organizationDao.getOrganizationById(organization.getOrganizationId());
        assertEquals(organization, organizationFromDao);
    }

    @Test
    void testBadInputAddGetOrganizationById() throws ElementNotFoundException {
        Organization organization = new Organization();
        organization.setName("Test Organization Name");
        organization.setContact("1-800-97-JENNY");
        organization.setDescription("Description of Organization");
        organization = organizationDao.addOrganization(organization);

        assertThrows(ElementNotFoundException.class, () -> {
            Organization organizationFromDao = organizationDao.getOrganizationById(500);
        });
    }

    @Test
    void testGetAllOrganizations() {
        Organization organization = new Organization();
        organization.setName("Test Organization Name");
        organization.setContact("Some number");
        organization.setDescription("Description of Organization");
        organization = organizationDao.addOrganization(organization);

        Organization organization2 = new Organization();
        organization2.setName("Test Another Organization Name");
        organization2.setContact("Some other number");
        organization2.setDescription("Description of Another Organization");
        organization2 = organizationDao.addOrganization(organization);

        List<Organization> organizationesList = organizationDao.getAllOrganizations();

        assertEquals(2, organizationesList.size());
        assertTrue(organizationesList.contains(organization));
        assertTrue(organizationesList.contains(organization2));
    }

    @Test
    void testUpdateOrganization() throws ElementNotFoundException {
        Organization organization = new Organization();
        organization.setName("Test Organization Name");
        organization.setDescription("Description of Organization");
        organization.setContact("800-COOL-ORG");
        organization = organizationDao.addOrganization(organization);

        Organization organizationFromDao = organizationDao.getOrganizationById(organization.getOrganizationId());
        assertEquals(organization, organizationFromDao);

        organization.setName("Different Organization Name Than First");
        organizationDao.updateOrganization(organization);

        assertNotEquals(organization, organizationFromDao);
    }

    @Test
    void testDeleteOrganizationById() {

        Organization organization = new Organization();
        organization.setName("Cool Org");
        organization.setContact("email");
        organization.setDescription("Bunch of Showoffs");
        organization = organizationDao.addOrganization(organization);

        Organization organization2 = new Organization();
        organization2.setName("Another Org");
        organization2.setContact("email2");
        organization2.setDescription("Bad guys");
        organization2 = organizationDao.addOrganization(organization2);

        organizationDao.deleteOrganizationById(organization.getOrganizationId());

        assertEquals(1, organizationDao.getAllOrganizations().size());
    }

    @Test
    void getAllOrganizationsForHero() {
        Hero hero = new Hero();
        Organization org1 = new Organization();
        Organization org2 = new Organization();
        Organization org3 = new Organization();
        List<Organization> orgs = new ArrayList<>();
        Power power = new Power();
        List<Power> powers = new ArrayList<>();

        power.setName("power name");
        power.setDescription("power description");
        power = powerDao.addPower(power);

        powers.add(power);

        org1.setName("org1 test");
        org1.setContact("contact test1");
        org1.setDescription("description1");
        org1 = organizationDao.addOrganization(org1);

        org2.setName("org2 test");
        org2.setContact("contact test1");
        org2.setDescription("description2");
        org2 = organizationDao.addOrganization(org2);

        org3.setName("org3 test");
        org3.setContact("contact test3");
        org3.setDescription("description3");
        org3 = organizationDao.addOrganization(org3);

        orgs.add(org1);
        orgs.add(org3);

        hero.setName("Hero Name");
        hero.setDescription("Hero description");
        hero.setOrganizations(orgs);
        hero.setPowers(powers);
        hero = heroDao.addHero(hero);

        assertEquals(2, organizationDao.getAllOrganizationsForHero(hero.getHeroId()).size());
    }
}