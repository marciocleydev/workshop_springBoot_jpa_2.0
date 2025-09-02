package com.myProject.SpringSalesApp.mapper;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    ProductDTO toDTO(Product product);
    List<ProductDTO> toDtoList(List<Product> products);

    Product toEntity(ProductDTO dto);

    /* EXEMPLOS: para mapear 1 unico atributo.
    @Mapping(source = "endereco.id", target = "enderecoId")
    @Mapping(source = "endereco.cidade", target = "cidadeEndereco")
    ClienteDTO toDto(Cliente cliente);
     */
}
