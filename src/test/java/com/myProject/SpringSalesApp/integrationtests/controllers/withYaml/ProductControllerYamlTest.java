package com.myProject.SpringSalesApp.integrationtests.controllers.withYaml;

import com.myProject.SpringSalesApp.config.TestConfigs;
import com.myProject.SpringSalesApp.integrationtests.controllers.withYaml.mapper.YAMLMapper;
import com.myProject.SpringSalesApp.integrationtests.dto.AccountCredentialsDTO;
import com.myProject.SpringSalesApp.integrationtests.dto.ProductDTO;
import com.myProject.SpringSalesApp.integrationtests.dto.TokenDTO;
import com.myProject.SpringSalesApp.integrationtests.dto.wrappers.json.WrapperProductDTO;
import com.myProject.SpringSalesApp.integrationtests.dto.wrappers.xml_yaml.PageModelProduct;
import com.myProject.SpringSalesApp.integrationtests.testcontainers.AbstractIntagrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)// JUnit faz os testes aleatoriamente, essa anotação diz para ele seguir uma ordem que será definida
class ProductControllerYamlTest extends AbstractIntagrationTest {
    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;
    private static ProductDTO productDTO;
    private static TokenDTO tokenDTO;


    @BeforeAll
    static void setUp() {
        objectMapper = new YAMLMapper();
        productDTO = new ProductDTO();
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

        var content = given().config(
                RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                        )
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(productDTO, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(ProductDTO.class, objectMapper);

        productDTO = content;
        verifyAssertNull();
        verifyAssertEquals(1);
    }

    @Order(2)
    @Test
    void findById() throws IOException {
        var content =  given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", productDTO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(ProductDTO.class, objectMapper);

        productDTO = content;
        verifyAssertNull();
        verifyAssertEquals(1);
    }

    @Order(3)
    @Test
    void updateById() throws IOException {
        mockProduct(2);

        var content =  given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", productDTO.getId())
                .body(productDTO, objectMapper)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(ProductDTO.class, objectMapper);

        productDTO =  content;
        verifyAssertNull();
        verifyAssertEquals(2);
    }

    @Order(4)
    @Test
    void disableProduct() throws IOException {
        var content = given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", productDTO.getId())
                .when()
                .patch("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(ProductDTO.class, objectMapper);

        productDTO =  content;
        assertFalse(productDTO.getEnabled());

        productDTO.setEnabled(true);
        verifyAssertNull();
        verifyAssertEquals(2);
    }

    @Order(5)
    @Test
    void deleteById() {
        given(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", productDTO.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204);
    }

    @Order(6)
    @Test
    void findAll() throws IOException {
        var response =  given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .queryParams("page", 3, "size", 12, "direction","asc")
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PageModelProduct.class, objectMapper);

        List<ProductDTO> products =response.content;
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
        var response =  given().config(
                        RestAssuredConfig.config()
                                .encoderConfig(
                                        EncoderConfig.encoderConfig()
                                                .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT)
                                )
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("name", "set") // name: nome do parametro que vai ser passado na url, set = valor do parametro.
                .queryParams("page", 2, "size", 4, "direction","asc")
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .when()
                .get("findByName/{name}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PageModelProduct.class, objectMapper);

        List<ProductDTO> products =response.content;
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
        productDTO.setName("Notebook " + n);
        productDTO.setDescription("Notebook de " + n + "0 polegadas");
        productDTO.setPrice(n * 1000.00);
        productDTO.setImgUrl("https://img.com/notebook" + n + ".png");
        productDTO.setEnabled(true);
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
        assertEquals("Notebook " + n, productDTO.getName());
        assertEquals("Notebook de " + n + "0 polegadas", productDTO.getDescription());
        assertEquals(n * 1000.00, productDTO.getPrice());
        assertEquals("https://img.com/notebook" + n + ".png", productDTO.getImgUrl());
    }

    private void verifyAssertNull() {
        assertNotNull(productDTO.getId());
        assertNotNull(productDTO.getName());
        assertNotNull(productDTO.getDescription());
        assertNotNull(productDTO.getPrice());
        assertNotNull(productDTO.getImgUrl());
        assertTrue(productDTO.getId() > 0);
        assertTrue(productDTO.getEnabled());
    }

}