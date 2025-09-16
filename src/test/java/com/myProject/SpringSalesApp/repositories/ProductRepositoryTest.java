package com.myProject.SpringSalesApp.repositories;

import com.myProject.SpringSalesApp.entities.Product;
import com.myProject.SpringSalesApp.integrationtests.testcontainers.AbstractIntagrationTest;
import org.hibernate.query.SortDirection;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@DataJpaTest()//carrega apenas componentes relacionados a camada de persistencia, repository, entidade e contexto do banco de dados, por padrao usa banco h2 para os testes
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)// por padrao o dataJpaTest substitui a configuraçao do banco de dados pelo o h2, essa anotation desativa obrigando a usar um banco de dados externo como postgreSQL/mysql
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)// JUnit faz os testes aleatoriamente, essa anotação diz para ele seguir uma ordem que será definida
class ProductRepositoryTest extends AbstractIntagrationTest {

    @Autowired
    ProductRepository repository;
    private static Product product;

    @BeforeAll
    static void setUp() {
        product = new Product();
    }

    @Test
    @Order(1)
    void findProductByName() {
        Pageable pageable = PageRequest.of(
                0,
                12,
                Sort.by(Sort.Direction.ASC,"name"));

        product = repository.findProductByName("smart", pageable).getContent().get(0);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals("Multi-Function Smartphone Holder", product.getName());
        assertEquals("Versatile holder that can be used on desks, cars, and more.", product.getDescription());
        assertEquals(12.99, product.getPrice());
        assertEquals("http://dummyimage.com/234x100.png/dddddd/000000", product.getImgUrl());
        assertTrue(product.getEnabled());
    }

    @Test
    @Order(2)
    void disableProduct() {
        Long id = product.getId();
        repository.disableProduct(id);

        var result = repository.findById(id);
        product = result.get();

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals("Multi-Function Smartphone Holder", product.getName());
        assertEquals("Versatile holder that can be used on desks, cars, and more.", product.getDescription());
        assertEquals(12.99, product.getPrice());
        assertEquals("http://dummyimage.com/234x100.png/dddddd/000000", product.getImgUrl());
        assertFalse(product.getEnabled());
    }
}