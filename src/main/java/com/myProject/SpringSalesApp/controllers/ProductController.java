package com.myProject.SpringSalesApp.controllers;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.controllers.docs.ProductControllerDocs;
import com.myProject.SpringSalesApp.file.exporter.MediaTypes;
import com.myProject.SpringSalesApp.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<PagedModel<EntityModel<ProductDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page, //numero da pagina
            @RequestParam(value = "size", defaultValue = "12") Integer size, //quantidade de registros por pagina
            @RequestParam(value = "direction", defaultValue = "asc") String direction //quantidade de registros por pagina
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
        var products = service.findAll(pageable);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/exportPage",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaTypes.APPLICATION_XLSX_VALUE,
                    MediaTypes.APPLICATION_PDF_VALUE,
                    MediaTypes.APPLICATION_CSV_VALUE})
    @Override
    public ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page, //numero da pagina
            @RequestParam(value = "size", defaultValue = "12") Integer size, //quantidade de registros por pagina
            @RequestParam(value = "direction", defaultValue = "asc") String direction, //quantidade de registros por pagina
            HttpServletRequest request
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = service.exportPage(pageable, acceptHeader);

        Map<String, String> extensionsMap = Map.of(
                MediaTypes.APPLICATION_XLSX_VALUE,".xlsx",
                MediaTypes.APPLICATION_CSV_VALUE,".csv",
                MediaTypes.APPLICATION_PDF_VALUE,".pdf"
        );

        var fileExtension = extensionsMap.getOrDefault(acceptHeader,"");
        var contentType = acceptHeader != null ? acceptHeader : "application/octet-stream"; // tipo mais generico que existe para extensão
        var fileName = "product_exported" + fileExtension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(file);
    }

@GetMapping(value = "/findByName/{name}",
        produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
)
@Override
public ResponseEntity<PagedModel<EntityModel<ProductDTO>>> findProductByName(
        @PathVariable(value = "name") String name,
        @RequestParam(value = "page", defaultValue = "0") Integer page, //numero da pagina
        @RequestParam(value = "size", defaultValue = "12") Integer size, //quantidade de registros por pagina
        @RequestParam(value = "direction", defaultValue = "asc") String direction //ordenacao
) {
    var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "name"));
    var products = service.findProductByName(name, pageable);
    return ResponseEntity.ok().body(products);
}

@GetMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
)
@Override
public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
    ProductDTO product = service.findById(id);
    return ResponseEntity.ok().body(product);
}

@PostMapping(
        produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
)
@Override
public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO product) {
    ProductDTO product1 = service.insert(product);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(product1.getId()).toUri();
    return ResponseEntity.created(uri).body(product1);
}

@PostMapping(value = "/massCreation",
        produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
)
@Override
public ResponseEntity<List<ProductDTO>> massCreation(@RequestParam("file") MultipartFile file) {
    List<ProductDTO> product1 = service.massCreation(file);
    return ResponseEntity.ok().body(product1);
}


@PutMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE},
        consumes = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
)
@Override
public ResponseEntity<ProductDTO> updateById(@RequestBody ProductDTO product, @PathVariable Long id) {
    ProductDTO product1 = service.updateById(product, id);
    return ResponseEntity.ok().body(product1);
}

@PatchMapping(value = "/{id}",
        produces = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_XML_VALUE,
                MediaType.APPLICATION_YAML_VALUE}
)
@Override
public ResponseEntity<ProductDTO> disableProduct(@PathVariable Long id) {
    ProductDTO product1 = service.disableProduct(id);
    return ResponseEntity.ok().body(product1);
}

@DeleteMapping(value = "/{id}")
@Override
public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    service.deleteById(id);
    return ResponseEntity.noContent().build();
}
}
