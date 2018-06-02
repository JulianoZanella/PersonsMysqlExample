CREATE DATABASE personsDB;
USE personsDB;
CREATE TABLE person(
	id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    birthDate Date,
    sex CHAR,
    PRIMARY KEY (id)
);