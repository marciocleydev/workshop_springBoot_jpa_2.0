package com.myProject.SpringSalesApp.integrationtests.controllers.withJson;

import com.myProject.SpringSalesApp.config.TestConfigs;
import com.myProject.SpringSalesApp.integrationtests.dto.AccountCredentialsDTO;
import com.myProject.SpringSalesApp.integrationtests.dto.ProductDTO;
import com.myProject.SpringSalesApp.integrationtests.dto.TokenDTO;
import com.myProject.SpringSalesApp.integrationtests.dto.wrappers.json.WrapperProductDTO;
import com.myProject.SpringSalesApp.integrationtests.testcontainers.AbstractIntagrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)// JUnit faz os testes aleatoriamente, essa anotação diz para ele seguir uma ordem que será definida
class ProductControllerJsonTest extends AbstractIntagrationTest {
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static ProductDTO product1;
    private static TokenDTO tokenDTO;


    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);// nao da falha quando o Json não trás links HATEOAS.
        product1 = new ProductDTO();
        tokenDTO = new TokenDTO();
    }

    @Order(0)
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

    @Order(1)
    @Test
    void insert() throws IOException {
        mockProduct(1);
        setSpecification();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product1)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        product1 = objectMapper.readValue(content, ProductDTO.class);
        verifyAssertNull();
        verifyAssertEquals(1);
    }

    @Order(2)
    @Test
    void findById() throws IOException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", product1.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        product1 = objectMapper.readValue(content, ProductDTO.class);
        verifyAssertNull();
        verifyAssertEquals(1);
    }

    @Order(3)
    @Test
    void updateById() throws IOException {
        mockProduct(2);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", product1.getId())
                .body(product1)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        product1 = objectMapper.readValue(content, ProductDTO.class);
        verifyAssertNull();
        verifyAssertEquals(2);
    }

    @Order(4)
    @Test
    void disableProduct() throws IOException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", product1.getId())
                .when()
                .patch("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        product1 = objectMapper.readValue(content, ProductDTO.class);
        assertFalse(product1.getEnabled());

        product1.setEnabled(true);
        verifyAssertNull();
        verifyAssertEquals(2);
    }

    @Order(5)
    @Test
    void deleteById() {
        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", product1.getId())
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
                .queryParams("page", 3, "size", 12, "direction","asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperProductDTO wrapper= objectMapper.readValue(content, WrapperProductDTO.class);
        List<ProductDTO> products = wrapper.getEmbedded().getProducts();

        var product1 = products.get(0);
        assertEquals("Asian Stir-Fry Vegetables", product1.getName());
        assertEquals("Frozen mix of colorful stir-fry veggies.", product1.getDescription());
        assertEquals(2.99, product1.getPrice());
        assertEquals("http://dummyimage.com/165x100.png/ff4444/ffffff", product1.getImgUrl());
        assertFalse(product1.getEnabled());

        var product5 = products.get(4);
        assertEquals("Avocados", product5.getName());
        assertEquals("Fresh, creamy avocados ideal for salads and guacamole.", product5.getDescription());
        assertEquals(1.5, product5.getPrice());
        assertEquals("http://dummyimage.com/128x100.png/cc0000/ffffff", product5.getImgUrl());
        assertTrue(product5.getEnabled());
    }

    @Order(7)
    @Test
    void findProductByName() throws IOException {
    //{{baseUrl}}/products/findByName/set?page=4&size=2&direction=asc
        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("name", "set") // name: nome do parametro que vai ser passado na url, set = valor do parametro.
                .queryParams("page", 2, "size", 4, "direction","asc")
                .when()
                .get("findByName/{name}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperProductDTO wrapper= objectMapper.readValue(content, WrapperProductDTO.class);
        List<ProductDTO> products = wrapper.getEmbedded().getProducts();

        var product1 = products.get(0);
        assertEquals("Coconut Bowls Set", product1.getName());
        assertEquals("Handmade eco-friendly bowls made from real coconuts.", product1.getDescription());
        assertEquals(22.99, product1.getPrice());
        assertEquals("http://dummyimage.com/134x100.png/ff4444/ffffff", product1.getImgUrl());
        assertFalse(product1.getEnabled());

        var product4 = products.get(3);
        assertEquals("Fashionable Scarves Set", product4.getName());
        assertEquals( "Stylish scarves to accessorize any outfit.", product4.getDescription());
        assertEquals(24.99, product4.getPrice());
        assertEquals("http://dummyimage.com/225x100.png/ff4444/ffffff", product4.getImgUrl());
        assertFalse(product4.getEnabled());
    }

    private void mockProduct(Integer n) {
        product1.setName("Notebook " + n);
        product1.setDescription("Notebook de " + n + "0 polegadas");
        product1.setPrice(n * 1000.00);
        product1.setImgUrl("https://img.com/notebook" + n + ".png");
        product1.setEnabled(true);
    }

    private void setSpecification() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_MARCIOCLEY)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/products")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    private void verifyAssertEquals(Integer n) {
        assertEquals("Notebook " + n, product1.getName());
        assertEquals("Notebook de " + n + "0 polegadas", product1.getDescription());
        assertEquals(n * 1000.00, product1.getPrice());
        assertEquals("https://img.com/notebook" + n + ".png", product1.getImgUrl());
    }

    private void verifyAssertNull() {
        assertNotNull(product1.getId());
        assertNotNull(product1.getName());
        assertNotNull(product1.getDescription());
        assertNotNull(product1.getPrice());
        assertNotNull(product1.getImgUrl());
        assertTrue(product1.getId() > 0);
        assertTrue(product1.getEnabled());
    }

}