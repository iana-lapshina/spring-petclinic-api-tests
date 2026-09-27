# Spring PetClinic API Tests

API test automation project for the Spring PetClinic REST application.

## Tech Stack

-   Java 17
-   Maven 3.9.9
-   Spring Boot
-   JUnit 5
-   RestAssured
-   Hamcrest
-   Jackson
-   Allure
-   Docker

## Prerequisites

-   Java 17+
-   Docker

Maven installation is not required because the project includes Maven
Wrapper.

## Run Spring PetClinic REST

Start the application using Docker:

``` bash
docker run --rm -p 9966:9966 springcommunity/spring-petclinic-rest
```

The application will be available at:

``` text
http://localhost:9966/petclinic
```

Swagger UI:

``` text
http://localhost:9966/petclinic/swagger-ui.html
```

## Run Tests

Using Maven:

``` bash
mvn clean test
```

Using Maven Wrapper on Windows:

``` bash
.\mvnw.cmd clean test
```

Using Maven Wrapper on Linux / macOS:

``` bash
./mvnw clean test
```

By default, tests use:

``` text
http://localhost:9966/petclinic
```

## Override Base URL

The base URL can be overridden using the `baseUrl` system property:

``` bash
mvn clean test -DbaseUrl=http://localhost:9966/petclinic
```

With Maven Wrapper on Windows:

``` bash
.\mvnw.cmd clean test -DbaseUrl=http://localhost:9966/petclinic
```

With Maven Wrapper on Linux / macOS:

``` bash
./mvnw clean test -DbaseUrl=http://localhost:9966/petclinic
```

## Test Coverage

The project contains automated tests for:

-   Health check:
  -   verifies HTTP status `200`
  -   verifies that application status is `UP`
-   Owner CRUD flow:
  -   creates an owner
  -   retrieves the created owner by ID
  -   updates the owner
  -   verifies the updated data
  -   deletes the owner
  -   verifies that the deleted owner returns `404`
-   Owner validation:
  -   verifies validation errors when creating an owner with invalid
      data
  -   verifies validation errors when updating an owner with invalid
      data
  -   verifies invalid telephone formats using parameterized tests
-   Error handling:
  -   verifies `404` when requesting a non-existing owner

## Logging

Request and response details are automatically logged when a RestAssured
validation fails.

## Allure

Allure is configured for test reporting.

Test results are generated in:

``` text
target/allure-results
```

API operations are annotated with Allure `@Step` annotations to provide
readable test steps in the report.
