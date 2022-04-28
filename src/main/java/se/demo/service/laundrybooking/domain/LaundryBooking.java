package se.demo.service.laundrybooking.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LaundryBooking(Integer bookingId, Integer ownerId, LocalDateTime bookingFrom, LocalDateTime bookingTo, Integer laundryId) {
}
