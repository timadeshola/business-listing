package com.business.model.response;

import lombok.Data;

import java.util.Date;

@Data
public class UserResponse {

    private String username;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Boolean status;
    private Date dateCreated;
}
