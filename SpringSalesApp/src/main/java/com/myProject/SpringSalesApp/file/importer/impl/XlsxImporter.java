package com.myProject.SpringSalesApp.file.importer.impl;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Component
public class XlsxImporter implements FileImporter {
    @Override
    public List<ProductDTO> importFile(InputStream inputStream) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0); // como o excel pode ter varias abas aqui vc seleciona , nesse caso vamos pegar a 1 aba
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            return parseRowsToProductDTOList(rowIterator);
        }
    }

    private List<ProductDTO> parseRowsToProductDTOList(Iterator<Row> rowIterator) {
        List<ProductDTO> products = new java.util.ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowValid(row)) {
               products.add(parseRowToProductDTO(row));
            }
        }
        return products;
    }

    private ProductDTO parseRowToProductDTO(Row row) {
        ProductDTO product = new ProductDTO();
        product.setName(row.getCell(0).getStringCellValue());
        product.setDescription(row.getCell(1).getStringCellValue());
        //verifica se o tipo é strig ou numeric para nao dar erro e gerar excecao
        if (row.getCell(2).getCellType() == CellType.NUMERIC) {
            product.setPrice(row.getCell(2).getNumericCellValue());
        } else {
            product.setPrice(Double.parseDouble(row.getCell(2).getStringCellValue()));
        }
        product.setImgUrl(row.getCell(3).getStringCellValue());
        product.setEnabled(true);
        return product;
    }

    private static boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}
