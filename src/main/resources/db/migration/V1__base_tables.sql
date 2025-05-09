DROP TABLE IF EXISTS images;
DROP TABLE IF EXISTS favourites;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS episode_character;
DROP TABLE IF EXISTS actors;
DROP TABLE IF EXISTS characters;
DROP TABLE IF EXISTS episodes;
DROP TABLE IF EXISTS seasons;
DROP TABLE IF EXISTS shows;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(100) NOT NULL,
  CONSTRAINT users_unq_01 UNIQUE (email)
);

CREATE TABLE shows (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(100) NOT NULL
);

CREATE TABLE comments (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  text VARCHAR(255) NOT NULL,
  show_id BIGINT NOT NULL,
  CONSTRAINT comments_fk_01 FOREIGN KEY (show_id) REFERENCES shows (id)
);

CREATE TABLE reviews (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  rating INT NOT NULL,
  show_id BIGINT NOT NULL,
  CONSTRAINT reviews_fk_01 FOREIGN KEY (show_id) REFERENCES shows (id)
);

CREATE TABLE seasons (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  season_num INT NOT NULL,
  description VARCHAR(100) NOT NULL,
  show_id BIGINT NOT NULL,
  CONSTRAINT seasons_fk_01 FOREIGN KEY (show_id) REFERENCES shows (id)
);

CREATE TABLE episodes (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  episode_num INT NOT NULL,
  title VARCHAR(100) NOT NULL,
  summary VARCHAR(100) NOT NULL,
  season_id BIGINT NOT NULL,
  CONSTRAINT episodes_fk_01 FOREIGN KEY (season_id) REFERENCES seasons (id)
);

CREATE TABLE characters (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(100) NOT NULL,
  gender VARCHAR(100) NOT NULL,
  nationality VARCHAR(100) NOT NULL,
  age INT NOT NULL
);

CREATE TABLE episode_character (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  episode_id BIGINT NOT NULL,
  character_id BIGINT NOT NULL,
  CONSTRAINT episode_character_fk_01 FOREIGN KEY (episode_id) REFERENCES episodes (id),
  CONSTRAINT episode_character_fk_02 FOREIGN KEY (character_id) REFERENCES characters (id)
);

CREATE TABLE actors (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  birth_date DATE NOT NULL,
  nationality VARCHAR(100) NOT NULL,
  gender VARCHAR(100) NOT NULL,
  birth_location VARCHAR(100) NOT NULL,
  character_id BIGINT NOT NULL,
  CONSTRAINT actors_fk_01 FOREIGN KEY (character_id) REFERENCES characters (id) ON DELETE CASCADE
);

CREATE TABLE favourites (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  show_id BIGINT NOT NULL,
  CONSTRAINT favourites_fk_01 FOREIGN KEY (show_id) REFERENCES shows (id) ON DELETE CASCADE
);