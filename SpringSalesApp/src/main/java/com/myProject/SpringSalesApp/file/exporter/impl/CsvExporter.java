package com.myProject.SpringSalesApp.file.exporter.impl;

import com.myProject.SpringSalesApp.DTO.ProductDTO;
import com.myProject.SpringSalesApp.file.exporter.contract.FileExporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Component
public class CsvExporter implements FileExporter {
    @Override
    public Resource exportFile(List<ProductDTO> products) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader("ID","Name", "Description", "Price", "ImgUrl", "Enabled" ) // todas as colunas
                .setSkipHeaderRecord(false) // nao pode pular o header pq ele precisa ser definido
                .build();

        try(CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
            for (ProductDTO product : products) {
                // precisa ser na mesma ordem que foi definida no header
                csvPrinter.printRecord(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImgUrl(),
                        product.getEnabled());
            }
        }

        return new ByteArrayResource(outputStream.toByteArray());
    }

    @Override
    public Resource exportProduct(ProductDTO product) throws Exception {
        return null;
    }
}
