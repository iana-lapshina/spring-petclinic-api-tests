package com.ianalapshina.petclinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OwnerResponse(
        Integer id,
        String firstName,
        String lastName,
        String address,
        String city,
        String telephone
) {
}
