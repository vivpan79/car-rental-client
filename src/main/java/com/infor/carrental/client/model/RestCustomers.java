package com.infor.carrental.client.model;

import java.util.List;

public class RestCustomers {

    private List<RestCustomer> restCustomers;

    public RestCustomers() {
    }

    public RestCustomers(List<RestCustomer> restCustomers) {
        this.restCustomers = restCustomers;
    }

    public List<RestCustomer> getRestCustomers() {
        return restCustomers;
    }

    public void setRestCustomers(List<RestCustomer> restCustomers) {
        this.restCustomers = restCustomers;
    }
}
