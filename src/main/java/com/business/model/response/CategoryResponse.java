package com.business.model.response;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CategoryResponse {

    private String name;
    private Timestamp dateCreated;
}
