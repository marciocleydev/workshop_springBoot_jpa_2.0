package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.controllers.docs.ProductControllerDocs;
import com.myProject.SpringSalesApp.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
@Tag(name = "Product Resource", description = "Endpoints for product management")
public class ProductController implements ProductControllerDocs {

    @Autowired
    private ProductService service;

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
            )
    @Override
    public  ResponseEntity<PagedModel<EntityModel<ProductDTO>>> findAll(
            @RequestParam(value = "page",defaultValue = "0")Integer page, //numero da pagina
            @RequestParam(value = "size",defaultValue = "12")Integer size, //quantidade de registros por pagina
            @RequestParam(value = "direction",defaultValue = "asc")String direction //quantidade de registros por pagina
    ){
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC: Direction.ASC;
        Pageable pageable = PageRequest.of(page,size,Sort.by(sortDirection,"name"));
        var products =  service.findAll(pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
        ProductDTO product = service.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE},
            consumes ={MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO product){
        ProductDTO product1 = service.insert(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product1.getId()).toUri();
        return ResponseEntity.created(uri).body(product1);
    }

    @PutMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE},
            consumes ={MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<ProductDTO> updateById(@RequestBody ProductDTO product, @PathVariable Long id){
        ProductDTO product1 = service.updateById(product,id);
        return ResponseEntity.ok().body(product1);
    }
    @PatchMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<ProductDTO> disableProduct(@PathVariable Long id){
        ProductDTO product1 = service.disableProduct(id);
        return ResponseEntity.ok().body(product1);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
