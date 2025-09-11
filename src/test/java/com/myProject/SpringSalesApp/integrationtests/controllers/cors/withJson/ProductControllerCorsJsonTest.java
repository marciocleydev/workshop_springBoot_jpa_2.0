package com.myProject.SpringSalesApp.integrationtests.controllers.cors.withJson;

import com.myProject.SpringSalesApp.config.TestConfigs;
import com.myProject.SpringSalesApp.integrationtests.dto.ProductDTO;
import com.myProject.SpringSalesApp.integrationtests.testcontainers.AbstractIntagrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)// JUnit faz os testes aleatoriamente, essa anotação diz para ele seguir uma ordem que será definida
class ProductControllerCorsJsonTest extends AbstractIntagrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static ProductDTO productDTO;


    @BeforeAll
    // vai criar apenas 1 instancia para o test todoo, o BeforeEach iria criar uma instancia para cada metodo.
    static void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);// nao da falha quando o Json não trás links HATEOAS.

        productDTO = new ProductDTO();
    }

    @Order(1)
    @ParameterizedTest  // parametriza mais de 1 test, nesse caso vai testar duas origens diferentes. ou seja 2 testes
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 201",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void insert(String origin, Integer expectedStatus) throws IOException {
        mockProduct(1);
        setSpecification(origin);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(productDTO)
                .when()
                .post()
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 201) {
            productDTO = objectMapper.readValue(content, ProductDTO.class);
            verifyAssertNull();
            verifyAssertEquals(1);
        }
        else {
            assertEquals("Invalid CORS request", content);
        }
    }

    @Order(2)
    @ParameterizedTest
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 200",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void findById(String origin, Integer expectedStatus) throws IOException {
        setSpecification(origin);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", productDTO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 200) {
            productDTO = objectMapper.readValue(content, ProductDTO.class);
            verifyAssertNull();
            verifyAssertEquals(1);
        }
        else {
            assertEquals("Invalid CORS request", content);
        }
    }

    @Order(3)
    @ParameterizedTest
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 200",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void updateById(String origin, Integer expectedStatus) throws IOException {
        setSpecification(origin);

        //update product
        mockProduct(2);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", productDTO.getId())
                .body(productDTO)
                .when()
                .put("/{id}")
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 200) {
            productDTO = objectMapper.readValue(content, ProductDTO.class);
            verifyAssertNull();
            verifyAssertEquals(2);
        }
        else {
            assertEquals("Invalid CORS request", content);
        }
    }

    @Order(4)
    @ParameterizedTest
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 204",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void deleteById(String origin, Integer expectedStatus) {
        setSpecification(origin);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", productDTO.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 204) {
            assertEquals("", content);
        }
        else {
            assertEquals("Invalid CORS request", content);
        }

    }

    @Order(5)
    @ParameterizedTest
    @CsvSource({
            TestConfigs.ORIGIN_MARCIOCLEY + ", 200",
            TestConfigs.ORIGIN_GOOGLE + ", 403"
    })
    void findAll(String origin, Integer expectedStatus) throws IOException {
        setSpecification(origin);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get()
                .then()
                .statusCode(expectedStatus)
                .extract()
                .body()
                .asString();

        if(expectedStatus == 200) {
            List<ProductDTO> products = objectMapper.readValue(content, new TypeReference<List<ProductDTO>>() {});
            productDTO = products.getFirst();
            verifyAssertNull();
            assertEquals("The Lord of the Rings", productDTO.getName());
            assertEquals("Lorem ipsum dolor sit amet, consectetur.", productDTO.getDescription());
            assertEquals(90.5, productDTO.getPrice());
            assertEquals("", productDTO.getImgUrl());
        }
        else {
            assertEquals("Invalid CORS request", content);
        }
    }

    private void mockProduct(Integer n) {
        productDTO.setName("Notebook "+ n);
        productDTO.setDescription("Notebook de " + n + "0 polegadas");
        productDTO.setPrice(n * 1000.00);
        productDTO.setImgUrl("https://img.com/notebook" + n +".png");
        productDTO.setEnabled(true);
    }
    private void setSpecification(String origin){
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, origin)
                .setBasePath("/products")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }
    private void verifyAssertEquals(Integer n){
        assertEquals("Notebook "+n, productDTO.getName());
        assertEquals("Notebook de "+ n +"0 polegadas", productDTO.getDescription());
        assertEquals(n * 1000.00, productDTO.getPrice());
        assertEquals("https://img.com/notebook"+ n + ".png", productDTO.getImgUrl());
    }
    private void verifyAssertNull(){
        assertNotNull(productDTO.getId());
        assertNotNull(productDTO.getName());
        assertNotNull(productDTO.getDescription());
        assertNotNull(productDTO.getPrice());
        assertNotNull(productDTO.getImgUrl());
        assertTrue(productDTO.getId() > 0);
        assertTrue(productDTO.getEnabled());
    }

}