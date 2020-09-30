package com.infor.carrental.client.simulator;

import static java.lang.Thread.sleep;
import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

import com.infor.carrental.client.model.RestAvailabilities;
import com.infor.carrental.client.model.RestAvailability;
import com.infor.carrental.client.model.RestBookings;
import com.infor.carrental.client.model.RestCar;
import com.infor.carrental.client.model.RestCars;
import com.infor.carrental.client.model.RestCustomer;
import com.infor.carrental.client.model.RestCustomers;
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
public class UserSimulator {

    private static final Logger log = LoggerFactory.getLogger(UserSimulator.class);
    @Autowired
    private RestTemplate restTemplate;

    public UserSimulator() {
    }

    @Async
    public void userKeepsRegisteringCarsAndGettingReports() throws InterruptedException {
        for (; ; ) {
            log.info("####################userKeepsRegisteringCarsAndGettingReports################");
            registerCars();
            findBookedCars();
            getCarBookingFrequency();
            getCarBookingPayment();
            sleep(1000);
        }
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

    public void findBookedCars() {
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        RestCars restCars = restTemplate.getForObject(
            "http://localhost:8080/booking"
                + "/find/from/" + formatter.format(date)
                + "/to/" + formatter.format(date.plusMonths(6L))
            , RestCars.class);
        log.info("All booked cars between dates: {}",
            restCars.getRestCars().stream().map(x -> x.getNumberPlate())
                .collect(Collectors.joining(";"))
        );
    }

    public void getCarBookingFrequency() {
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        Double frequency = restTemplate.getForObject(
            "http://localhost:8080/booking"
                + "/frequency/from/" + formatter.format(date)
                + "/to/" + formatter.format(date.plusMonths(6L))
            , Double.class);
        log.info("Car booking frequency: {}", frequency);
    }

    public void getCarBookingPayment() {
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        Long payments = restTemplate.getForObject(
            "http://localhost:8080/booking"
                + "/payments/from/" + formatter.format(date)
                + "/to/" + formatter.format(date.plusMonths(6L))
            , Long.class);
        log.info("Total payment from car bookings between dates: {}", payments);
    }

    public void registerAvailability() {
        String numberPlate = registerCars().getNumberPlate();
        LocalDateTime date = now();
        DateTimeFormatter formatter = ofPattern("yyyy-MM-dd'T'HH:mm");
        RestAvailability availability = restTemplate.postForObject(
            "http://localhost:8080/availability/car/"
                + numberPlate
                + "/register/from/" + formatter.format(date)
                + "/to/" + formatter.format(date.plusHours(6L))
                + "/rate/100", "null", RestAvailability.class);
        log.info("Availability for car with number plate {} has been registered successfully! ",
            availability.getCar().getNumberPlate());
    }

    public void getAvailability() {
        RestAvailabilities availabilities = restTemplate.getForObject(
            "http://localhost:8080/availability", RestAvailabilities.class);
        log.info("All available cars: {}",
            availabilities.getRestAvailabilityList().stream().map(x -> x.getCar().getNumberPlate())
                .collect(Collectors.joining(";")));
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

    public void getCustomers() {
        RestCustomers restCustomers = restTemplate.getForObject(
            "http://localhost:8080/customer", RestCustomers.class);
        log.info("All customers: {}",
            restCustomers.getRestCustomers().stream().map(x -> x.getUserName())
                .collect(Collectors.joining(";")));
    }

    public void getBooking() {
        RestBookings restBookings = restTemplate.getForObject(
            "http://localhost:8080/booking", RestBookings.class);
        log.info("All booked cars: {}",
            restBookings.getRestBookings().stream().map(x -> x.getCar().getNumberPlate())
                .collect(Collectors.joining(";")));
    }
}
