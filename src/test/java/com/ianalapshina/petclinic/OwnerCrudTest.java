package com.ianalapshina.petclinic;

import com.ianalapshina.petclinic.dto.ErrorResponse;
import com.ianalapshina.petclinic.generator.OwnerGenerator;
import com.ianalapshina.petclinic.model.Owner;
import com.ianalapshina.petclinic.stepdefs.OwnerSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import com.ianalapshina.petclinic.dto.OwnerResponse;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OwnerCrudTest extends BaseApiTest {

    @Autowired
    private OwnerSteps ownerSteps;
    private final Set<Integer> createdOwnerIds = new HashSet<>();

    private OwnerResponse createOwnerAndTrack(Owner owner) {
        OwnerResponse createdOwner = ownerSteps.createOwner(owner);
        createdOwnerIds.add(createdOwner.id());
        return createdOwner;
    }

    @AfterEach
    void cleanUp() {
        for (Integer ownerId : createdOwnerIds) {
            ownerSteps.deleteOwnerIfExists(ownerId);
        }

        createdOwnerIds.clear();
    }

    @Test
    @DisplayName("CRUD владельца: создание, получение, обновление и удаление")
    void ownerCrud() {

        Owner owner = OwnerGenerator.validOwner();

        OwnerResponse createdOwner = createOwnerAndTrack(owner);
        Integer createdOwnerId = createdOwner.id();

        assertAll("Проверка создания владельца",
                () -> assertNotNull(createdOwner.id()),
                () -> assertEquals(owner.firstName(), createdOwner.firstName()),
                () -> assertEquals(owner.lastName(), createdOwner.lastName())
        );

        OwnerResponse receivedOwner = ownerSteps.getOwner(createdOwnerId);

        assertAll("Проверка получения владельца",
                () -> assertEquals(createdOwnerId, receivedOwner.id()),
                () -> assertEquals(owner.firstName(), receivedOwner.firstName()),
                () -> assertEquals(owner.lastName(), receivedOwner.lastName()),
                () -> assertEquals(owner.address(), receivedOwner.address()),
                () -> assertEquals(owner.city(), receivedOwner.city()),
                () -> assertEquals(owner.telephone(), receivedOwner.telephone())
        );

        Owner updatedOwner = OwnerGenerator.updatedOwner();

        ownerSteps.updateOwner(createdOwnerId, updatedOwner);

        OwnerResponse updatedResponse = ownerSteps.getOwner(createdOwnerId);

        assertAll("Проверка данных после обновления владельца",
                () -> assertEquals(createdOwnerId, updatedResponse.id()),
                () -> assertEquals(updatedOwner.firstName(), updatedResponse.firstName()),
                () -> assertEquals(updatedOwner.lastName(), updatedResponse.lastName()),
                () -> assertEquals(updatedOwner.address(), updatedResponse.address()),
                () -> assertEquals(updatedOwner.city(), updatedResponse.city()),
                () -> assertEquals(updatedOwner.telephone(), updatedResponse.telephone())
        );

        ownerSteps.deleteOwner(createdOwnerId);
        createdOwnerIds.remove(createdOwnerId);

        ownerSteps.getNonExistingOwner(createdOwnerId);
    }

    @Test
    @DisplayName("Создание владельца с невалидными данными возвращает ошибку валидации")
    void createInvalidOwner() {
        Owner invalidOwner = OwnerGenerator.invalidOwner();

        ErrorResponse error = ownerSteps.createInvalidOwner(invalidOwner);

        assertAll("Проверка ошибки валидации",
                () -> assertEquals(400, error.status()),
                () -> assertEquals("The request contains invalid or missing parameters", error.detail()),
                () -> assertNotNull(error.schemaValidationErrors())
        );
    }

    @Test
    @DisplayName("Получение несуществующего владельца возвращает 404")
    void getNonExistingOwner() {
        int ownerId = 999999;

        ownerSteps.getNonExistingOwner(ownerId);
    }

    @Test
    @DisplayName("Обновление владельца невалидными данными возвращает ошибку валидации")
    void updateOwnerWithInvalidData() {
        Owner owner = OwnerGenerator.validOwner();

        OwnerResponse createdOwner = createOwnerAndTrack(owner);
        Integer createdOwnerId = createdOwner.id();

        Owner invalidOwner = OwnerGenerator.invalidOwner();

        ErrorResponse error = ownerSteps.updateOwnerWithInvalidData(createdOwnerId, invalidOwner);

        assertAll("Проверка ошибки валидации при обновлении владельца",
                () -> assertEquals(400, error.status()),
                () -> assertEquals("The request contains invalid or missing parameters", error.detail()),
                () -> assertNotNull(error.schemaValidationErrors())
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

        ErrorResponse error = ownerSteps.createInvalidOwner(owner);
        assertAll("Проверка валидации телефона",
                () -> assertEquals(400, error.status()),
                () -> assertEquals("The request contains invalid or missing parameters", error.detail()),
                () -> assertNotNull(error.schemaValidationErrors())
        );
    }
}
