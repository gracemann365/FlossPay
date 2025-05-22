package com.openpay.shared.dto;

public class StatusResponse {
    private Long id;
    private String status;
    private String message;

    public StatusResponse() {}

    public StatusResponse(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    // Add this 3-argument constructor:
    public StatusResponse(Long id, String status, String message) {
        this.id = id;
        this.status = status;
        this.message = message;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
