package se.demo.service.laundrybooking.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.demo.service.laundrybooking.domain.LaundryBooking;
import se.demo.service.laundrybooking.repository.LaundryBookingFacade;
import se.demo.service.laundrybooking.repository.OwnerFacade;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    LaundryBookingFacade laundryBookingFacade;

    @MockBean
    OwnerFacade ownerFacade;

    String APPLIANCE_ID = "UXD123";


    @Test
    void generateTables() {
        webTestClient.put()
                .uri("/api/generate-owner-and-booking-table")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("success").isEqualTo("true");
    }

    @Test
    void bookLaundryTime() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("ownerId", 1);
            jsonObject.put("bookingFrom", "2022-04-29T21:28:15.607904");
            jsonObject.put("bookingTo", "2022-04-29T21:29:15.607904");
            jsonObject.put("laundryId", 1);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        Mockito.when(laundryBookingFacade.fetchConflictingBookings(any())).thenReturn(List.of());

        webTestClient.post()
                .uri("/api/book-laundry-time")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonObject.toString())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("success").isEqualTo("true");
    }

    @Test
    void getAllBookings() {

        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = LocalDateTime.now().plusHours(2);

        LaundryBooking booking = LaundryBooking.builder()
                .bookingId(1)
                .ownerId(1)
                .bookingFrom(from)
                .bookingTo(to)
                .laundryId(1).build();

        Mockito.when(laundryBookingFacade.fetchAllBookings()).thenReturn(List.of(booking));

        webTestClient.get()
                .uri("/api/list-bookings")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].bookingId").isEqualTo(1)
                .jsonPath("$.[0].ownerId").isEqualTo(1)
                .jsonPath("$.[0].bookingFrom").isNotEmpty()
                .jsonPath("$.[0].bookingTo").isNotEmpty()
                .jsonPath("$.[0].laundryId").isEqualTo(1);
    }

    @Test
    void cancelBooking() {
        webTestClient.delete()
                .uri("/api/cancel-laundry-booking/1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("success").isEqualTo("true");
    }
}