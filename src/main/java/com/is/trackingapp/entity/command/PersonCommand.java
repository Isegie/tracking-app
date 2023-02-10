package com.is.trackingapp.entity.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
public class PersonCommand {
    @NotBlank(message = "First name must not be empty.")
    @JsonProperty("firstName")
    private String firstName;
    @NotBlank(message = "Last name must not be empty.")
    @JsonProperty("lastName")
    private String lastName;
    @Pattern(message = "OIB should contain 11 digits.", regexp = "[\\d]{11}")
    @NotBlank(message = "OIB must not be empty")
    @JsonProperty("oib")
    private String oib;
    @NotBlank(message = "Status must not be empty.")
    @JsonProperty("status")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
