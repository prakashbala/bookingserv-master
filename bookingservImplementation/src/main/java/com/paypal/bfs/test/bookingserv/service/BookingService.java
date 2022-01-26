package com.paypal.bfs.test.bookingserv.service;


import com.paypal.bfs.test.bookingserv.api.model.Booking;

import java.util.List;

public interface BookingService {

    /**
     * Save the given entity
     * @param entity contains booking details
     * @return the created Booking with new id
     */
    Booking save(Booking entity);

    /**
     * @return list of bookings
     */
    List<Booking> findAll();
}
