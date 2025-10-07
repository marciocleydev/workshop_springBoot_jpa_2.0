package com.myProject.SpringSalesApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myProject.SpringSalesApp.entities.PK.OrderItemPk;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_order_item")
public class OrderItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private OrderItemPk id = new OrderItemPk();
    private Integer quantity;
    private Double price;
    public OrderItem(){
    }

    public OrderItem(Order order, Product product, Integer quantity, Double price) {
        setProduct(product);
        setOrder(order);
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Product getProduct(){
        return this.id.getProduct();
    }
    public void setProduct(Product product){
        this.id.setProduct(product);
    }
    @JsonIgnore
    public Order getOrder(){
        return this.id.getOder();
    }
    public void setOrder(Order order){
        this.id.setOder(order);
    }
    public Double getSubTotal(){
        return price * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
