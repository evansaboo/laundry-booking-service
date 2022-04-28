# laundry-booking-service
A laundry booking service where you can book a timeslot, list all booked slots and cancel you booked slot.

1. Update the database credentials (url, username, password) in application.yml to connect to postgres server.
2. Call the REST endpoint (PUT) ```/api/generate-owner-and-booking-table``` to create tables and generate owners and their bookings.
3. You can:
   1. Make a POST request to ```/api/book-laundry-time``` to book a timeslot with the example payload:
      ```json
      {
      "ownerId": 1,
      "bookingFrom": "2022-04-29T20:28:15.607904",
      "bookingTo": "2022-04-29T21:29:15.607904",
      "laundryId": 1
      } 
      ```
   2. GET request to ```/api/list-bookings``` to list all booked timeslots.
   3. DELETE request to ```/api/cancel-laundry-booking/{booking-id}``` to cancel a booked timeslot.
