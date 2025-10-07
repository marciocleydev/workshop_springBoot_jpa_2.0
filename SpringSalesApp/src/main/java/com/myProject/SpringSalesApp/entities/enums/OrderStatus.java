package com.myProject.SpringSalesApp.entities.enums;

public enum OrderStatus {
    WAITING_PAYMENT(1),
    PAID(2),
    SHIPPED(3),
    DELIVERED(4),
    CANCELED(5);

    private Integer id;
    private OrderStatus(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return this.id;
    }
    public static OrderStatus getStatus(Integer id){
        for (OrderStatus status: OrderStatus.values()){
            if (id == status.getId()){
                return status;
            }
        }
        throw new IllegalArgumentException("Id: " + id + " doesn't exist!");
    }
}
