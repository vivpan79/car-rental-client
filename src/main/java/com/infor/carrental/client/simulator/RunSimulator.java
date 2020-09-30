package com.infor.carrental.client.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RunSimulator {

    private static final Logger log = LoggerFactory.getLogger(RunSimulator.class);
    @Autowired
    private CustomerSimulator customerSimulator;
    @Autowired
    private UserSimulator userSimulator;
    @Autowired
    private RestTemplate restTemplate;

    public RunSimulator() {
    }

    @Async
    public void testCarRentalApplication() throws InterruptedException {
        regressionTest();

        for (int i = 0; i < 5; i++) {
            userSimulator.userKeepsRegisteringCarsAndGettingReports();
        }
        for (int i = 0; i < 10; i++) {
            customerSimulator.userKeepsBookingCars();
        }
    }

    @Async
    public void regressionTest() {
        userSimulator.registerCars();
        userSimulator.getCars();
        userSimulator.registerCustomer();
        userSimulator.getCustomers();
        userSimulator.registerAvailability();
        userSimulator.getAvailability();
        customerSimulator.registerBooking();
        userSimulator.getBooking();
        userSimulator.findBookedCars();
        userSimulator.getCarBookingFrequency();
        userSimulator.getCarBookingPayment();
    }
}
