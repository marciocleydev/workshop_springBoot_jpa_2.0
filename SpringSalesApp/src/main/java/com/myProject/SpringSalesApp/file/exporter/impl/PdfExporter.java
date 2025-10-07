package com.myProject.SpringSalesApp.file.exporter.impl;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.file.exporter.contract.FileExporter;
import com.myProject.SpringSalesApp.services.QRCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Component
public class PdfExporter implements FileExporter {
    @Autowired
    QRCodeService service;

    @Override
    public Resource exportFile(List<ProductDTO> products) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/templates/products.jrxml");
        if (inputStream == null) {
            throw new RuntimeException("Template file not found: /templates/products.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(products);

        //se fosse passar parametros:
        Map<String, Object> parameters = new HashMap<>();
        //exemplo de parametro: parameters.put("title", "My Title");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }


    @Override
    public Resource exportProduct(ProductDTO product) throws Exception {
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/product.jrxml");
        if(mainTemplateStream == null) {
            throw new RuntimeException("Template file not found: /templates/product.jrxml");
        }

        InputStream subReportStream = getClass().getResourceAsStream("/templates/categories.jrxml");
        if(subReportStream == null) {
            throw new RuntimeException("Template file not found: /templates/categories.jrxml");
        }

        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        InputStream qrCodeStream = service.generateQRCode("https://www.google.com", 200, 200); // 200 , 200 significa pixels largura e altura

        JRBeanCollectionDataSource subDataSource = new JRBeanCollectionDataSource(product.getCategories()); // Collections.singletonList -> transforma em apenas um objeto dentro de uma lista

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CATEGORIES_SUB_REPORT", subReport);
        parameters.put("SUB_REPORT_DATA_SOURCE", subDataSource);
        parameters.put("QR_CODEIMAGE", qrCodeStream);

        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(product));

        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, mainDataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }


}
