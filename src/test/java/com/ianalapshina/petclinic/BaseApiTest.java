package com.ianalapshina.petclinic;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
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
