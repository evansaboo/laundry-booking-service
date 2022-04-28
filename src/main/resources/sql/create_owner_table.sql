DROP TABLE IF EXISTS OWNERS;

CREATE TABLE OWNERS(
ID SERIAL PRIMARY KEY, NAME VARCHAR(50), ADDRESS VARCHAR(50), POSTAL_CODE INT
);

INSERT INTO
  OWNERS (NAME, ADDRESS, POSTAL_CODE)
VALUES
  ('Olivia', 'ABC123', 11111),
  ('Pelle', 'DEF456', 11111),
  ('Kalle', 'GHI789', 11111);