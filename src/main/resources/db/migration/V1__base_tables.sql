DROP TABLE IF EXISTS ${sch}.images;
DROP TABLE IF EXISTS ${sch}.users;
DROP TABLE IF EXISTS ${sch}.episode_character;
DROP TABLE IF EXISTS ${sch}.actors;
DROP TABLE IF EXISTS ${sch}.characters;
DROP TABLE IF EXISTS ${sch}.episodes;
DROP TABLE IF EXISTS ${sch}.seasons;

CREATE TABLE ${sch}.seasons (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  season_num INT NOT NULL,
  description VARCHAR(100) NOT NULL,
  CONSTRAINT seasons_unq_01 UNIQUE (season_num)
);

CREATE TABLE ${sch}.episodes (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  episode_num INT NOT NULL,
  title VARCHAR(100) NOT NULL,
  summary VARCHAR(100) NOT NULL,
  season_id BIGINT NOT NULL,
  CONSTRAINT episodes_fk_01 FOREIGN KEY (season_id) REFERENCES ${sch}.seasons (id)
);

CREATE TABLE ${sch}.characters (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(100) NOT NULL,
  gender VARCHAR(100) NOT NULL,
  nationality VARCHAR(100) NOT NULL,
  age INT NOT NULL
);

CREATE TABLE ${sch}.episode_character (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  episode_id BIGINT NOT NULL,
  character_id BIGINT NOT NULL,
  CONSTRAINT episode_character_fk_01 FOREIGN KEY (episode_id) REFERENCES ${sch}.episodes (id),
  CONSTRAINT episode_character_fk_02 FOREIGN KEY (character_id) REFERENCES ${sch}.characters (id)
);

CREATE TABLE ${sch}.actors (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  birth_date DATE NOT NULL,
  nationality VARCHAR(100) NOT NULL,
  gender VARCHAR(100) NOT NULL,
  birth_location VARCHAR(100) NOT NULL,
  character_id BIGINT NOT NULL,
  CONSTRAINT actors_fk_01 FOREIGN KEY (character_id) REFERENCES characters (id)
);