package com.sg.rha.superherotracker.model.hero;

import com.sg.rha.superherotracker.exception.ElementNotFoundException;

import java.util.List;

public interface HeroDao {

    Hero getHeroById(int id) throws ElementNotFoundException;
    List<Hero> getAllHeroes();
    Hero addHero(Hero hero);
    void updateHero(Hero hero);
    void deleteHeroById(int id);

    public List<Hero> GetAllHeroesSightedAtLocation(int locationId);
    public List<Hero> getAllHeroesByOrganization(int orgId);
}
