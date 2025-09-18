package com.myProject.SpringSalesApp.services;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.controllers.ProductController;
import com.myProject.SpringSalesApp.entities.Product;
import com.myProject.SpringSalesApp.exceptions.BadRequestException;
import com.myProject.SpringSalesApp.exceptions.FileStorageException;
import com.myProject.SpringSalesApp.file.importer.contract.FileImporter;
import com.myProject.SpringSalesApp.file.importer.factory.FileImporterFactory;
import com.myProject.SpringSalesApp.mapper.ProductMapper;
import com.myProject.SpringSalesApp.repositories.ProductRepository;
import com.myProject.SpringSalesApp.exceptions.DataIntegrityException;
import com.myProject.SpringSalesApp.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {
    @Autowired
    ProductRepository repository;

    @Autowired
    FileImporterFactory importer;
    @Autowired
    ProductMapper productMapper;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    PagedResourcesAssembler<ProductDTO> assembler;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public PagedModel<EntityModel<ProductDTO>> findAll(Pageable pageable) {
        logger.info("Finding all Products!");

        var products = repository.findAll(pageable);
        return buildPagedModel(pageable, products);
    }

    public PagedModel<EntityModel<ProductDTO>> findProductByName(String name, Pageable pageable) {
        logger.info("Finding Products by name!");

        var products = repository.findProductByName(name, pageable);
        return buildPagedModel(pageable, products);
    }

    public ProductDTO findById(Long id) {
        logger.info("Finding one Product!");
        var dto = productMapper.toDTO(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id)));
        addHateoasLinks(dto);
        return dto;
    }

    public ProductDTO insert(ProductDTO product) {
        logger.info("Creating a new product!");
        var dto = productMapper.toDTO(repository.save(productMapper.toEntity(product)));
        addHateoasLinks(dto);
        return dto;
    }

    public ProductDTO updateById(ProductDTO newProductDTO, Long id) {
        logger.info("Updating one product!");
        try {
            Product oldProduct = repository.getReferenceById(id);
            updateGeneration(oldProduct, newProductDTO);
            var dto = productMapper.toDTO(repository.save(oldProduct));
            addHateoasLinks(dto);
            return dto;
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateGeneration(Product oldProduct, ProductDTO newProduct) {
        oldProduct.setName(newProduct.getName());
        oldProduct.setDescription(newProduct.getDescription());
        oldProduct.setImgUrl(newProduct.getImgUrl());
        oldProduct.setPrice(newProduct.getPrice());
    }

    public void deleteById(Long id) {
        logger.info("Deleting one product!");
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException(e.getMessage());
        }
    }

    @Transactional
    // como eu mesmo que criei essa operaçao no repositorio preciso informar @Transactional, pois essa operação precisa obedecer o ACID
    public ProductDTO disableProduct(Long id) {
        logger.info("Disabling one product!");
        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        repository.disableProduct(id);
        var product = repository.findById(id).get();
        var productDTO = productMapper.toDTO(product);
        addHateoasLinks(productDTO);
        return productDTO;
    }

    public List<ProductDTO> massCreation(MultipartFile file)  {
        logger.info("Importing products from file");
        if (file.isEmpty()) {
            throw new BadRequestException("Please set a valid file");
        }
        try(InputStream inputStream = file.getInputStream()) {
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name is required"));
            FileImporter importer = this.importer.getImporter(fileName);

            List<Product> products = importer.importFile(inputStream).stream()
                    .map(dto -> repository.save(productMapper.toEntity(dto)))
                    .toList();

            return products.stream()
                    .map(productMapper::toDTO)
                    .peek(this::addHateoasLinks)
                    .toList();
        } catch (IOException e) {
            throw new FileStorageException("Error reading file");
        }catch (Exception e){
            throw new BadRequestException("Error importing file");
        }
    }

    private PagedModel<EntityModel<ProductDTO>> buildPagedModel(Pageable pageable, Page<Product> products) {
        var productsWithLinks = products.map(
                product -> {
                    var dto = productMapper.toDTO(product);
                    addHateoasLinks(dto);
                    return dto;
                });
        //adiciona link hateoas a página
        Link findAllLInk = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(ProductController.class)
                                .findAll(
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        pageable.getSort().toString()))
                .withSelfRel();
        return assembler.toModel(productsWithLinks, findAllLInk);
    }

    private void addHateoasLinks(ProductDTO dto) {
        dto.add(linkTo(methodOn(ProductController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(ProductController.class).findProductByName(dto.getName(),1, 12, "asc")).withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(ProductController.class).deleteById(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(ProductController.class)).slash("massCreation").withRel("massCreation").withType("POST"));
        dto.add(linkTo(methodOn(ProductController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(ProductController.class).insert(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(ProductController.class).updateById(dto, dto.getId())).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(ProductController.class).disableProduct(dto.getId())).withRel("disabe").withType("PATCH"));
    }
}
