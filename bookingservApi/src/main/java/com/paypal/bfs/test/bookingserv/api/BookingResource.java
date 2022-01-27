package com.paypal.bfs.test.bookingserv.api;

import com.paypal.bfs.test.bookingserv.api.model.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Booking create and get implementations
 */
public interface BookingResource {

    /**
     * Create {@link Booking} resource
     *
     * @param booking the booking object
     * @return the created booking with HttpStatus.OK (200)
     * If user error due to input, the following error codes will be sent with HttpStatus.BAD_REQUEST (400)
     * - BOOK-MAND-01 - if required fields are missing or field validations error from jsr-303
     * - BOOK-CHK-01 - if check-in time is greater than check-out time
     * - BOOK-CHK-02 - if check-in time and check-out past the current time (UTC)
     * - BOOK-DOB-01 - if date-of-birth is in future date
     * - BOOK-FORM-01 - if fields date-of-birth, check-in/out time are in invalid format(formats other than ISO-8601 standard)
     * - BOOK-INV-01 - for any other invalid input such as empty body etc.,
     * If a runtime unchecked exception occurs, system will throw BOOK-SERV-01 with HttpStatus.INTERNAL_SERVER_ERROR (500)
     */
    @RequestMapping(value = "/v1/bfs/booking", method = RequestMethod.POST,
            consumes = "application/json", produces = "application/json")
    ResponseEntity<Booking> create(@RequestBody Booking booking);


    /**
     * GetAll {@link Booking} resource
     *
     * @return all bookings with HttpStatus.OK (200)
     * If a runtime unchecked exception occurs, system will throw BOOK-SERV-01 with HttpStatus.INTERNAL_SERVER_ERROR (500)
     */
    @RequestMapping(value = "/v1/bfs/booking", method = RequestMethod.GET,
            consumes = "application/json", produces = "application/json")
    ResponseEntity<List<Booking>> getAll();

}
