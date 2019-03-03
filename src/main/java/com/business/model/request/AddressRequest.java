package com.business.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class AddressRequest {

    @NotNull
    private String houseNo;

    @NotNull
    private String street;

    @NotNull
    private String city;

    @NotNull
    private Long stateId;

    @NotNull
    private Long countryId;
}
