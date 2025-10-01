package com.myProject.SpringSalesApp.integrationtests.controllers.withJson;

import com.myProject.SpringSalesApp.config.TestConfigs;
import com.myProject.SpringSalesApp.integrationtests.dto.AccountCredentialsDTO;
import com.myProject.SpringSalesApp.integrationtests.dto.TokenDTO;
import com.myProject.SpringSalesApp.integrationtests.testcontainers.AbstractIntagrationTest;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class AuthControllerJsonTest extends AbstractIntagrationTest {

    private static TokenDTO tokenDTO;


    @BeforeAll
    // vai criar apenas 1 instancia para o test todoo, o BeforeEach iria criar uma instancia para cada metodo.
    static void setUp() {
        tokenDTO = new TokenDTO();
    }

    @Order(1)
    @Test
    void signin() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("marcio", "admin123");

        tokenDTO = given()
                .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                    .when()
                .post()
                    .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
    @Order(2)
    @Test
    void refreshToken() {
        tokenDTO = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("username", tokenDTO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                    .put("{username}")
                        .then()
                        .statusCode(200)
                                .extract()
                                .body()
                                .as(TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}