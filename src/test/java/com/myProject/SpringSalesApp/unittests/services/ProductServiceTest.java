package com.myProject.SpringSalesApp.unittests.services;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.entities.Product;
import com.myProject.SpringSalesApp.mapper.ProductMapper;
import com.myProject.SpringSalesApp.unittests.mocks.MockProduct;
import com.myProject.SpringSalesApp.repositories.ProductRepository;
import com.myProject.SpringSalesApp.services.ProductService;
import com.myProject.SpringSalesApp.services.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    MockProduct input;
    @InjectMocks
    private ProductService service;
    @Mock
    ProductRepository repository;
    @Mock
    ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        input = new MockProduct();
    }

    @Test
    void findById() {
        Product product = input.mockEntity(1);
        product.setId(1L);

        ProductDTO productDTO = input.mockDTO(1);
        productDTO.setId(product.getId());

        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(productDTO);

        var result = service.findById(1L);

        assertNotNull(result);

        assertNotNull(result.getLinks());
        assertNotNull(result.getId());
        boolean hasSelfLink = result.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/products/1")
                        && "GET".equals(link.getType()));

        assertTrue(hasSelfLink, "O link 'self' não foi encontrado ou está incorreto");

        boolean hasFindAllLink = result.getLinks().stream()
                .anyMatch(link -> "findAll".equals(link.getRel().value())
                        && link.getHref().endsWith("/products")
                        && "GET".equals(link.getType()));

        assertTrue(hasFindAllLink, "O link 'findAll' não foi encontrado ou está incorreto");

        boolean hasCreateLink = result.getLinks().stream()
                .anyMatch(link -> "create".equals(link.getRel().value())
                        && link.getHref().endsWith("/products")
                        && "POST".equals(link.getType()));

        assertTrue(hasCreateLink, "O link 'create' não foi encontrado ou está incorreto");

        boolean hasUpdateLink = result.getLinks().stream()
                .anyMatch(link -> "update".equals(link.getRel().value())
                        && link.getHref().endsWith("/products/1")
                        && "PUT".equals(link.getType()));

        assertTrue(hasUpdateLink, "O link 'update' não foi encontrado ou está incorreto");

        boolean hasDeleteLink = result.getLinks().stream()
                .anyMatch(link -> "delete".equals(link.getRel().value())
                        && link.getHref().endsWith("/products/1")
                        && "DELETE".equals(link.getType()));

        assertTrue(hasDeleteLink, "O link 'delete' não foi encontrado ou está incorreto");

        assertEquals("name test1", result.getName());
        assertEquals("setImgUrl1", result.getImgUrl());
        assertEquals(1.0, result.getPrice());
        assertEquals("descrição de test numero:1", result.getDescription());

        verify(repository).findById(1L);
        verify(productMapper).toDTO(product);
        verifyNoMoreInteractions(repository, productMapper);
    }

    @Test
    void insert() {
        Product entity = input.mockEntity(1);
        entity.setId(1L);


        ProductDTO dto = input.mockDTO(1);
        dto.setId(1L);

        when(productMapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(productMapper.toDTO(entity)).thenReturn(dto);

        var result = service.insert(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        boolean hasSelfLink = result.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/products/1")
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink);

        boolean hasCreateLink = result.getLinks().stream()
                .anyMatch(link -> "create".equals(link.getRel().value())
                        && link.getHref().endsWith("/products")
                        && "POST".equals(link.getType()));
        assertTrue(hasCreateLink);

        assertEquals("name test1", result.getName());
        assertEquals("setImgUrl1", result.getImgUrl());
        assertEquals(1.0, result.getPrice());
        assertEquals("descrição de test numero:1", result.getDescription());

        verify(productMapper).toEntity(dto);
        verify(repository).save(entity);
        verify(productMapper).toDTO(entity);
        verifyNoMoreInteractions(repository, productMapper);
    }

    @Test
    void insertWithoutId() {
        Product entity = input.mockEntity(1);
        entity.setId(1L);

        ProductDTO inputDto = input.mockDTO(1);
        inputDto.setId(null);

        ProductDTO returnedDto = input.mockDTO(1);
        returnedDto.setId(1L);

        when(productMapper.toEntity(inputDto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(productMapper.toDTO(entity)).thenReturn(returnedDto);

        var result = service.insert(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getLinks());

        boolean hasSelfLink = result.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/products/1")
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink);

        verify(productMapper).toEntity(inputDto);
        verify(repository).save(entity);
        verify(productMapper).toDTO(entity);
        verifyNoMoreInteractions(repository, productMapper);
    }

    @Test
    void updateById() {
        Product entity = input.mockEntity(1);
        entity.setId(1L);

        entity.setId(1L);

        ProductDTO dto = input.mockDTO(1);
        dto.setId(1L);

        when(repository.getReferenceById(1L)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(productMapper.toDTO(entity)).thenReturn(dto);

        var result = service.updateById(dto, 1L);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        boolean hasSelfLink = result.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/products/1")
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink);

        assertEquals("name test1", result.getName());
        assertEquals("setImgUrl1", result.getImgUrl());
        assertEquals(1.0, result.getPrice());
        assertEquals("descrição de test numero:1", result.getDescription());

        verify(repository).getReferenceById(1L);
        verify(repository).save(entity);
        verify(productMapper).toDTO(entity);
        verifyNoMoreInteractions(repository, productMapper);
    }

    @Test
    void updateById_notFound() {
        ProductDTO dto = input.mockDTO(1);
        dto.setId(999L);

        when(repository.getReferenceById(999L)).thenThrow(new jakarta.persistence.EntityNotFoundException());

        assertThrows(ResourceNotFoundException.class, () -> service.updateById(dto, 999L));

        verify(repository).getReferenceById(999L);
        verifyNoMoreInteractions(repository, productMapper);
    }

    @Test
    void deleteById_success() {
        service.deleteById(1L);
        verify(repository).deleteById(1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById_notFound() {
        doThrow(new EmptyResultDataAccessException(1)).when(repository).deleteById(999L);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(999L));
        verify(repository).deleteById(999L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById_dataIntegrity() {
        doThrow(new DataIntegrityViolationException("FK violation")).when(repository).deleteById(2L);
        assertThrows(DataIntegrityException.class, () -> service.deleteById(2L));
        verify(repository).deleteById(2L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @Disabled("Reason: Still on development")
    void findAll() {
        List<Product> list = input.mockEntityList();
        List<ProductDTO> listDTO = input.mockDTOList();

        when(repository.findAll()).thenReturn(list);
        when(productMapper.toDtoList(list)).thenReturn(listDTO);

        var result = new ArrayList<>();// service.findAll(pageable);

        assertNotNull(result);
        assertEquals(20, result.size());

        var productDTO1 = result.get(1);

        assertNotNull(productDTO1);
        assertNotNull(productDTO1.getLinks());

        Long expectedId1 = productDTO1.getId();
        assertNotNull(expectedId1, "ID do DTO não deveria ser nulo");

        boolean hasSelfLink1 = productDTO1.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/products/" + expectedId1)
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink1, "Link self ausente ou incorreto para o item da lista");

        assertEquals("name test1", productDTO1.getName());
        assertEquals("setImgUrl1", productDTO1.getImgUrl());
        assertEquals(1.0, productDTO1.getPrice());
        assertEquals("descrição de test numero:1", productDTO1.getDescription());

        var productDTO5 = result.get(5);

        assertNotNull(productDTO5);
        assertNotNull(productDTO5.getLinks());

        Long expectedId5 = productDTO5.getId();
        assertNotNull(expectedId5, "ID do DTO não deveria ser nulo");

        boolean hasSelfLink5 = productDTO5.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/products/" + expectedId5)
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink5, "Link self ausente ou incorreto para o item da lista");

        assertEquals("name test5", productDTO5.getName());
        assertEquals("setImgUrl5", productDTO5.getImgUrl());
        assertEquals(5.0, productDTO5.getPrice());
        assertEquals("descrição de test numero:5", productDTO5.getDescription());

        var productDTO15 = result.get(15);

        assertNotNull(productDTO15);
        assertNotNull(productDTO15.getLinks());

        Long expectedId15 = productDTO15.getId();
        assertNotNull(expectedId15, "ID do DTO não deveria ser nulo");

        boolean hasSelfLink15 = productDTO15.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/products/" + expectedId15)
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink15, "Link self ausente ou incorreto para o item da lista");

        assertEquals("name test15", productDTO15.getName());
        assertEquals("setImgUrl15", productDTO15.getImgUrl());
        assertEquals(15.0, productDTO15.getPrice());
        assertEquals("descrição de test numero:15", productDTO15.getDescription());

        verify(repository).findAll();
        verify(productMapper).toDtoList(list);
        verifyNoMoreInteractions(repository, productMapper);
    }
}