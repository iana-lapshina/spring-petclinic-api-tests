package com.ianalapshina.petclinic.stepdefs;

import com.ianalapshina.petclinic.model.Owner;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class OwnerSteps {

    @Step("Создать владельца")
    public Response createOwner(Owner owner) {
        return given()
                .contentType("application/json")
                .body(owner)
                .when()
                .post("/api/owners");
    }

    @Step("Получить владельца по id: {ownerId}")
    public Response getOwner(int ownerId) {
        return given()
                .pathParam("ownerId", ownerId)
                .when()
                .get("/api/owners/{ownerId}");
    }

    @Step("Обновить владельца по id: {ownerId}")
    public Response updateOwner(int ownerId, Owner owner) {
        return given()
                .contentType("application/json")
                .pathParam("ownerId", ownerId)
                .body(owner)
                .when()
                .put("/api/owners/{ownerId}");
    }

    @Step("Удалить владельца по id: {ownerId}")
    public Response deleteOwner(int ownerId) {
        return given()
                .pathParam("ownerId", ownerId)
                .when()
                .delete("/api/owners/{ownerId}");
    }
}

