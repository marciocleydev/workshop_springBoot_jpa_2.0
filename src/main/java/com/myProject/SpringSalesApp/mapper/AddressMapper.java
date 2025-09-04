package com.myProject.SpringSalesApp.mapper;

import com.myProject.SpringSalesApp.DTO.AddressDTO;
import com.myProject.SpringSalesApp.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper{


    AddressDTO toDTO(Address address);

    Address toEntity(AddressDTO dto);

    List<Address> toEntityList(List<AddressDTO> dtos);

    List<AddressDTO> toDtoList(List<Address> addresses);



}
