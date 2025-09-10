package com.myProject.SpringSalesApp.unittests.mocks;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class MockProduct {

    public Product mockEntity(Integer number){
        Product product = new Product();
        product.setName("name test" + number);
        product.setPrice(number.doubleValue());
        product.setDescription("descrição de test numero:" + number);
        product.setImgUrl("setImgUrl" + number);
        return product;
    }
    public ProductDTO mockDTO(Integer number){
        ProductDTO product = new ProductDTO();
        if (number != null) {
            product.setId(number.longValue());
        }
        product.setName("name test" + number);
        product.setPrice(number.doubleValue());
        product.setDescription("descrição de test numero:" + number);
        product.setImgUrl("setImgUrl" + number);
        return product;
    }
    public List<Product>mockEntityList(){
        List<Product> products = new ArrayList<>();
        for (int i =0; i< 20; i++){
            products.add(mockEntity(i));
        }
        return products;
    }
    public List<ProductDTO>mockDTOList(){
        List<ProductDTO> products = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            products.add(mockDTO(i));
        }
        return products;
    }
}
