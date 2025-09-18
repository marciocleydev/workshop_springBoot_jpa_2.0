package com.myProject.SpringSalesApp.file.importer.contract;

import com.myProject.SpringSalesApp.DTO.ProductDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<ProductDTO> importFile(InputStream inputStream) throws Exception;
}
