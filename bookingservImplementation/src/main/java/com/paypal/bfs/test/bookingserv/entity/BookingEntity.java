package com.paypal.bfs.test.bookingserv.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@SuppressWarnings("unused") //getter, setters are required so suppressing unused warnings
@Entity(name = "Booking")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "BOOKINGS")
public class BookingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "DATE_OF_BIRTH")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "CHECK_IN")
    private Date checkIn;

    @Column(name = "CHECK_OUT")
    private Date checkOut;

    @Column(name = "TOTAL_PRICE")
    private Double totalPrice;

    @Column(name = "DEPOSIT")
    private Double deposit;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @JsonManagedReference
    private AddressEntity address;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingEntity bookingEntity = (BookingEntity) o;
        return Objects.equals(id, bookingEntity.id) && Objects.equals(firstName, bookingEntity.firstName) && Objects.equals(lastName, bookingEntity.lastName) && Objects.equals(dateOfBirth, bookingEntity.dateOfBirth) && Objects.equals(checkIn, bookingEntity.checkIn) && Objects.equals(checkOut, bookingEntity.checkOut) && Objects.equals(totalPrice, bookingEntity.totalPrice) && Objects.equals(deposit, bookingEntity.deposit) && Objects.equals(address, bookingEntity.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, dateOfBirth, checkIn, checkOut, totalPrice, deposit, address);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", totalPrice=" + totalPrice +
                ", deposit=" + deposit +
                '}';
    }
}
