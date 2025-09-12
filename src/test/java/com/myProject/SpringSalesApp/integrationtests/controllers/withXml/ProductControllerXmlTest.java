package com.myProject.SpringSalesApp.integrationtests.controllers.withXml;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)// JUnit faz os testes aleatoriamente, essa anotação diz para ele seguir uma ordem que será definida
class ProductControllerXmlTest extends AbstractIntagrationTest {
    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static ProductDTO productDTO;


    @BeforeAll
    // vai criar apenas 1 instancia para o test todoo, o BeforeEach iria criar uma instancia para cada metodo.
    static void setUp() {

        objectMapper = new XmlMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);// nao da falha quando o Json não trás links HATEOAS.
        productDTO = new ProductDTO();
    }

    @Order(1)
    @Test
    void insert() throws IOException {
        mockProduct(1);
        setSpecification();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(productDTO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        productDTO = objectMapper.readValue(content, ProductDTO.class);
        verifyAssertNull();
        verifyAssertEquals(1);
    }

    @Order(2)
    @Test
    void findById() throws IOException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", productDTO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        productDTO = objectMapper.readValue(content, ProductDTO.class);
        verifyAssertNull();
        verifyAssertEquals(1);
    }

    @Order(3)
    @Test
    void updateById() throws IOException {
        mockProduct(2);

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", productDTO.getId())
                .body(productDTO)
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        productDTO = objectMapper.readValue(content, ProductDTO.class);
        verifyAssertNull();
        verifyAssertEquals(2);
    }

    @Order(4)
    @Test
    void disableProduct() throws IOException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", productDTO.getId())
                .when()
                .patch("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        productDTO = objectMapper.readValue(content, ProductDTO.class);
        assertFalse(productDTO.getEnabled());

        productDTO.setEnabled(true);
        verifyAssertNull();
        verifyAssertEquals(2);
    }

    @Order(5)
    @Test
    void deleteById() {
        given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", productDTO.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204);
    }

    @Order(6)
    @Test
    void findAll() throws IOException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<ProductDTO> products = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, ProductDTO.class));
        productDTO = products.get(0);
        verifyAssertNull();
        assertEquals("The Lord of the Rings", productDTO.getName());
        assertEquals("Lorem ipsum dolor sit amet, consectetur.", productDTO.getDescription());
        assertEquals(90.5, productDTO.getPrice());
        assertEquals("", productDTO.getImgUrl());

        productDTO = products.get(4);
        verifyAssertNull();
        assertEquals("Rails for Dummies", productDTO.getName());
        assertEquals("Cras fringilla convallis sem vel faucibus.", productDTO.getDescription());
        assertEquals(100.99, productDTO.getPrice());
        assertEquals("", productDTO.getImgUrl());
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