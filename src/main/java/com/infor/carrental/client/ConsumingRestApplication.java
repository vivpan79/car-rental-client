package com.infor.carrental.client;

import static java.time.LocalDateTime.now;
import static java.time.format.DateTimeFormatter.ofPattern;

import com.infor.carrental.client.model.RestAvailabilities;
import com.infor.carrental.client.model.RestAvailability;
import com.infor.carrental.client.model.RestBooking;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumingRestApplication {

    private static final Logger log = LoggerFactory.getLogger(ConsumingRestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumingRestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            registerCars(restTemplate);
            getCars(restTemplate);
            registerCustomer(restTemplate);
            getCustomers(restTemplate);
            registerAvailability(restTemplate);
            getAvailability(restTemplate);
            registerBooking(restTemplate);
            getBooking(restTemplate);
        };
    }

    private RestCar registerCars(RestTemplate restTemplate) {
        RestCar restCar = getRestCar();

        RestCar car = restTemplate.postForObject(
            "http://localhost:8080/car/register", restCar, RestCar.class);
        log.info("Car with numberplate {} has been registered successfully! ", car.getNumberPlate());
        return car;
    }

    private RestCar getRestCar() {
        RestCar restCar = new RestCar();
        restCar.setNumberPlate("ABC" + new Random().nextInt());
        return restCar;
    }

    private void getCars(RestTemplate restTemplate) {
        RestCars restCars = restTemplate.getForObject(
            "http://localhost:8080/car", RestCars.class);
        log.info(restCars.getRestCars().stream().map(x -> x.getNumberPlate()).collect(Collectors.joining()));
    }

    private RestCustomer registerCustomer(RestTemplate restTemplate) {
        RestCustomer restCustomer = getRestCustomer();

        RestCustomer customer = restTemplate.postForObject(
            "http://localhost:8080/customer/register", restCustomer, RestCustomer.class);
        log.info("Customer with username {} has been registered successfully! ", customer.getUserName());
        return customer;
    }

    private RestCustomer getRestCustomer() {
        RestCustomer restCustomer = new RestCustomer();
        restCustomer.setUserName("ABC" + new Random().nextInt(1000));
        restCustomer.setPassword("password");
        return restCustomer;
    }

    private void getCustomers(RestTemplate restTemplate) {
        RestCustomers restCustomers = restTemplate.getForObject(
            "http://localhost:8080/customer", RestCustomers.class);
        log.info(restCustomers.getRestCustomers().stream().map(x -> x.getUserName()).collect(Collectors.joining()));
    }

    private void registerAvailability(RestTemplate restTemplate) {
        String numberPlate = registerCars(restTemplate).getNumberPlate();
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

    private void getAvailability(RestTemplate restTemplate) {
        RestAvailabilities availabilities = restTemplate.getForObject(
            "http://localhost:8080/availability", RestAvailabilities.class);
        log.info(availabilities.getRestAvailabilityList().stream().map(x -> x.getCar().getNumberPlate())
            .collect(Collectors.joining()));
    }

    private void registerBooking(RestTemplate restTemplate) {
        String numberPlate = registerCars(restTemplate).getNumberPlate();
        String userName = registerCustomer(restTemplate).getUserName();
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
        RestBooking booking = restTemplate.postForObject(
            "http://localhost:8080/booking/car/"
                + numberPlate
                + "/register/from/" + formatter.format(date)
                + "/to/" + formatter.format(date.plusHours(6L))
                + "/for/" + userName, "null", RestBooking.class);
        log.info("Booking for car with number plate {} has been registered successfully! ",
            booking.getCar().getNumberPlate());
    }

    private void getBooking(RestTemplate restTemplate) {
        RestBookings restBookings = restTemplate.getForObject(
            "http://localhost:8080/booking", RestBookings.class);
        log.info(restBookings.getRestBookings().stream().map(x -> x.getCar().getNumberPlate())
            .collect(Collectors.joining()));
    }
}
