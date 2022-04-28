package se.demo.service.laundrybooking.controller.model;

import lombok.Builder;
import se.demo.service.laundrybooking.domain.LaundryBooking;

import java.util.List;

@Builder
public record BookingResponse(Boolean success, List<LaundryBooking> conflictingBookings) {
}
