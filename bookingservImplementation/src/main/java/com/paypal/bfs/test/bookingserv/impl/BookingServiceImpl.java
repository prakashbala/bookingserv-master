package com.paypal.bfs.test.bookingserv.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.entity.BookingEntity;
import com.paypal.bfs.test.bookingserv.repository.BookingRepository;
import com.paypal.bfs.test.bookingserv.service.BookingService;
import com.paypal.bfs.test.bookingserv.utils.BookingUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ObjectMapper objectMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, ObjectMapper objectMapper) {
        this.bookingRepository = bookingRepository;
        this.objectMapper = objectMapper;
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Booking save(Booking bookingModel) {
        //silently ignore the id user sent
        bookingModel.setId(null);

        //convert to entity
        final BookingEntity bookingRequest = BookingUtils.createBookingEntity(objectMapper, bookingModel);

        //if it's duplicate return existing entity after converting to model
        final BookingEntity duplicateRequest = isDuplicate(bookingRequest);
        if (null != duplicateRequest)
            return BookingUtils.createBookingModel(objectMapper, duplicateRequest);

        //else save in db
        final BookingEntity newEntity = bookingRepository.save(bookingRequest);

        //return model with id set
        bookingModel.setId(newEntity.getId());
        return bookingModel;
    }

    @Override
    public List<Booking> findAll() {
        //retrieve all bookings
        List<BookingEntity> allBookings = bookingRepository.findAll();

        //convert entities to pojo and return
        return allBookings.stream().map(e -> BookingUtils.createBookingModel(objectMapper, e))
                .collect(Collectors.toList());
    }

    private BookingEntity isDuplicate(BookingEntity originalRequest) {
        //check if booking matches with the basic details booking
        List<BookingEntity> similarBookings = bookingRepository.findByBookingDetails(originalRequest.getFirstName(),
                originalRequest.getLastName(), originalRequest.getCheckIn(), originalRequest.getCheckOut(),
                originalRequest.getDateOfBirth());

        //if no similar bookings return
        if (null == similarBookings || similarBookings.isEmpty())
            return null;

        //if similar bookings are available, check if address object matches as well
        return similarBookings.stream().filter(e -> e.getAddress().equals(originalRequest.getAddress())).findAny()
                .orElse(null);
    }
}
