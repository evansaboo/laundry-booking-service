package se.demo.service.laundrybooking.service;

import ch.qos.logback.core.boolex.EvaluationException;
import io.micrometer.core.instrument.config.validate.Validated;
import io.micrometer.core.instrument.config.validate.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import se.demo.service.laundrybooking.controller.model.BookingResponse;
import se.demo.service.laundrybooking.domain.LaundryBooking;
import se.demo.service.laundrybooking.repository.LaundryBookingFacade;
import se.demo.service.laundrybooking.repository.OwnerFacade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private OwnerFacade ownerFacade;
    @Autowired
    private LaundryBookingFacade laundryBookingFacade;

    public void generateBookingOwnersAndLaundryBookings() {
        ownerFacade.createCustomerTable();
        laundryBookingFacade.createLaundryBookingTable();
    }

    public BookingResponse bookLaundry(LaundryBooking booking) {
        List<LaundryBooking> conflictingBookings = laundryBookingFacade.fetchConflictingBookings(booking);

        validateBookingPeriod(booking);

        if(conflictingBookings.size() > 0) {
            return getBookingResponse(false, conflictingBookings);
        }


        laundryBookingFacade.insertNewBooking(booking);
        return getBookingResponse(true, List.of());
    }

    // Should create a custom exception and be handled by @RestControllerAdvice
    private void validateBookingPeriod(LaundryBooking booking) {

        if (booking.bookingFrom().isAfter(booking.bookingTo())) {
            throw new RestClientException("Booking time period is invalid");
        }

        LocalDateTime bookableTimeStart = LocalDateTime.of(booking.bookingFrom().toLocalDate(), LocalTime.of(7,0));
        LocalDateTime bookableTimeEnd = LocalDateTime.of(booking.bookingFrom().toLocalDate(), LocalTime.of(22,0));

        if(booking.bookingFrom().isBefore(bookableTimeStart) ||
            booking.bookingFrom().isAfter(bookableTimeEnd) ||
            booking.bookingTo().isBefore(bookableTimeStart) ||
            booking.bookingTo().isAfter(bookableTimeEnd)) {

            throw new RestClientException("Booking time period is not in the timeslot between 07:00 and 22:00");
        }
    }


    public List<LaundryBooking> getAllBookings() {
        return laundryBookingFacade.fetchAllBookings();
    }

    public void cancelBooking(Integer bookingId){
        laundryBookingFacade.deleteBooking(bookingId);
    }

    private BookingResponse getBookingResponse(boolean success, List<LaundryBooking> conflictingBookings) {
        return BookingResponse.builder()
                .success(success)
                .conflictingBookings(conflictingBookings)
                .build();
    }

}
