package com.candidate.bringg_model;

public class BringgCustomerResponseDTO {

    private boolean success;
    private BringgCustomerDTO bringgCustomerDTO;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BringgCustomerDTO getBringgCustomerDTO() {
        return bringgCustomerDTO;
    }

    public void setBringgCustomerDTO(BringgCustomerDTO bringgCustomerDTO) {
        this.bringgCustomerDTO = bringgCustomerDTO;
    }
}
