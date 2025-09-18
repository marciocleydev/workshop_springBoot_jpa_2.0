package com.myProject.SpringSalesApp.file.importer.impl;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
@Component
public class CsvImporter implements FileImporter {
    @Override
    public List<ProductDTO> importFile(InputStream inputStream) throws Exception {
        CSVFormat format = CSVFormat.Builder.create()
                .setHeader()
                .setSkipHeaderRecord(true) //pule o primeiro registro na primeira linha
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();
        Iterable<CSVRecord> records = format.parse(new InputStreamReader(inputStream));
        return parseRecordsToProductDTOs(records);
    }

    private List<ProductDTO> parseRecordsToProductDTOs(Iterable<CSVRecord> records) {
        List<ProductDTO> products = new ArrayList<>();

        for (CSVRecord record : records) {
            ProductDTO product = new ProductDTO();
            product.setName(record.get("name"));
            product.setDescription(record.get("description"));
            product.setPrice(Double.parseDouble(record.get("price")));
            product.setImgUrl(record.get("imgUrl"));
            product.setEnabled(true);
            products.add(product);
        }
        return products;
    }
}
