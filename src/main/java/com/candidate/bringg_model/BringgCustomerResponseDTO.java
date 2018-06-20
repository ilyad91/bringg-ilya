package com.candidate.bringg_model;

public class BringgCustomerResponseDTO {

    private boolean success;
    private BringgCustomerDTO customer;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BringgCustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(BringgCustomerDTO customer) {
        this.customer = customer;
    }
}
