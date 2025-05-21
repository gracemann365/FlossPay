package com.openpay.api.dto;

import java.math.BigDecimal;

import com.openpay.api.validation.ValidUpi;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public class PaymentRequest {

    @ValidUpi
    @NotBlank(message = "Sender UPI is required and must be a valid UPI ID")
    private String senderUpi;

    @ValidUpi
    @NotBlank(message = "Receiver UPI is required and must be a valid UPI ID")
    private String receiverUpi;

    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @NotBlank(message = "Amount is required and must be greater than 0")
    private BigDecimal amount;

    // Getters & Setters

    public String getSenderUpi() {
        return senderUpi;
    }

    public void setSenderUpi(String senderUpi) {
        this.senderUpi = senderUpi;
    }

    public String getReceiverUpi() {
        return receiverUpi;
    }

    public void setReceiverUpi(String receiverUpi) {
        this.receiverUpi = receiverUpi;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
