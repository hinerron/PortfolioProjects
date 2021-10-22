package com.sg.rha.superherotracker.model.sighting;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;
import com.sg.rha.superherotracker.model.hero.Hero;
import com.sg.rha.superherotracker.model.hero.HeroDao;
import com.sg.rha.superherotracker.model.location.Location;
import com.sg.rha.superherotracker.model.location.LocationDao;
import com.sg.rha.superherotracker.model.organization.Organization;
import com.sg.rha.superherotracker.model.organization.OrganizationDao;
import com.sg.rha.superherotracker.model.power.Power;
import com.sg.rha.superherotracker.model.power.PowerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SightingDaoDBTest {

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
    void testAddGetSightingById() throws ElementNotFoundException {
        Location location = new Location();
        Sighting sighting = new Sighting();
        Hero hero = new Hero();
        List<Hero> heroes = new ArrayList<>();
        Power power = new Power();
        List<Power> powers = new ArrayList<>();
        Organization org = new Organization();
        List<Organization> orgs = new ArrayList<>();

        location.setName("LocationNameTest");
        location.setLatitude(100.121212);
        location.setLongitude(10.2222);
        location.setAddress("123 Fake St");
        location.setDescription("A test place");
        location = locationDao.addLocation(location);

        power.setName("Power Test");
        power.setPowerId(1);
        power.setDescription("Desc Test");
        power = powerDao.addPower(power);

        powers.add(power);

        org.setName("org test name");
        org.setDescription("org test description");
        org.setContact("contact");
        org = organizationDao.addOrganization(org);

        orgs.add(org);

        hero.setDescription("A Test Person");
        hero.setName("Lady Test");
        hero.setPowers(powers);
        hero.setOrganizations(orgs);
        hero = heroDao.addHero(hero);

        heroes.add(hero);

        sighting.setSightingDate(LocalDate.parse("1983-11-04"));
        sighting.setDescription("Description of Sighting");
        sighting.setLocationId(location.getLocationId());
        sighting.setHeroes(heroes);
        sighting = sightingDao.addSighting(sighting);

        Sighting sightingFromDao = sightingDao.getSightingById(sighting.getSightingId());
        assertEquals(sighting.getSightingId(), sightingFromDao.getSightingId());
    }

    @Test
    void testBadInputAddGetSightingById() {
        Location location = new Location();
        Sighting sighting = new Sighting();
        Hero hero = new Hero();
        List<Hero> heroes = new ArrayList<>();
        Power power = new Power();
        List<Power> powers = new ArrayList<>();
        Organization org = new Organization();
        List<Organization> orgs = new ArrayList<>();

        location.setName("LocationNameTest");
        location.setLatitude(100.121212);
        location.setLongitude(10.2222);
        location.setAddress("123 Fake St");
        location.setDescription("A test place");
        location = locationDao.addLocation(location);

        power.setName("Power Test");
        power.setPowerId(1);
        power.setDescription("Desc Test");
        power = powerDao.addPower(power);

        powers.add(power);

        org.setName("org test name");
        org.setDescription("org test description");
        org.setContact("contact");
        org = organizationDao.addOrganization(org);

        orgs.add(org);

        hero.setDescription("A Test Person");
        hero.setName("Lady Test");
        hero.setPowers(powers);
        hero.setOrganizations(orgs);
        hero = heroDao.addHero(hero);

        heroes.add(hero);

        sighting.setSightingDate(LocalDate.parse("1983-11-04"));
        sighting.setDescription("Description of Sighting");
        sighting.setLocationId(location.getLocationId());
        sighting.setHeroes(heroes);
        sighting = sightingDao.addSighting(sighting);


        assertThrows(ElementNotFoundException.class, () -> {
            Sighting sightingFromDao = sightingDao.getSightingById(97);
        } );
    }

    @Test
    void testGetAllSightings() {
        Location location = new Location();
        Sighting sighting = new Sighting();
        Hero hero = new Hero();
        List<Hero> heroes = new ArrayList<>();
        Power power = new Power();
        List<Power> powers = new ArrayList<>();
        Organization org = new Organization();
        List<Organization> orgs = new ArrayList<>();

        location.setName("LocationNameTest");
        location.setLatitude(100.121212);
        location.setLongitude(10.2222);
        location.setAddress("123 Fake St");
        location.setDescription("A test place");
        location = locationDao.addLocation(location);

        power.setName("Power Test");
        power.setPowerId(1);
        power.setDescription("Desc Test");
        power = powerDao.addPower(power);

        powers.add(power);

        org.setName("org test name");
        org.setDescription("org test description");
        org.setContact("contact");
        org = organizationDao.addOrganization(org);

        orgs.add(org);

        hero.setDescription("A Test Person");
        hero.setName("Lady Test");
        hero.setPowers(powers);
        hero.setOrganizations(orgs);
        hero = heroDao.addHero(hero);

        heroes.add(hero);

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
        sighting2 = sightingDao.addSighting(sighting);

        List<Sighting> sightingsList = sightingDao.getAllSightings();

        assertEquals(2, sightingsList.size());
    }

    @Test
    void testUpdateSighting() throws ElementNotFoundException {
        Location location = new Location();
        Sighting sighting = new Sighting();
        Hero hero = new Hero();
        List<Hero> heroes = new ArrayList<>();
        Power power = new Power();
        List<Power> powers = new ArrayList<>();
        Organization org = new Organization();
        List<Organization> orgs = new ArrayList<>();

        location.setName("LocationNameTest");
        location.setLatitude(100.121212);
        location.setLongitude(10.2222);
        location.setAddress("123 Fake St");
        location.setDescription("A test place");
        location = locationDao.addLocation(location);

        power.setName("Power Test");
        power.setPowerId(1);
        power.setDescription("Desc Test");
        power = powerDao.addPower(power);

        powers.add(power);

        org.setName("org test name");
        org.setDescription("org test description");
        org.setContact("contact");
        org = organizationDao.addOrganization(org);

        orgs.add(org);

        hero.setDescription("A Test Person");
        hero.setName("Lady Test");
        hero.setPowers(powers);
        hero.setOrganizations(orgs);
        hero = heroDao.addHero(hero);

        heroes.add(hero);

        sighting.setSightingDate(LocalDate.parse("1983-11-04"));
        sighting.setDescription("Description of Sighting");
        sighting.setLocationId(location.getLocationId());
        sighting.setHeroes(heroes);
        sighting = sightingDao.addSighting(sighting);

        Sighting sightingFromDao = sightingDao.getSightingById(sighting.getSightingId());
        assertEquals(sighting.getDescription(), sightingFromDao.getDescription());

        sighting.setSightingDate(LocalDate.parse("1983-11-05"));
        sightingDao.updateSighting(sighting);

        assertNotEquals(sighting, sightingFromDao);
    }

    @Test
    void testDeleteSightingById() {
        Location location = new Location();
        Sighting sighting = new Sighting();
        Hero hero = new Hero();
        List<Hero> heroes = new ArrayList<>();
        Power power = new Power();
        List<Power> powers = new ArrayList<>();
        Organization org = new Organization();
        List<Organization> orgs = new ArrayList<>();

        location.setName("LocationNameTest");
        location.setLatitude(100.121212);
        location.setLongitude(10.2222);
        location.setAddress("123 Fake St");
        location.setDescription("A test place");
        location = locationDao.addLocation(location);

        power.setName("Power Test");
        power.setPowerId(1);
        power.setDescription("Desc Test");
        power = powerDao.addPower(power);

        powers.add(power);

        org.setName("org test name");
        org.setDescription("org test description");
        org.setContact("contact");
        org = organizationDao.addOrganization(org);

        orgs.add(org);

        hero.setDescription("A Test Person");
        hero.setName("Lady Test");
        hero.setPowers(powers);
        hero.setOrganizations(orgs);
        hero = heroDao.addHero(hero);

        heroes.add(hero);

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

        assertEquals(2, sightingDao.getAllSightings().size());

        sightingDao.deleteSightingById(sighting.getSightingId());

        assertEquals(1, sightingDao.getAllSightings().size());
    }

    @Test
    void testGetSightingsByDate() throws ElementNotFoundException {
        Location location = new Location();
        location.setName("LocationNameTest");
        location.setLatitude(100.121212);
        location.setLongitude(10.2222);
        location.setAddress("123 Fake St");
        location.setDescription("A test place");
        location = locationDao.addLocation(location);

        Hero hero = new Hero();
        hero.setName("test name");
        hero.setDescription("test description");
        hero = heroDao.addHero(hero);

        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);

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

        LocalDate date = LocalDate.parse("1983-11-04");

        List<Sighting> sightings = sightingDao.getSightingsByDate(date);

        assertEquals(1, sightings.size());
    }

    @Test
    void testBadDateFormatGetSightingsByDate() throws ElementNotFoundException {
        Location location = new Location();
        location.setName("LocationNameTest");
        location.setLatitude(100.121212);
        location.setLongitude(10.2222);
        location.setAddress("123 Fake St");
        location.setDescription("A test place");
        location = locationDao.addLocation(location);

        Hero hero = new Hero();
        hero.setName("test name");
        hero.setDescription("test description");
        hero = heroDao.addHero(hero);

        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);

        Sighting sighting = new Sighting();
        sighting.setSightingDate(LocalDate.parse("1983-11-04"));
        sighting.setDescription("Description of Sighting");
        sighting.setLocationId(location.getLocationId());
        sighting.setHeroes(heroes);
        sighting = sightingDao.addSighting(sighting);

        assertThrows(DateTimeParseException.class, () -> {
            sightingDao.getSightingsByDate(LocalDate.parse("198-11-01"));
        });
    }

    @Test
    void testNoDateFoundGetSightingsByDate() throws ElementNotFoundException {
        Location location = new Location();
        location.setName("LocationNameTest");
        location.setLatitude(100.121212);
        location.setLongitude(10.2222);
        location.setAddress("123 Fake St");
        location.setDescription("A test place");
        location = locationDao.addLocation(location);

        Hero hero = new Hero();
        hero.setName("test name");
        hero.setDescription("test description");
        hero = heroDao.addHero(hero);

        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);

        Sighting sighting = new Sighting();
        sighting.setSightingDate(LocalDate.parse("1983-11-04"));
        sighting.setDescription("Description of Sighting");
        sighting.setLocationId(location.getLocationId());
        sighting.setHeroes(heroes);
        sighting = sightingDao.addSighting(sighting);

        assertThrows(ElementNotFoundException.class, () -> {
            sightingDao.getSightingsByDate(LocalDate.parse("1983-11-01"));
        });
    }
}