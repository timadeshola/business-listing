package com.business.model.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RoleResponse {

    private String name;
    private Boolean status;
    private Timestamp dateCreated;
}
