package com.ianalapshina.petclinic.generator;

import com.ianalapshina.petclinic.model.Owner;

public class OwnerGenerator {

    public static Owner validOwner() {
        return new Owner(
                "Iana",
                "Test",
                "Test Street 1",
                "Moscow",
                "1234567890"
        );
    }

    public static Owner updatedOwner() {
        return new Owner(
                "Iana",
                "Updated",
                "New Street 10",
                "Madrid",
                "9876543210"
        );
    }

    public static Owner invalidOwner() {
        return new Owner(
                "",
                "",
                "",
                "",
                ""
        );
    }
}
