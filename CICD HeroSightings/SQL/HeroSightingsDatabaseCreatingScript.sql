DROP SCHEMA IF EXISTS SuperHeroSightings ;

CREATE DATABASE SuperHeroSightings DEFAULT CHARACTER SET utf8;
USE SuperHeroSightings;

CREATE TABLE Hero (
  HeroId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `Name` VARCHAR(30) NOT NULL,
  `Description` VARCHAR(250) NULL
);

CREATE TABLE IF NOT EXISTS Location (
  LocationId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `Name` VARCHAR(45) NOT NULL,
  Latitude DECIMAL(9,6) NOT NULL,
  Longitude DECIMAL(9,6) NOT NULL,
  -- Address is optional because what about the middle of an ocean? Or even just running down the street?
  Address VARCHAR(250) NULL,
  `Description` VARCHAR(250) NULL
);

CREATE TABLE IF NOT EXISTS Sighting (
  SightingId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  LocationId INT NOT NULL,
  SitingDate DATE NOT NULL,
  `Description` VARCHAR(250) NULL,
  FOREIGN KEY fk_Sighting_Location (LocationId)
	REFERENCES Sighting(SightingId)
);

CREATE TABLE `Power` (
  PowerId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `Name` VARCHAR(45) NOT NULL,
  `Description` VARCHAR(250) NULL
);

CREATE TABLE `Organization` (
  OrganizationId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `Name` VARCHAR(45) NOT NULL,
  Contact VARCHAR(45) NOT NULL,
  `Description` VARCHAR(250) NULL
);

CREATE TABLE HeroOrganization (
	HeroId INT NOT NULL,
    OrganizationId INT NOT NULL,
    PRIMARY KEY pk_HeroOrganization (HeroId, OrganizationId),
    FOREIGN KEY fk_HeroOrganization_Hero (HeroID)
		REFERENCES Hero(HeroId),
	FOREIGN KEY fk_HeroOrganization_Organization (OrganizationId)
		REFERENCES `Organization`(OrganizationId)
);

CREATE TABLE HeroPower (
	HeroId INT NOT NULL,
    PowerId INT NOT NULL,
    PRIMARY KEY pk_HeroPower (HeroId, PowerId),
    FOREIGN KEY fk_HeroPower_Hero (HeroId)
		REFERENCES Hero(HeroId),
	FOREIGN KEY fk_HeroPower_Power (PowerId)
		REFERENCES `Power`(PowerId)
);

CREATE TABLE HeroSighting (
	HeroId INT NOT NULL,
    SightingId INT NOT NULL,
    PRIMARY KEY pk_HeroSighting (HeroId, SightingID),
    FOREIGN KEY fk_HeroSighting_Hero (HeroId)
		REFERENCES Hero(HeroId),
	FOREIGN KEY fk_HeroSighting_Sighting (SightingID)
		REFERENCES Sighting(SightingId)
);

CREATE TABLE SightingLocation (
	SightingID INT NOT NULL,
    LocationId INT NOT NULL,
    PRIMARY KEY pk_SightingLocation (SightingId, LocationId),
    FOREIGN KEY fk_SightingLocation_Sighting (SightingId)
		REFERENCES Sighting(SightingId),
	FOREIGN KEY fk_SightingLocation_Location (LocationId)
		REFERENCES Location(LocationId)
);

