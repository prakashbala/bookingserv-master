package com.paypal.bfs.test.bookingserv.impl;

import com.paypal.bfs.test.bookingserv.api.BookingResource;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.service.BookingService;
import com.paypal.bfs.test.bookingserv.utils.BookingValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class BookingResourceImpl implements BookingResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingResourceImpl.class);

    private final BookingService service;
    private final BookingValidator validator;

    public BookingResourceImpl(BookingService service, BookingValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    @Override
    @Validated
    public ResponseEntity<Booking> create(@Valid Booking booking) {
        //validate request
        validator.validateBooking(booking);

        //save request
        Booking result = service.save(booking);

        LOGGER.info("Booking request with id {} is success", result.getId());

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<Booking>> getAll() {
        List<Booking> bookings = service.findAll();

        LOGGER.info("Total bookings retrieved {}",bookings.size());

        return ResponseEntity.ok(bookings);
    }


}

