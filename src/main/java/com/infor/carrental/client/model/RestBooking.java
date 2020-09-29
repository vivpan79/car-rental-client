package com.infor.carrental.client.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

public class RestBooking {

    public static final String DATE_TIME = "yyyy-MM-dd'T'HH:mm";
    private RestCar car;
    private RestCustomer customer;

    @DateTimeFormat(pattern = DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime fromDate;

    @DateTimeFormat(pattern = DATE_TIME)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime toDate;

    public RestBooking() {
    }

    public RestCar getCar() {
        return car;
    }

    public void setCar(RestCar car) {
        this.car = car;
    }

    public RestCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(RestCustomer customer) {
        this.customer = customer;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }
}
