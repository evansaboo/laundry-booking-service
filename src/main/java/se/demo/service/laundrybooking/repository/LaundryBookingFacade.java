package se.demo.service.laundrybooking.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import se.demo.service.laundrybooking.domain.LaundryBooking;
import se.demo.service.laundrybooking.repository.util.FileReader;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class LaundryBookingFacade {

    String createLaundryBookingsTable;
    String insertNewBookingSql;
    String deleteBookingSql;
    String fetchAllBookings;
    String fetchBookingsBetweenPeriod;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    private FileReader fileReader;

    @EventListener(ApplicationReadyEvent.class)
    public void loadSqlStatements() {
        createLaundryBookingsTable = fileReader.readFileContent("create_laundry_bookings_table.sql");
        insertNewBookingSql = fileReader.readFileContent("insert_new_booking.sql");
        deleteBookingSql = fileReader.readFileContent("delete_booking.sql");
        fetchAllBookings = fileReader.readFileContent("fetch_all_bookings.sql");
        fetchBookingsBetweenPeriod = fileReader.readFileContent("fetch_bookings_between_period.sql");
    }

    public void createLaundryBookingTable() {
        jdbcTemplate.update(createLaundryBookingsTable, Map.of());
    }

    public void insertNewBooking(LaundryBooking booking) {
        Map<String, Object> sqlParams = Map.of(
                "ownerId", booking.ownerId(),
                "laundryId", booking.laundryId(),
                "bookingFrom", booking.bookingFrom(),
                "bookingTo", booking.bookingTo());

        jdbcTemplate.update(insertNewBookingSql, sqlParams);
    }

    public void deleteBooking(Integer bookingId) {
        Map<String, Integer> sqlParams = Map.of(
                "bookingId", bookingId);

        jdbcTemplate.update(deleteBookingSql, sqlParams);
    }

    public List<LaundryBooking> fetchAllBookings() {
        return jdbcTemplate.query(fetchAllBookings, extractBookingsFromResultSet());
    }

    public List<LaundryBooking> fetchConflictingBookings(LaundryBooking booking) {

        Map<String, Object> sqlParams = Map.of(
                "laundryId", booking.laundryId(),
                "bookingFrom", booking.bookingFrom(),
                "bookingTo", booking.bookingTo());

        return jdbcTemplate.query(fetchBookingsBetweenPeriod, sqlParams, extractBookingsFromResultSet());
    }
    private ResultSetExtractor<List<LaundryBooking>> extractBookingsFromResultSet() {
        return rs -> {
            List<LaundryBooking> bookings = new ArrayList<>();
            while(rs.next()) {
                LaundryBooking booking = LaundryBooking.builder()
                        .bookingId(rs.getInt("ID"))
                        .ownerId(rs.getInt("OWNER_ID"))
                        .laundryId(rs.getInt("LAUNDRY_ID"))
                        .bookingFrom(rs.getObject("BOOKING_FROM", LocalDateTime.class))
                        .bookingTo(rs.getObject("BOOKING_TO", LocalDateTime.class))
                        .build();
                bookings.add(booking);
            }
            return bookings;
        };
    }

}
