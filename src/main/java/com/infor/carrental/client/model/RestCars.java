package com.infor.carrental.client.model;

import java.util.List;

public class RestCars {

    private List<RestCar> restCars;

    public RestCars() {
    }

    public RestCars(List<RestCar> collect) {
        this.restCars = collect;
    }

    public List<RestCar> getRestCars() {
        return restCars;
    }

    public void setRestCars(List<RestCar> restCars) {
        this.restCars = restCars;
    }
}
