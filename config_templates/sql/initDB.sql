DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS cities_seq;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');

CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE cities_seq START 100000;

CREATE TABLE cities (
                        id      INTEGER PRIMARY KEY DEFAULT nextval('cities_seq'),
                        city    TEXT NOT NULL
);
CREATE UNIQUE INDEX city_idx ON cities (city);

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL,
  city_id   INTEGER NOT NULL,
  CONSTRAINT city_fk FOREIGN KEY (city_id) REFERENCES cities (id)
);

CREATE UNIQUE INDEX email_idx ON users (email);