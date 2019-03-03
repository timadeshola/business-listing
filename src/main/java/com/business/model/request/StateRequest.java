package com.business.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class StateRequest {

    @NotNull
    private String name;

    @NotNull
    @Size(min = 2, max = 3, message = "minimum character is 2 and maximum is 3")
    private String stateCode;

}
