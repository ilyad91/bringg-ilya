package com.candidate.bringg_model;

public class BringgOrderResponseDTO {

    private boolean success;
    private BringgOrderDTO task;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BringgOrderDTO getTask() {
        return task;
    }

    public void setTask(BringgOrderDTO task) {
        this.task = task;
    }
}
