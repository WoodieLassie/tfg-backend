DROP TABLE IF EXISTS favourites;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS recommendations;
DROP TABLE IF EXISTS episode_character;
DROP TABLE IF EXISTS actor_character;
DROP TABLE IF EXISTS actors;
DROP TABLE IF EXISTS characters;
DROP TABLE IF EXISTS episodes;
DROP TABLE IF EXISTS seasons;
DROP TABLE IF EXISTS shows;
DROP TABLE IF EXISTS chat_rooms;
DROP TABLE IF EXISTS chat_messages;
DROP TABLE IF EXISTS friendships;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) NOT NULL,
  username VARCHAR(20) NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(100) NOT NULL,
  CONSTRAINT users_unq_01 UNIQUE (email),
  CONSTRAINT users_unq_02 UNIQUE (username)
);

CREATE TABLE friendships (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  request_status BOOLEAN NOT NULL,
  CONSTRAINT friendships_fk_01 FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT friendships_fk_02 FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE chat_rooms (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    chat_room_reference VARCHAR(100) NOT NULL,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    CONSTRAINT chat_rooms_fk_01 FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chat_rooms_fk_02 FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE chat_messages (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    chat_room_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content VARCHAR(255) NOT NULL,
    timestamp DATE NOT NULL,
    CONSTRAINT chat_messages_fk_01 FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chat_messages_fk_02 FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE shows (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(100) NOT NULL
);

CREATE TABLE recommendations (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  show_id BIGINT NOT NULL,
  CONSTRAINT recommendations_fk_01 FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT recommendations_fk_02 FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT recommendations_fk_03 FOREIGN KEY (show_id) REFERENCES shows(id) ON DELETE CASCADE
);

CREATE TABLE comments (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  text VARCHAR(255) NOT NULL,
  user_id BIGINT NOT NULL,
  show_id BIGINT NOT NULL,
  CONSTRAINT comments_fk_01 FOREIGN KEY (show_id) REFERENCES shows (id) ON DELETE CASCADE,
  CONSTRAINT user_comments_fk_01 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE reviews (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  rating INT NOT NULL,
  user_id BIGINT NOT NULL,
  show_id BIGINT NOT NULL,
  CONSTRAINT reviews_fk_01 FOREIGN KEY (show_id) REFERENCES shows (id),
  CONSTRAINT user_reviews_fk_01 FOREIGN KEY (user_id) REFERENCES users (id)
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
  birth_location VARCHAR(100) NOT NULL
);

CREATE TABLE actor_character (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    actor_id BIGINT NOT NULL,
    character_id BIGINT NOT NULL,
    CONSTRAINT actor_character_fk_01 FOREIGN KEY (actor_id) REFERENCES actors (id),
    CONSTRAINT actor_character_fk_02 FOREIGN KEY (character_id) REFERENCES characters (id)
);

CREATE TABLE favourites (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  show_id BIGINT NOT NULL,
  CONSTRAINT favourites_fk_01 FOREIGN KEY (show_id) REFERENCES shows (id) ON DELETE CASCADE,
  CONSTRAINT user_favourites_fk_01 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);