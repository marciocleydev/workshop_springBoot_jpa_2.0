package com.myProject.SpringSalesApp.file.exporter.impl;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.file.exporter.contract.FileExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
@Component
public class XlsxExporter implements FileExporter {
    @Override
    public Resource exportFile(List<ProductDTO> products) throws Exception {

        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Products");

            Row headerRow = sheet.createRow(0);
            String[] header = {"ID","Name", "Description", "Price", "ImgUrl", "Enabled" };
            for (int i = 0; i < header.length; i++) {
                // Create a cell and put a value in it.
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(header[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }
            int rowIndex = 1;
            for (ProductDTO product : products) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getDescription());
                row.createCell(3).setCellValue(product.getPrice());
                row.createCell(4).setCellValue(product.getImgUrl());
                row.createCell(5).setCellValue(product.getEnabled() ? "Yes": "No");
            }
            for (int i = 0; i < header.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }

    @Override
    public Resource exportProduct(ProductDTO product) throws Exception {
        return null;
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
