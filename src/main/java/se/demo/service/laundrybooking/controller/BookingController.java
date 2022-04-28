package se.demo.service.laundrybooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.demo.service.laundrybooking.controller.model.*;
import se.demo.service.laundrybooking.domain.LaundryBooking;
import se.demo.service.laundrybooking.service.BookingService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PutMapping("generate-owner-and-booking-table")
    public ResponseEntity<RestResponse> generateOwnerAndBookingTable() {
        bookingService.generateBookingOwnersAndLaundryBookings();

        return ResponseEntity.ok(RestResponse.builder()
                .success(true)
                .build());
    }

    @PostMapping("book-laundry-time")
    public ResponseEntity<BookingResponse> bookLaundryTime(@RequestBody LaundryBooking booking) {
     return ResponseEntity.ok(bookingService.bookLaundry(booking));
    }

    @GetMapping("list-bookings")
    public ResponseEntity<List<LaundryBooking>> listAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    /* It's best to return a response if the cancellation was unsuccessful.
     * For now, we assume it is successful even if the booking doesn't exist.
     */
    @DeleteMapping("cancel-laundry-booking/{bookingId}")
    public ResponseEntity<RestResponse> cancelBookingTime(@PathVariable("bookingId") Integer bookingId) {
        bookingService.cancelBooking(bookingId);

        return ResponseEntity.ok(RestResponse.builder()
                .success(true)
                .build());
    }
}
