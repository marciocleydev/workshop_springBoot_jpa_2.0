package com.myProject.SpringSalesApp.file.importer.impl;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.file.importer.contract.FileImporter;

import java.io.InputStream;
import java.util.List;

public class XlsxImporter implements FileImporter {
    @Override
    public List<ProductDTO> importFile(InputStream inputStream) throws Exception {
        return List.of();
    }
}
