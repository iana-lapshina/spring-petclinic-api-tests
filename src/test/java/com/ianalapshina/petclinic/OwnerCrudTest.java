package com.ianalapshina.petclinic;

import com.ianalapshina.petclinic.generator.OwnerGenerator;
import com.ianalapshina.petclinic.model.Owner;
import com.ianalapshina.petclinic.stepdefs.OwnerSteps;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OwnerCrudTest extends BaseApiTest {

    private final OwnerSteps ownerSteps = new OwnerSteps();
    private Integer createdOwnerId;

    @AfterEach
    void cleanUp() {
        if (createdOwnerId != null) {
            ownerSteps.deleteOwner(createdOwnerId);
        }
    }

    @Test
    @DisplayName("CRUD владельца: создание, получение, обновление и удаление")
    void ownerCrud() {

        Owner owner = OwnerGenerator.validOwner();

        Response createResponse = ownerSteps.createOwner(owner);
        assertAll("Проверка создания владельца",
                () -> assertEquals(201, createResponse.statusCode()),
                () -> assertNotNull(createResponse.jsonPath().get("id")),
                () -> assertEquals(owner.firstName(), createResponse.jsonPath().getString("firstName")),
                () -> assertEquals(owner.lastName(), createResponse.jsonPath().getString("lastName")),
                () -> assertEquals(owner.address(), createResponse.jsonPath().getString("address")),
                () -> assertEquals(owner.city(), createResponse.jsonPath().getString("city")),
                () -> assertEquals(owner.telephone(), createResponse.jsonPath().getString("telephone"))
        );

        createdOwnerId = createResponse.jsonPath().getInt("id");

        Response getResponse = ownerSteps.getOwner(createdOwnerId);
        assertAll("Проверка созданного владельца",
                () -> assertEquals(200, getResponse.statusCode()),
                () -> assertEquals(createdOwnerId, getResponse.jsonPath().getInt("id")),
                () -> assertEquals(owner.firstName(), getResponse.jsonPath().getString("firstName")),
                () -> assertEquals(owner.lastName(), getResponse.jsonPath().getString("lastName")),
                () -> assertEquals(owner.address(), getResponse.jsonPath().getString("address")),
                () -> assertEquals(owner.city(), getResponse.jsonPath().getString("city")),
                () -> assertEquals(owner.telephone(), getResponse.jsonPath().getString("telephone"))
        );

        Owner updatedOwner = OwnerGenerator.updatedOwner();

        Response updateResponse = ownerSteps.updateOwner(createdOwnerId, updatedOwner);
        assertEquals(204, updateResponse.statusCode());

        Response updatedResponse = ownerSteps.getOwner(createdOwnerId);
        assertAll("Проверка данных после обновления владельца",
                () -> assertEquals(200, updatedResponse.statusCode()),
                () -> assertEquals(createdOwnerId, updatedResponse.jsonPath().getInt("id")),
                () -> assertEquals(updatedOwner.firstName(), updatedResponse.jsonPath().getString("firstName")),
                () -> assertEquals(updatedOwner.lastName(), updatedResponse.jsonPath().getString("lastName")),
                () -> assertEquals(updatedOwner.address(), updatedResponse.jsonPath().getString("address")),
                () -> assertEquals(updatedOwner.city(), updatedResponse.jsonPath().getString("city")),
                () -> assertEquals(updatedOwner.telephone(), updatedResponse.jsonPath().getString("telephone"))
        );

        Response deleteResponse = ownerSteps.deleteOwner(createdOwnerId);
        assertEquals(204, deleteResponse.statusCode());

        Response deletedOwnerResponse = ownerSteps.getOwner(createdOwnerId);
        assertEquals(404, deletedOwnerResponse.statusCode());
    }

    @Test
    @DisplayName("Создание владельца с невалидными данными возвращает ошибку валидации")
    void createInvalidOwner() {
        Owner invalidOwner = OwnerGenerator.invalidOwner();

        Response response = ownerSteps.createOwner(invalidOwner);
        assertAll("Проверка ошибки валидации при создании владельца",
                () -> assertEquals(400, response.statusCode()),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertEquals("The request contains invalid or missing parameters", response.jsonPath().getString("detail")),
                () -> assertNotNull(response.jsonPath().get("schemaValidationErrors"))
        );
    }

    @Test
    @DisplayName("Получение несуществующего владельца возвращает 404")
    void getNonExistingOwner() {
        int ownerId = 999999;

        Response response = ownerSteps.getOwner(ownerId);
        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Обновление владельца невалидными данными возвращает ошибку валидации")
    void updateOwnerWithInvalidData() {
        Owner owner = OwnerGenerator.validOwner();

        Response createResponse = ownerSteps.createOwner(owner);
        createdOwnerId = createResponse.jsonPath().getInt("id");

        Owner invalidOwner = OwnerGenerator.invalidOwner();

        Response updateResponse = ownerSteps.updateOwner(createdOwnerId, invalidOwner);
        assertAll("Проверка ошибки валидации при обновлении владельца",
                () -> assertEquals(400, updateResponse.statusCode()),
                () -> assertEquals(400, updateResponse.jsonPath().getInt("status")),
                () -> assertEquals("The request contains invalid or missing parameters", updateResponse.jsonPath().getString("detail")),
                () -> assertNotNull(updateResponse.jsonPath().get("schemaValidationErrors"))
        );
    }

    @ParameterizedTest(name = "Некорректный телефон: {0}")
    @ValueSource(strings = {"abc123", "123abc", "123-456", "+79991234567"})
    @DisplayName("Создание владельца с некорректным форматом телефона возвращает ошибку валидации")
    void createOwnerWithInvalidTelephone(String telephone) {
        Owner owner = new Owner(
                "Iana",
                "Test",
                "Test Street 1",
                "Moscow",
                telephone
        );

        Response response = ownerSteps.createOwner(owner);
        assertAll("Проверка валидации телефона",
                () -> assertEquals(400, response.statusCode()),
                () -> assertEquals(400, response.jsonPath().getInt("status")),
                () -> assertEquals("The request contains invalid or missing parameters", response.jsonPath().getString("detail")),
                () -> assertNotNull(response.jsonPath().get("schemaValidationErrors"))
        );
    }
}
