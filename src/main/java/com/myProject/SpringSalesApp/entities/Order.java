package com.myProject.SpringSalesApp.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myProject.SpringSalesApp.entities.enums.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_order")
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",timezone = "GMT")
    private Instant moment;
    private Integer status;
    @ManyToOne
    @JoinColumn(name = "id_client")
    private User client;

    @OneToMany(mappedBy = "id.order")
    Set<OrderItem> items =  new HashSet<>();

    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL)
    private Payment payment;
    public Order(){
    }

    public Order(Long id, Instant moment, OrderStatus status, User client) {
        this.id = id;
        this.moment = moment;
        this.client = client;
        this.status = getIntStatus(status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }
    public OrderStatus getStringStatus(Integer id){
        return OrderStatus.getStatus(id);
    }
    public Integer getIntStatus(OrderStatus status){
        return status.getId();
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Double getTotal(){
        double sum = 0.0;
        for(OrderItem x: items){
            sum += x.getSubTotal();
        }
        return sum;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
