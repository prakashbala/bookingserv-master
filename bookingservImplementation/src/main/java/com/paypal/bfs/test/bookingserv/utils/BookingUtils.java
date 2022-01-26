package com.paypal.bfs.test.bookingserv.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.entity.BookingEntity;

public class BookingUtils {

    public static BookingEntity createBookingEntity(final ObjectMapper objectMapper, Booking model) {
        return convertToGivenType(objectMapper, model, BookingEntity.class);
    }

    public static Booking createBookingModel(final ObjectMapper objectMapper, BookingEntity entity) {
        return convertToGivenType(objectMapper, entity, Booking.class);
    }

    private static <T, R> R convertToGivenType(final ObjectMapper mapper, final T input, Class<R> outputType) {
        if (null != input) {
            return mapper.convertValue(input, outputType);
        }
        return null;
    }

}
