package com.paypal.bfs.test.bookingserv.api;

import com.paypal.bfs.test.bookingserv.api.model.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public interface BookingResource {
    /**
     * Create {@link Booking} resource
     *
     * @param booking the booking object
     * @return the created booking
     */
    @RequestMapping(value = "/v1/bfs/booking", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    ResponseEntity<Booking> create(@RequestBody Booking booking);


    /**
     * GetAll {@link Booking} resource
     *
     * @return all bookings
     */
    @RequestMapping(value = "/v1/bfs/booking", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    ResponseEntity<List<Booking>> getAll();

}
