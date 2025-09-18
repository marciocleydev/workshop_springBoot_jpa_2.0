package com.myProject.SpringSalesApp.file.exporter.impl;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.file.exporter.contract.FileExporter;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class XlsxExporter implements FileExporter {
    @Override
    public Resource exportFile(List<ProductDTO> products) throws Exception {
        return null;
    }
}
