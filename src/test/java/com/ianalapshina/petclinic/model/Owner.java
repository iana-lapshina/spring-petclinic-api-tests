package com.ianalapshina.petclinic.model;

public record Owner(
        String firstName,
        String lastName,
        String address,
        String city,
        String telephone
) {
}
