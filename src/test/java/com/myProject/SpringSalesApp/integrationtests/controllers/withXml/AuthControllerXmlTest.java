package com.myProject.SpringSalesApp.integrationtests.controllers.withXml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

class AuthControllerXmlTest extends AbstractIntagrationTest {

    private static XmlMapper objectMapper;

    private static TokenDTO tokenDTO;


    @BeforeAll
    // vai criar apenas 1 instancia para o test todoo, o BeforeEach iria criar uma instancia para cada metodo.
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// nao da falha quando o Json não trás links HATEOAS.
        tokenDTO = new TokenDTO();
    }

    @Order(1)
    @Test
    void signin() throws JsonProcessingException {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO("marcio", "admin123");

        var content = given()
                .basePath("/auth/signin")
                    .port(TestConfigs.SERVER_PORT)
                    .contentType(MediaType.APPLICATION_XML_VALUE)
                    .accept(MediaType.APPLICATION_XML_VALUE)
                .body(credentials)
                    .when()
                .post()
                    .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .asString();

        tokenDTO = objectMapper.readValue(content, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
    @Order(2)
    @Test
    void refreshToken() throws JsonProcessingException {
        var content = given()
                .basePath("/auth/refresh")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                    .pathParam("username", tokenDTO.getUsername())
                    .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getRefreshToken())
                .when()
                    .put("{username}")
                        .then()
                        .statusCode(200)
                                .extract()
                                .body()
                                .asString();

        tokenDTO = objectMapper.readValue(content, TokenDTO.class);

        assertNotNull(tokenDTO.getAccessToken());
        assertNotNull(tokenDTO.getRefreshToken());
    }
}