package com.myProject.SpringSalesApp.integrationtests.dto.wrappers.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;

public class WrapperProductDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("_embedded")
    private ProductEmbeddedDTO embedded;
    public WrapperProductDTO(){
    }

    public ProductEmbeddedDTO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(ProductEmbeddedDTO embedded) {
        this.embedded = embedded;
    }
}
