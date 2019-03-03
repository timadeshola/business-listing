package com.business.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    private Long id;

    @Size(min = 3, message = "minimum length is 3")
    private String firstName;

    @Size(min = 3, message = "minimum length is 3")
    private String lastName;

    @Email
    private String email;

    private List<Long> roleIds;
}
