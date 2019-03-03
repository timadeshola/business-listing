package com.business.model.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class StateResponse {

    private String name;
    private String stateCode;
    private Timestamp dateCreated;
}
