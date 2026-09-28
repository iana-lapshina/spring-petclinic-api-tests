package com.ianalapshina.petclinic.stepdefs;

import com.ianalapshina.petclinic.model.Owner;
import io.qameta.allure.Step;
import org.springframework.stereotype.Component;
import com.ianalapshina.petclinic.dto.OwnerResponse;
import com.ianalapshina.petclinic.dto.ErrorResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;


@Component
public class OwnerSteps {

    @Step("Создать владельца")
    public OwnerResponse createOwner(Owner owner) {
        return given()
                .contentType("application/json")
                .body(owner)
                .when()
                .post("/api/owners")
                .then()
                .statusCode(201)
                .extract()
                .as(OwnerResponse.class);
    }

    @Step("Получить владельца по id: {ownerId}")
    public OwnerResponse getOwner(int ownerId) {
        return given()
                .pathParam("ownerId", ownerId)
                .when()
                .get("/api/owners/{ownerId}")
                .then()
                .statusCode(200)
                .extract()
                .as(OwnerResponse.class);
    }

    @Step("Обновить владельца по id: {ownerId}")
    public void updateOwner(int ownerId, Owner owner) {
        given()
                .contentType("application/json")
                .pathParam("ownerId", ownerId)
                .body(owner)
                .when()
                .put("/api/owners/{ownerId}")
                .then()
                .statusCode(204);
    }

    @Step("Удалить владельца по id: {ownerId}")
    public void deleteOwner(int ownerId) {
        given()
                .pathParam("ownerId", ownerId)
                .when()
                .delete("/api/owners/{ownerId}")
                .then()
                .statusCode(204);
    }

    @Step("Создать владельца с невалидными данными")
    public ErrorResponse createInvalidOwner(Owner owner) {
        return given()
                .contentType("application/json")
                .body(owner)
                .when()
                .post("/api/owners")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
    }

    @Step("Проверить, что владелец с id {ownerId} не найден")
    public void getNonExistingOwner(int ownerId) {
        given()
                .pathParam("ownerId", ownerId)
                .when()
                .get("/api/owners/{ownerId}")
                .then()
                .statusCode(404);
    }

    @Step("Обновить владельца по id {ownerId} невалидными данными")
    public ErrorResponse updateOwnerWithInvalidData(int ownerId, Owner owner) {
        return given()
                .contentType("application/json")
                .pathParam("ownerId", ownerId)
                .body(owner)
                .when()
                .put("/api/owners/{ownerId}")
                .then()
                .statusCode(400)
                .extract()
                .as(ErrorResponse.class);
    }

    @Step("Удалить владельца при очистке тестовых данных: {ownerId}")
    public void deleteOwnerIfExists(int ownerId) {
        given()
                .pathParam("ownerId", ownerId)
                .when()
                .delete("/api/owners/{ownerId}")
                .then()
                .statusCode(anyOf(is(204), is(404)));
    }
}

