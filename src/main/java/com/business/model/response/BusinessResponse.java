package com.business.model.response;

import com.business.jpa.entity.Address;
import com.business.jpa.entity.Category;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Data
public class BusinessResponse {

    private String name;
    private String description;
    private Set<Category> categories;
    private Address address;
    private Timestamp dateCreated;
}
