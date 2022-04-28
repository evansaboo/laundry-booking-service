DROP TABLE IF EXISTS LAUNDRY_BOOKINGS;

CREATE TABLE LAUNDRY_BOOKINGS(
ID SERIAL PRIMARY KEY, OWNER_ID INTEGER, BOOKING_FROM TIMESTAMP, BOOKING_TO TIMESTAMP, LAUNDRY_ID INTEGER
);

INSERT INTO
  LAUNDRY_BOOKINGS (OWNER_ID, BOOKING_FROM, BOOKING_TO, LAUNDRY_ID)
VALUES
  (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
  (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2);