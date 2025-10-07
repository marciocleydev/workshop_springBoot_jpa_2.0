package com.myProject.SpringSalesApp.file.importer.factory;

import com.myProject.SpringSalesApp.exceptions.BadRequestException;
import com.myProject.SpringSalesApp.file.importer.contract.FileImporter;
import com.myProject.SpringSalesApp.file.importer.impl.CsvImporter;
import com.myProject.SpringSalesApp.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {
    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context; // serve para injetar o contexto do spring e nao precisar usar new
    public FileImporter getImporter(String fileName)throws Exception{
        if (fileName.endsWith(".xlsx")){
            return context.getBean(XlsxImporter.class);
        }else if (fileName.endsWith(".csv")){
            return context.getBean(CsvImporter.class);
        }else {
            throw new BadRequestException("Invalid file format");
        }
    }
}
