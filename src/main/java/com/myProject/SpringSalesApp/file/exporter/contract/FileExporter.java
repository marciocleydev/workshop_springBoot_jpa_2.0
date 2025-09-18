package com.myProject.SpringSalesApp.file.exporter.contract;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.List;

public interface FileExporter {

    Resource exportFile(List<ProductDTO>products) throws Exception;
}
