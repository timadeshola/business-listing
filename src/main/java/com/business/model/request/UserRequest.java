package com.business.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotNull(message = "username cannot be empty")
    @Size(min = 3, message = "minimum length is 3")
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must have a least a number, uppercase and lower case, special characters")
    @Size(min = 8, message = "minimum length is 8")
    private String password;

    @NotNull(message = "first name cannot be empty")
    @Size(min = 3, message = "minimum length is 3")
    private String firstName;

    @NotNull(message = "last name cannot be empty")
    @Size(min = 3, message = "minimum length is 3")
    private String lastName;

    @Email
    @NotNull
    private String email;

    private List<Long> roleIds;
}
