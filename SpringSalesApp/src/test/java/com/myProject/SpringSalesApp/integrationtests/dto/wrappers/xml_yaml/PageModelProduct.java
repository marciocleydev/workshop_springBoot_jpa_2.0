package com.myProject.SpringSalesApp.integrationtests.dto.wrappers.xml_yaml;

import com.myProject.SpringSalesApp.integrationtests.dto.ProductDTO;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@XmlRootElement
public class PageModelProduct implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public List<ProductDTO> content;

    public PageModelProduct(){
    }

    public List<ProductDTO> getContent() {
        return content;
    }

    public void setContent(List<ProductDTO> content) {
        this.content = content;
    }
}
