package com.jeffjackson.enquiry.model;

public class DepositPayload {
    private Double amount;
    private boolean isDeposit;

    public DepositPayload() {
    }

    public DepositPayload(Double amount, boolean isDeposit) {
        this.amount = amount;
        this.isDeposit = isDeposit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public boolean isDeposit() {
        return isDeposit;
    }

    public void setDeposit(boolean deposit) {
        isDeposit = deposit;
    }
}
