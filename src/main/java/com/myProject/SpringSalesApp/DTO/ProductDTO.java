package com.myProject.SpringSalesApp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serial;
import java.io.Serializable;

@Relation(collectionRelation = "products")
@JsonPropertyOrder({"id","name","price","description","imgUrl","enabled"})
public class ProductDTO extends RepresentationModel<ProductDTO> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
        private Long id;
        @JsonProperty("name")
        private String name;
    @JsonProperty("description")
        private String description;
      @JsonProperty("price")
        private Double price;
    @JsonProperty("imgUrl")
        private String imgUrl;
    @JsonProperty("enabled")
        private boolean enabled;
    /*
    @JsonFormat(pattern = "dd/MM/yyyy")
        private LocalDate factDate;
     */

        public ProductDTO(){
        }

        public ProductDTO(Long id, String name, String description, Double price, String imgUrl, boolean enabled) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.imgUrl = imgUrl;
            this.enabled = enabled;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
        public boolean getEnabled() {
            return enabled;
        }
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
}
