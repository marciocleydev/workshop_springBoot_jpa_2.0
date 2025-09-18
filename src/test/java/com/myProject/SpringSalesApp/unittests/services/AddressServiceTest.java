package com.myProject.SpringSalesApp.unittests.services;

import com.myProject.SpringSalesApp.DTO.AddressDTO;
import com.myProject.SpringSalesApp.entities.Address;
import com.myProject.SpringSalesApp.mapper.AddressMapper;
import com.myProject.SpringSalesApp.unittests.mocks.MockAddress;
import com.myProject.SpringSalesApp.repositories.AddressRepository;
import com.myProject.SpringSalesApp.services.AddressService;
import com.myProject.SpringSalesApp.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.exceptions.ResourceNotFoundException;
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
        assertHasHateoasLinks(result);
        assertEqualsData(result);

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
        assertHasHateoasLinks(result);
        assertEqualsData(result);

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
        assertHasHateoasLinks(result);
        assertEqualsData(result);

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

        var address1 = result.get(1);

        assertNotNull(address1);
        assertNotNull(address1.getLinks());
        assertHasHateoasLinks(address1);
        assertEqualsData(address1);

        var address10 = result.get(10);

        assertNotNull(address10);
        assertNotNull(address10.getLinks());
        assertHasHateoasLinks(address10);
        assertEqualsData(address10);

        var address19 = result.get(19);

        assertNotNull(address19);
        assertNotNull(address19.getLinks());
       assertHasHateoasLinks(address19);
       assertEqualsData(address19);

        verify(repository).findAll();
        verify(mapper).toDtoList(addresses);
        verifyNoMoreInteractions(repository, mapper);

    }

    private void assertHasHateoasLinks(AddressDTO result) {
        Long expectedId = result.getId();
        boolean hasSelfLink = result.getLinks().stream()
                .anyMatch(link -> "self".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/" + expectedId)
                        && "GET".equals(link.getType()));
        assertTrue(hasSelfLink,"O link 'self' não foi encontrado ou está incorreto");

        boolean hasDeleteLink = result.getLinks().stream()
                .anyMatch(link -> "delete".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/"+ expectedId)
                        && "DELETE".equals(link.getType()));
        assertTrue(hasDeleteLink,"O link 'delete' não foi encontrado ou está incorreto");

        boolean hasCreateLink = result.getLinks().stream()
                .anyMatch(link -> "create".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses")
                        && "POST".equals(link.getType()));
        assertTrue(hasCreateLink,"O link 'create' não foi encontrado ou está incorreto");

        boolean hasUpdateLink = result.getLinks().stream()
                .anyMatch(link -> "update".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses/" + expectedId)
                        && "PUT".equals(link.getType()));
        assertTrue(hasUpdateLink, "O link 'update' não foi encontrado ou está incorreto");

        boolean hasFindAllLink = result.getLinks().stream()
                .anyMatch(link -> "findAll".equals(link.getRel().value())
                        && link.getHref().endsWith("/addresses")
                        && "GET".equals(link.getType()));
        assertTrue(hasFindAllLink, "O link 'findAll' não foi encontrado ou está incorreto");
    }
    private void assertEqualsData(AddressDTO dto){
        Long expectedValue = dto.getId();
        assertEquals("street test" + expectedValue, dto.getStreet());
        assertEquals("city test" + expectedValue, dto.getCity());
        assertEquals("state test" + expectedValue, dto.getState());
        assertEquals("zipcode test" + expectedValue, dto.getZipCode());
        assertEquals("country test" + expectedValue, dto.getCountry());
        assertEquals("complement test" + expectedValue, dto.getComplement());
        assertEquals(expectedValue + "00", dto.getNumber());
    }
}