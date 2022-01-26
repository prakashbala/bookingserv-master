package com.paypal.bfs.test.bookingserv.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * The Entity class for Address Object
 */
@SuppressWarnings("unused") //getter, setters are required so suppressing unused warnings
@Entity(name = "Address")
@Table(name = "ADDRESSES")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AddressEntity implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "LINE1", nullable = false)
    private String line1;

    @Column(name = "LINE2")
    private String line2;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Column(name = "STATE", nullable = false)
    private String state;

    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @Column(name = "ZIP_CODE", nullable = false)
    private String zipCode;

    @OneToOne(mappedBy = "address")
    @JsonBackReference
    private BookingEntity booking;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public BookingEntity getBooking() {
        return booking;
    }

    public void setBooking(BookingEntity booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressEntity addressEntity = (AddressEntity) o;
        return Objects.equals(line1, addressEntity.line1) && Objects.equals(city, addressEntity.city)
                && Objects.equals(state, addressEntity.state) && Objects.equals(country, addressEntity.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, line1, line2, city, state, country, zipCode, booking);
    }

    @Override
    public String toString() {
        return "AddressE{" +
                "id=" + id +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", booking=" + booking +
                '}';
    }
}
