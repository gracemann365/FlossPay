package com.openpay.api.dto;

import java.math.BigDecimal;

import com.openpay.api.validation.ValidUpi;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @ValidUpi
    @NotBlank(message = "Sender UPI is required and must be a valid UPI ID")
    private String senderUpi;

    @ValidUpi
    @NotBlank(message = "Receiver UPI is required and must be a valid UPI ID")
    private String receiverUpi;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
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
