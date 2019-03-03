package com.business.model.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AddressResponse {

    private String houseNo;
    private String street;
    private String city;
    private String state;
    private String country;
    private Timestamp dateCreated;
}
