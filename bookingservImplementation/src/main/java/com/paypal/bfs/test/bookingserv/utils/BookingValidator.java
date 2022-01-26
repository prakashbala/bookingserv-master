package com.paypal.bfs.test.bookingserv.utils;

import com.paypal.bfs.test.bookingserv.api.model.Booking;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class BookingValidator {

    public void validateBooking(Booking booking){
        checkInCheckOutValidation(booking.getCheckIn(), booking.getCheckOut());
        dateOfBirthValidation(booking.getDateOfBirth());
    }

    private void checkInCheckOutValidation(Date checkIn, Date checkOut){
        //check-in should happen before check-out
        if (!checkIn.before(checkOut))
            throw new BookingException("BOOK-CHK-01", "check_in time must be before check_out time", HttpStatus.BAD_REQUEST);

        //check-in & check-out shouldn't be in past
        if (!(checkIn.after(new Date()) && checkOut.after(new Date())))
            throw new BookingException("BOOK-CHK-02", "check_in time and check_out time must not be in past", HttpStatus.BAD_REQUEST);
    }

    private void dateOfBirthValidation(String dateOfBirth) {
        LocalDate todayInUTC = LocalDate.now(ZoneId.of("UTC"));

        boolean isValid = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .isBefore(todayInUTC.plusDays(1L));

        if (!isValid)
            throw new BookingException("BOOK-DOB-01", "date_of_birth cannot be in future date", HttpStatus.BAD_REQUEST);
    }

}
