package com.paypal.bfs.test.bookingserv;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.bookingserv.api.model.Address;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.entity.BookingEntity;
import com.paypal.bfs.test.bookingserv.repository.BookingRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = BookingServApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class BookingIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void whenValidInput_thenCreateBooking() throws Exception {
        Booking validBooking = createValidBooking("Jasprit", "Bumrah");
        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(validBooking)));

        List<BookingEntity> bookings = bookingRepository.findAll();
        assertThat(bookings).extracting(BookingEntity::getFirstName).containsOnly("Jasprit");
    }

    @Test
    public void whenBookingsUpdated_thenGetAllShouldReturnAll() throws Exception {
        Booking validBooking1 = createValidBooking("Mark", "Waugh");
        Booking validBooking2 = createValidBooking("Steve", "Waugh");

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(validBooking1)));
        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(validBooking2)));

        mvc.perform(get("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(validBooking2)))
                .andExpect(jsonPath("$", hasSize(equalTo(2))));

    }

    @Test
    public void whenMultipleValidBooking_thenExpectIncreasedSize() throws Exception {
        Booking validBooking1 = createValidBooking("John", "Doe");
        Booking validBooking2 = createValidBooking("S", "Gill");

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(validBooking1)));
        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(validBooking2)));

        List<BookingEntity> bookings = bookingRepository.findAll();
        assertThat(bookings).extracting(BookingEntity::getFirstName).containsOnly("John", "S");
    }

    @Test
    public void whenDuplicateRequest_thenExpectOnlyOneBooking() throws Exception {
        Booking validBooking = createValidBooking();

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(validBooking)));
        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(validBooking)));

        List<BookingEntity> bookings = bookingRepository.findAll();
        assertThat(bookings).extracting(BookingEntity::getFirstName).containsOnly("John");
    }

    @Test
    public void whenInvalidDOBFormat_thenExpect_Error_BOOK_DOB_01() throws Exception {
        Booking booking = createValidBooking();
        booking.setDateOfBirth("12/12/2021");

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking))).andExpect(jsonPath("$.errorCode", is("BOOK-FORM-01")));
    }

    @Test
    public void whenFutureDOB_thenExpect_Error_BOOK_DOB_01() throws Exception {
        Booking booking = createValidBooking();
        booking.setDateOfBirth(LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE));

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-DOB-01")));
    }

    @Test
    public void whenCheckOutBeforeCheckIn_thenExpect_Error_BOOK_CHK_01() throws Exception {
        Booking booking = createValidBooking();
        booking.setCheckIn(Date.from(LocalDateTime.now().plusMonths(1).plusSeconds(1).toInstant(ZoneOffset.UTC)));
        booking.setCheckOut(Date.from(LocalDateTime.now().plusMonths(1).toInstant(ZoneOffset.UTC)));

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking))).andExpect(jsonPath("$.errorCode", is("BOOK-CHK-01")));
    }

    @Test
    public void whenCheckOutCheckAreInPast_thenExpect_Error_BOOK_TM_02() throws Exception {
        Booking booking = createValidBooking();
        booking.setCheckIn(Date.from(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC)));

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking))).andExpect(jsonPath("$.errorCode", is("BOOK-CHK-02")));
    }

    @Test
    public void whenFirstNameInvalid_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        booking.setFirstName("");

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("firstName")));
    }

    @Test
    public void whenLastNameInvalid_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        booking.setLastName("");

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("lastName")));
    }

    @Test
    public void whenTotalPriceInvalid_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        booking.setTotalPrice(null);

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("totalPrice")));
    }

    @Test
    public void whenDepositInvalid_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        booking.setDeposit(null);

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("deposit")));
    }

    @Test
    public void whenAddressMissing_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        booking.setAddress(null);

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("address")));
    }

    @Test
    public void whenAddressLine1Invalid_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        Address address = booking.getAddress();
        address.setLine1("");

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("line1")));
    }

    @Test
    public void whenCityInvalid_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        Address address = booking.getAddress();
        address.setCity("");


        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("city")));
    }

    @Test
    public void whenStateInvalid_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        Address address = booking.getAddress();
        address.setState("");

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("state")));
    }

    @Test
    public void whenInvalidCountry_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        Address address = booking.getAddress();
        address.setCountry("");

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("country")));
    }

    @Test
    public void whenZipCode_thenExpect_Error_BOOK_MAND_01() throws Exception {
        Booking booking = createValidBooking();
        Address address = booking.getAddress();
        address.setZipCode("");

        mvc.perform(post("/v1/bfs/booking").contentType(MediaType.APPLICATION_JSON)
                .content(toJson(booking)))
                .andExpect(jsonPath("$.errorCode", is("BOOK-MAND-01")))
                .andExpect(jsonPath("$.details[0]", containsString("zipCode")));
    }

    @After
    public void resetDb() {
        bookingRepository.deleteAll();
    }


    private Booking createValidBooking() {
        return createValidBooking("John", "Doe");
    }

    private Booking createValidBooking(String firstName, String lastName){
        Booking booking = new Booking();
        booking.setFirstName(firstName);
        booking.setLastName(lastName);
        booking.setCheckIn(Date.from(LocalDateTime.now().plusMonths(1).toInstant(ZoneOffset.UTC)));
        booking.setCheckOut(Date.from(LocalDateTime.now().plusMonths(1).plusDays(3).toInstant(ZoneOffset.UTC)));
        booking.setDateOfBirth(LocalDate.now().minusYears(19).format(DateTimeFormatter.ISO_DATE));
        booking.setTotalPrice(Double.valueOf("324.50"));
        booking.setDeposit(Double.valueOf("100"));
        booking.setAddress(createValidAddress());
        return booking;
    }

    private Address createValidAddress() {
        Address address = new Address();
        address.setLine1("Smith Apartment 1c 213 Derrick Street");
        address.setCity("Boston");
        address.setState("MA");
        address.setCountry("02130");
        address.setZipCode("USA");
        return address;
    }

    private byte[] toJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}
