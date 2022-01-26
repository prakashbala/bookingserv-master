package com.paypal.bfs.test.bookingserv.service;


import com.paypal.bfs.test.bookingserv.api.model.Booking;

import java.util.List;

public interface BookingService {

    Booking save(Booking entity);

    List<Booking> findAll();
}
