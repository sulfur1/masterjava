DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS cities_seq;

CREATE SEQUENCE user_seq START 100000;
CREATE SEQUENCE cities_seq START 100000;

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL
);

CREATE UNIQUE INDEX email_idx ON users (email);

CREATE TABLE cities (
    id      INTEGER PRIMARY KEY DEFAULT nextval('cities_seq'),
    city    TEXT NOT NULL
);
CREATE UNIQUE INDEX city_idx ON cities (city);