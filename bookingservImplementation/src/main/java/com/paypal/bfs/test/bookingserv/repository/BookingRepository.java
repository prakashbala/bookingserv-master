package com.paypal.bfs.test.bookingserv.repository;

import com.paypal.bfs.test.bookingserv.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {

    @Query("select b from Booking b where b.firstName = :firstName and b.lastName = :lastName and b.checkIn = :checkIn and b.checkOut = :checkOut and b.dateOfBirth = :dateOfBirth")
    List<BookingEntity> findByBookingDetails(@Param("firstName")String firstName, @Param("lastName")String lastName,
                                             @Param("checkIn")Date checkIn, @Param("checkOut")Date checkOut,
                                             @Param("dateOfBirth")Date dateOfBirth);

}




