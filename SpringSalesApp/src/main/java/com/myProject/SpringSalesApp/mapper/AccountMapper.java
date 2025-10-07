package com.myProject.SpringSalesApp.mapper;

import com.myProject.SpringSalesApp.DTO.security.AccountCredentialsDTO;
import com.myProject.SpringSalesApp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mappings({
            @Mapping(source = "username", target = "username"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "fullName", target = "fullName"),
    })
    AccountCredentialsDTO toDTO(User user);

}
