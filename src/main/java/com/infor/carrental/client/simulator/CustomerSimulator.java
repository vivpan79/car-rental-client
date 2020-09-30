package com.infor.carrental.client.simulator;

import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

import com.infor.carrental.client.model.RestAvailability;
import com.infor.carrental.client.model.RestBooking;
import com.infor.carrental.client.model.RestCar;
import com.infor.carrental.client.model.RestCars;
import com.infor.carrental.client.model.RestCustomer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class CustomerSimulator {

    private static final Logger log = LoggerFactory.getLogger(CustomerSimulator.class);
    @Autowired
    private RestTemplate restTemplate;

    public CustomerSimulator() {
    }

    @Async
    public void userKeepsBookingCars() throws InterruptedException {
        for (; ; ) {
            log.info("*****************************userKeepsBookingCars****************************");
            registerBooking();
            sleep(1000);
        }
    }

    @Async
    public void registerBooking() {
        String numberPlate = registerCars().getNumberPlate();
        String userName = registerCustomer().getUserName();
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        RestAvailability availability = restTemplate.postForObject(
            "http://localhost:8080/availability/car/"
                + numberPlate
                + "/register/from/" + formatter.format(date)
                + "/to/" + formatter.format(date.plusHours(6L))
                + "/rate/" + new Random().nextInt(), "null", RestAvailability.class);
        log.info("Availability for car with number plate {} has been registered successfully! ",
            availability.getCar().getNumberPlate());
        RestBooking booking = restTemplate.postForObject(
            "http://localhost:8080/booking/car/"
                + numberPlate
                + "/register/from/" + formatter.format(date)
                + "/to/" + formatter.format(date.plusHours(6L))
                + "/for/" + userName, "null", RestBooking.class);
        log.info("Booking for car with number plate {} has been registered successfully! ",
            booking.getCar().getNumberPlate());
    }

    public RestCar registerCars() {
        RestCar restCar = getRestCar();

        RestCar car = restTemplate.postForObject(
            "http://localhost:8080/car/register", restCar, RestCar.class);
        log.info("Car with numberplate {} has been registered successfully! ", car.getNumberPlate());
        return car;
    }

    public RestCar getRestCar() {
        RestCar restCar = new RestCar();
        restCar.setNumberPlate("ABC" + new Random().nextInt());
        return restCar;
    }

    public void getCars() {
        RestCars restCars = restTemplate.getForObject(
            "http://localhost:8080/car", RestCars.class);
        log.info("All cars: {}",
            restCars.getRestCars().stream().map(x -> x.getNumberPlate())
                .collect(Collectors.joining(";")));
    }

    public RestCustomer registerCustomer() {
        RestCustomer restCustomer = getRestCustomer();

        RestCustomer customer = restTemplate.postForObject(
            "http://localhost:8080/customer/register", restCustomer, RestCustomer.class);
        log.info("Customer with username {} has been registered successfully! ", customer.getUserName());
        return customer;
    }

    public RestCustomer getRestCustomer() {
        RestCustomer restCustomer = new RestCustomer();
        restCustomer.setUserName("ABC" + new Random().nextInt());
        restCustomer.setPassword("password");
        return restCustomer;
    }
}
