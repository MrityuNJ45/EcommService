package com.ecommerceapp.userservice.exceptions;

import java.time.LocalDateTime;

public class ErrorDetails {

    private LocalDateTime timeStamp;

    private String message ;

    private String description;



    public ErrorDetails() {
        super();
    }

    public ErrorDetails(LocalDateTime timeStamp, String message, String discription) {
        super();
        this.timeStamp = timeStamp;
        this.message = message;
        this.description = discription;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
