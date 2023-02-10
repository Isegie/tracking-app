package com.is.trackingapp.entity.dto;


import com.is.trackingapp.entity.enums.Status;
import lombok.Builder;

@Builder
public class PersonDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String oib;

    private Status status;


    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s", firstName, lastName,
                oib, status.toString());
    }
}
