DROP TABLE IF EXISTS ${sch}.users;

CREATE TABLE ${sch}.users (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  deleted BIT NOT NULL DEFAULT 0,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  create_user_id BIGINT DEFAULT NULL,
  update_time DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  update_user_id BIGINT DEFAULT NULL,
  CONSTRAINT users_unq_01 UNIQUE (email),
  CONSTRAINT users_fk_01 FOREIGN KEY (create_user_id) REFERENCES ${sch}.users (id),
  CONSTRAINT users_fk_02 FOREIGN KEY (update_user_id) REFERENCES ${sch}.users (id)
);


