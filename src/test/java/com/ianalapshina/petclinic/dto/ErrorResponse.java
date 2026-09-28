package com.ianalapshina.petclinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorResponse(
        Integer status,
        String detail,
        List<Object> schemaValidationErrors
) {
}
