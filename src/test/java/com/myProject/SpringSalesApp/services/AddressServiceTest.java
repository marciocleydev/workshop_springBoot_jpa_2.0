package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.DTO.AddressDTO;
import com.myProject.SpringSalesApp.entities.Address;
import com.myProject.SpringSalesApp.mapper.AddressMapper;
import com.myProject.SpringSalesApp.mocks.MockAddress;
import com.myProject.SpringSalesApp.repositories.AddressRepository;
import com.myProject.SpringSalesApp.services.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    MockAddress input;

    @InjectMocks
    private AddressService service;
    @Mock
    AddressRepository repository;
    @Mock
    AddressMapper mapper;

    @BeforeEach
    void setUp() {
        input = new MockAddress();
    }

    @Test
    void findById() {
        Address address = input.mockEntity(1);
        AddressDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(address));
        when(mapper.toDTO(address)).thenReturn(dto);

        var result = service.findById(1L);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getId());

        boolean hasSelfLink = result.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/1")
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink,"O link 'self' não foi encontrado ou está incorreto");

        boolean hasDeleteLink = result.getLinks().stream()
                .anyMatch(link -> "delete".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/1")
                        && "DELETE".equals(link.getType()));
        assertTrue(hasDeleteLink,"O link 'delete' não foi encontrado ou está incorreto");

        boolean hasCreateLink = result.getLinks().stream()
                .anyMatch(link -> "create".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses")
                        && "POST".equals(link.getType()));
        assertTrue(hasCreateLink,"O link 'create' não foi encontrado ou está incorreto");

        boolean hasUpdateLink = result.getLinks().stream()
                .anyMatch(link -> "update".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/1")
                        && "PUT".equals(link.getType()));
        assertTrue(hasUpdateLink, "O link 'update' não foi encontrado ou está incorreto");

        boolean hasFindAllLink = result.getLinks().stream()
                .anyMatch(link -> "findAll".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses")
                        && "GET".equals(link.getType()));
        assertTrue(hasFindAllLink, "O link 'findAll' não foi encontrado ou está incorreto");


        assertEquals("street test1", result.getStreet());
        assertEquals("city test1", result.getCity());
        assertEquals("state test1", result.getState());
        assertEquals("zipcode test1", result.getZipCode());
        assertEquals("country test1", result.getCountry());
        assertEquals("complement test1", result.getComplement());
        assertEquals("100", result.getNumber());

        verify(repository).findById(1L);
        verify(mapper).toDTO(address);
        verifyNoMoreInteractions(repository, mapper);

    }

    @Test
    void insert() {
        Address entity = input.mockEntity(1);
        AddressDTO dto = input.mockDTO(1);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);
        when(repository.save(entity)).thenReturn(entity);

        var result = service.insert(dto);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getId());

        boolean hasSelfLink = result.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/1")
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink,"O link 'self' não foi encontrado ou está incorreto");

        boolean hasDeleteLink = result.getLinks().stream()
                .anyMatch(link -> "delete".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/1")
                        && "DELETE".equals(link.getType()));
        assertTrue(hasDeleteLink,"O link 'delete' não foi encontrado ou está incorreto");

        boolean hasCreateLink = result.getLinks().stream()
                .anyMatch(link -> "create".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses")
                        && "POST".equals(link.getType()));
        assertTrue(hasCreateLink,"O link 'create' não foi encontrado ou está incorreto");

        boolean hasUpdateLink = result.getLinks().stream()
                .anyMatch(link -> "update".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/1")
                        && "PUT".equals(link.getType()));
        assertTrue(hasUpdateLink, "O link 'update' não foi encontrado ou está incorreto");

        boolean hasFindAllLink = result.getLinks().stream()
                .anyMatch(link -> "findAll".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses")
                        && "GET".equals(link.getType()));
        assertTrue(hasFindAllLink, "O link 'findAll' não foi encontrado ou está incorreto");


        assertEquals("street test1", result.getStreet());
        assertEquals("city test1", result.getCity());
        assertEquals("state test1", result.getState());
        assertEquals("zipcode test1", result.getZipCode());
        assertEquals("country test1", result.getCountry());
        assertEquals("complement test1", result.getComplement());
        assertEquals("100", result.getNumber());

        verify(repository).save(entity);
        verify(mapper).toDTO(entity);
        verify(mapper).toEntity(dto);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void updateById() {
        Address entity = input.mockEntity(1);
        AddressDTO dto = input.mockDTO(1);

        when(mapper.toDTO(entity)).thenReturn(dto);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        var result = service.updateById(dto, 1L);

        assertNotNull(result);
        assertNotNull(result.getLinks());
        assertNotNull(result.getId());

        boolean hasSelfLink = result.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/1")
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink,"O link 'self' não foi encontrado ou está incorreto");

        boolean hasDeleteLink = result.getLinks().stream()
                .anyMatch(link -> "delete".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/1")
                        && "DELETE".equals(link.getType()));
        assertTrue(hasDeleteLink,"O link 'delete' não foi encontrado ou está incorreto");

        boolean hasCreateLink = result.getLinks().stream()
                .anyMatch(link -> "create".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses")
                        && "POST".equals(link.getType()));
        assertTrue(hasCreateLink,"O link 'create' não foi encontrado ou está incorreto");

        boolean hasUpdateLink = result.getLinks().stream()
                .anyMatch(link -> "update".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/1")
                        && "PUT".equals(link.getType()));
        assertTrue(hasUpdateLink, "O link 'update' não foi encontrado ou está incorreto");

        boolean hasFindAllLink = result.getLinks().stream()
                .anyMatch(link -> "findAll".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses")
                        && "GET".equals(link.getType()));
        assertTrue(hasFindAllLink, "O link 'findAll' não foi encontrado ou está incorreto");


        assertEquals("street test1", result.getStreet());
        assertEquals("city test1", result.getCity());
        assertEquals("state test1", result.getState());
        assertEquals("zipcode test1", result.getZipCode());
        assertEquals("country test1", result.getCountry());
        assertEquals("complement test1", result.getComplement());
        assertEquals("100", result.getNumber());

        verify(repository).save(entity);
        verify(mapper).toDTO(entity);
        verify(repository).findById(1L);
        verifyNoMoreInteractions(repository, mapper);
    }
    @Test
    void updateById_notFound() {
        AddressDTO dto = input.mockDTO(1);

        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.updateById(dto, 999L));
        verify(repository).findById(999L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void deleteById() {
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
    void findAll() {
        List<Address> addresses = input.mockEntityList();
        List<AddressDTO> addressDTOList = input.mockDTOList();

        when(repository.findAll()).thenReturn(addresses);
        when(mapper.toDtoList(addresses)).thenReturn(addressDTOList);

        var result = service.findAll();

        assertNotNull(result);
        assertEquals(20, result.size());

        var addressDTO1 = result.get(1);

        assertNotNull(addressDTO1);
        assertNotNull(addressDTO1.getLinks());

        Long expectedId1 = addressDTO1.getId();
        assertNotNull(expectedId1, "ID do DTO não deveria ser nulo");

        boolean hasSelfLink1 = addressDTO1.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/" + expectedId1)
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink1, "Link self ausente ou incorreto para o item da lista");

        assertEquals("street test1", addressDTO1.getStreet());
        assertEquals("city test1", addressDTO1.getCity());
        assertEquals("state test1", addressDTO1.getState());
        assertEquals("zipcode test1", addressDTO1.getZipCode());
        assertEquals("country test1", addressDTO1.getCountry());
        assertEquals("complement test1", addressDTO1.getComplement());
        assertEquals("100", addressDTO1.getNumber());

        var addressDTO19 = result.get(19);

        assertNotNull(addressDTO19);
        assertNotNull(addressDTO19.getLinks());

        Long expectedId219 = addressDTO19.getId();
        assertNotNull(expectedId219, "ID do DTO não deveria ser nulo");

        boolean hasSelfLink19 = addressDTO19.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/" + expectedId219)
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink19, "Link self ausente ou incorreto para o item da lista");

        assertEquals("street test19", addressDTO19.getStreet());
        assertEquals("city test19", addressDTO19.getCity());
        assertEquals("state test19", addressDTO19.getState());
        assertEquals("zipcode test19", addressDTO19.getZipCode());
        assertEquals("country test19", addressDTO19.getCountry());
        assertEquals("complement test19", addressDTO19.getComplement());
        assertEquals("1900", addressDTO19.getNumber());

        verify(repository).findAll();
        verify(mapper).toDtoList(addresses);
        verifyNoMoreInteractions(repository, mapper);

    }
}