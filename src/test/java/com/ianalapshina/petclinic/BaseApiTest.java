package com.ianalapshina.petclinic;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {

    private static final String DEFAULT_BASE_URL =
            "http://localhost:9966/petclinic";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = System.getProperty(
                "baseUrl",
                DEFAULT_BASE_URL
        );

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
