package com.business.model.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CountryResponse {

    private String name;
    private String countryCode;
    private Timestamp dateCreated;
}
