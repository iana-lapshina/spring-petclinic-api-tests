package com.ianalapshina.petclinic;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class HealthCheckTest extends BaseApiTest {
    @Test
    void healthCheckShouldReturnUp() {
        given()
                .when()
                .get("/actuator/health")
                .then()
                .statusCode(200)
                .body("status", equalTo("UP"));
    }
}
