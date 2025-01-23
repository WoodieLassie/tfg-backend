ALTER TABLE reviews ADD deleted BIT NOT NULL DEFAULT 0;
ALTER TABLE reviews ADD create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE reviews ADD create_user_id BIGINT DEFAULT NULL;
ALTER TABLE reviews ADD update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE reviews ADD update_user_id BIGINT DEFAULT NULL;
ALTER TABLE reviews ADD CONSTRAINT reviews_users_fk_01 FOREIGN KEY (create_user_id) REFERENCES users(id);
ALTER TABLE reviews ADD CONSTRAINT reviews_users_fk_02 FOREIGN KEY (update_user_id) REFERENCES users(id);

ALTER TABLE comments ADD deleted BIT NOT NULL DEFAULT 0;
ALTER TABLE comments ADD create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE comments ADD create_user_id BIGINT DEFAULT NULL;
ALTER TABLE comments ADD update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE comments ADD update_user_id BIGINT DEFAULT NULL;
ALTER TABLE comments ADD CONSTRAINT comments_users_fk_01 FOREIGN KEY (create_user_id) REFERENCES users(id);
ALTER TABLE comments ADD CONSTRAINT comments_users_fk_02 FOREIGN KEY (update_user_id) REFERENCES users(id);

ALTER TABLE shows ADD deleted BIT NOT NULL DEFAULT 0;
ALTER TABLE shows ADD create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE shows ADD create_user_id BIGINT DEFAULT NULL;
ALTER TABLE shows ADD update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE shows ADD update_user_id BIGINT DEFAULT NULL;
ALTER TABLE shows ADD CONSTRAINT shows_users_fk_01 FOREIGN KEY (create_user_id) REFERENCES users(id);
ALTER TABLE shows ADD CONSTRAINT shows_users_fk_02 FOREIGN KEY (update_user_id) REFERENCES users(id);

ALTER TABLE episodes ADD deleted BIT NOT NULL DEFAULT 0;
ALTER TABLE episodes ADD create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE episodes ADD create_user_id BIGINT DEFAULT NULL;
ALTER TABLE episodes ADD update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE episodes ADD update_user_id BIGINT DEFAULT NULL;
ALTER TABLE episodes ADD CONSTRAINT episodes_users_fk_01 FOREIGN KEY (create_user_id) REFERENCES users(id);
ALTER TABLE episodes ADD CONSTRAINT episodes_users_fk_02 FOREIGN KEY (update_user_id) REFERENCES users(id);

ALTER TABLE episode_character ADD deleted BIT NOT NULL DEFAULT 0;
ALTER TABLE episode_character ADD create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE episode_character ADD create_user_id BIGINT DEFAULT NULL;
ALTER TABLE episode_character ADD update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE episode_character ADD update_user_id BIGINT DEFAULT NULL;
ALTER TABLE episode_character ADD CONSTRAINT episode_character_users_fk_01 FOREIGN KEY (create_user_id) REFERENCES users(id);
ALTER TABLE episode_character ADD CONSTRAINT episode_character_users_fk_02 FOREIGN KEY (update_user_id) REFERENCES users(id);

ALTER TABLE seasons ADD deleted BIT NOT NULL DEFAULT 0;
ALTER TABLE seasons ADD create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE seasons ADD create_user_id BIGINT DEFAULT NULL;
ALTER TABLE seasons ADD update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE seasons ADD update_user_id BIGINT DEFAULT NULL;
ALTER TABLE seasons ADD CONSTRAINT season_users_fk_01 FOREIGN KEY (create_user_id) REFERENCES users(id);
ALTER TABLE seasons ADD CONSTRAINT season_users_fk_02 FOREIGN KEY (update_user_id) REFERENCES users(id);

ALTER TABLE characters ADD deleted BIT NOT NULL DEFAULT 0;
ALTER TABLE characters ADD create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE characters ADD create_user_id BIGINT DEFAULT NULL;
ALTER TABLE characters ADD update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE characters ADD update_user_id BIGINT DEFAULT NULL;
ALTER TABLE characters ADD CONSTRAINT characters_users_fk_01 FOREIGN KEY (create_user_id) REFERENCES users(id);
ALTER TABLE characters ADD CONSTRAINT characters_users_fk_02 FOREIGN KEY (update_user_id) REFERENCES users(id);

ALTER TABLE actors ADD deleted BIT NOT NULL DEFAULT 0;
ALTER TABLE actors ADD create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE actors ADD create_user_id BIGINT DEFAULT NULL;
ALTER TABLE actors ADD update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE actors ADD update_user_id BIGINT DEFAULT NULL;
ALTER TABLE actors ADD CONSTRAINT actors_users_fk_01 FOREIGN KEY (create_user_id) REFERENCES users(id);
ALTER TABLE actors ADD CONSTRAINT actors_users_fk_02 FOREIGN KEY (update_user_id) REFERENCES users(id);

ALTER TABLE images ADD deleted BIT NOT NULL DEFAULT 0;
ALTER TABLE images ADD create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE images ADD create_user_id BIGINT DEFAULT NULL;
ALTER TABLE images ADD update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP;
ALTER TABLE images ADD update_user_id BIGINT DEFAULT NULL;
ALTER TABLE images ADD CONSTRAINT images_users_fk_01 FOREIGN KEY (create_user_id) REFERENCES users(id);
ALTER TABLE images ADD CONSTRAINT images_users_fk_02 FOREIGN KEY (update_user_id) REFERENCES users(id);

UPDATE seasons SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;
UPDATE episodes SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;
UPDATE characters SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;
UPDATE actors SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;
UPDATE episode_character SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;
UPDATE images SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;
UPDATE shows SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;
UPDATE users SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;
UPDATE comments SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;
UPDATE reviews SET deleted = 0, create_time = '2021-02-22 00:00:00', create_user_id = 1, update_time = '2021-02-22 00:00:00', update_user_id = 1;