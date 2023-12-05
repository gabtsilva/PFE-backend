DROP SCHEMA IF EXISTS snappies CASCADE;
CREATE SCHEMA snappies;

CREATE TABLE snappies.users(
    email VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50) NOT NULL
);

INSERT INTO snappies.users (email, password) VALUES ('gab@test.com','Sup3rS3cur3!');