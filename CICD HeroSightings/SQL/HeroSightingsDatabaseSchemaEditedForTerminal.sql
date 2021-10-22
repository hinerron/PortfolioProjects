DROP SCHEMA IF EXISTS SuperHeroSightings;

CREATE DATABASE SuperHeroSightings DEFAULT CHARACTER SET utf8;
USE SuperHeroSightings;

CREATE TABLE Hero (
  heroId INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(30) NOT NULL,
  `description` VARCHAR(250) NULL
);

CREATE TABLE IF NOT EXISTS Location (
  locationId INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(45) NOT NULL,
  latitude DECIMAL(9,6) NOT NULL,
  longitude DECIMAL(9,6) NOT NULL,
  -- Address is optional because what about the middle of an ocean? Or even just running down the street?
  address VARCHAR(250) NULL,
  `description` VARCHAR(250) NULL
);

CREATE TABLE IF NOT EXISTS Sighting (
  sightingId INT AUTO_INCREMENT PRIMARY KEY,
  locationId INT NOT NULL,
  sightingDate DATE NOT NULL,
  `description` VARCHAR(250) NULL,
  FOREIGN KEY fk_Sighting_Location (locationId)
	REFERENCES Location(locationId)
);

CREATE TABLE `Power` (
  powerId INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(250) NULL
);

CREATE TABLE `Organization` (
  organizationId INT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(45) NOT NULL,
  contact VARCHAR(45) NOT NULL,
  `description` VARCHAR(250) NULL
);

CREATE TABLE HeroOrganization (
	heroId INT NOT NULL,
    organizationId INT NOT NULL,
    PRIMARY KEY pk_HeroOrganization (heroId, organizationId),
    FOREIGN KEY fk_HeroOrganization_Hero (heroId)
		REFERENCES Hero(heroId),
	FOREIGN KEY fk_HeroOrganization_Organization (organizationId)
		REFERENCES `Organization`(organizationId)
);

CREATE TABLE HeroPower (
	heroId INT NOT NULL,
    powerId INT NOT NULL,
    PRIMARY KEY pk_HeroPower (heroId, powerId),
    FOREIGN KEY fk_HeroPower_Hero (heroId)
		REFERENCES Hero(heroId),
	FOREIGN KEY fk_HeroPower_Power (powerId)
		REFERENCES `Power`(powerId)
);

CREATE TABLE HeroSighting (
	heroId INT NOT NULL,
    sightingId INT NOT NULL,
    PRIMARY KEY pk_HeroSighting (heroId, sightingID),
    FOREIGN KEY fk_HeroSighting_Hero (heroId)
		REFERENCES Hero(heroId),
	FOREIGN KEY fk_HeroSighting_Sighting (sightingID)
		REFERENCES Sighting(sightingId)
);


