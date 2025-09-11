package com.myProject.SpringSalesApp.integrationtests.controllers.withJson;

import com.myProject.SpringSalesApp.config.TestConfigs;
import com.myProject.SpringSalesApp.integrationtests.dto.ProductDTO;
import com.myProject.SpringSalesApp.integrationtests.testcontainers.AbstractIntagrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
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
class ProductControllerJsonTest extends AbstractIntagrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static ProductDTO product4;


    @BeforeAll
    // vai criar apenas 1 instancia para o test todoo, o BeforeEach iria criar uma instancia para cada metodo.
    static void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);// nao da falha quando o Json não trás links HATEOAS.

        product4 = new ProductDTO();
    }

    @Order(1)
    @Test
    void insert() throws IOException {
        mockProduct(1);
        setSpecification();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product4)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

            product4 = objectMapper.readValue(content, ProductDTO.class);
            verifyAssertNull();
            verifyAssertEquals(1);
    }

    @Order(2)
    @Test
    void findById() throws IOException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", product4.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

            product4 = objectMapper.readValue(content, ProductDTO.class);
            verifyAssertNull();
            verifyAssertEquals(1);
    }

    @Order(3)
    @Test
    void updateById() throws IOException {
        mockProduct(2);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", product4.getId())
                .body(product4)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

            product4 = objectMapper.readValue(content, ProductDTO.class);
            verifyAssertNull();
            verifyAssertEquals(2);
    }
    @Order(4)
    @Test
    void disableProduct() throws IOException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", product4.getId())
                .when()
                .patch("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        product4 = objectMapper.readValue(content, ProductDTO.class);
        assertFalse(product4.getEnabled());

        product4.setEnabled(true);
        verifyAssertNull();
        verifyAssertEquals(2);
    }

    @Order(5)
    @Test
    void deleteById() {
        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", product4.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204);
    }

    @Order(6)
    @Test
    void findAll() throws IOException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

            List<ProductDTO> products = objectMapper.readValue(content, new TypeReference<List<ProductDTO>>() {});
            product4 = products.get(0);
            verifyAssertNull();
            assertEquals("The Lord of the Rings", product4.getName());
            assertEquals("Lorem ipsum dolor sit amet, consectetur.", product4.getDescription());
            assertEquals(90.5, product4.getPrice());
            assertEquals("", product4.getImgUrl());

            product4 = products.get(4);
            verifyAssertNull();
            assertEquals("Rails for Dummies", product4.getName());
            assertEquals("Cras fringilla convallis sem vel faucibus.", product4.getDescription());
            assertEquals(100.99, product4.getPrice());
            assertEquals("", product4.getImgUrl());
    }

    private void mockProduct(Integer n) {
        product4.setName("Notebook "+ n);
        product4.setDescription("Notebook de " + n + "0 polegadas");
        product4.setPrice(n * 1000.00);
        product4.setImgUrl("https://img.com/notebook" + n +".png");
        product4.setEnabled(true);
    }
    private void setSpecification(){
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN,TestConfigs.ORIGIN_MARCIOCLEY)
                .setBasePath("/products")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }
    private void verifyAssertEquals(Integer n){
        assertEquals("Notebook "+n, product4.getName());
        assertEquals("Notebook de "+ n +"0 polegadas", product4.getDescription());
        assertEquals(n * 1000.00, product4.getPrice());
        assertEquals("https://img.com/notebook"+ n + ".png", product4.getImgUrl());
    }
    private void verifyAssertNull(){
        assertNotNull(product4.getId());
        assertNotNull(product4.getName());
        assertNotNull(product4.getDescription());
        assertNotNull(product4.getPrice());
        assertNotNull(product4.getImgUrl());
        assertTrue(product4.getId() > 0);
        assertTrue(product4.getEnabled());
    }

}