package com.myProject.SpringSalesApp.DTO;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;

public class AddressDTO extends RepresentationModel<AddressDTO>implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private Long id;
    private String street;
    private String number;
    private String complement;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Long idClient;
    private String nameClient;

    public AddressDTO() {
    }

    public AddressDTO(Long id, String city, String complement, String country, String number, String state, String street, String zipCode) {
        this.id = id;
        this.city = city;
        this.complement = complement;
        this.country = country;
        this.number = number;
        this.state = state;
        this.street = street;
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public Long getIdClient() {
        return idClient;
    }
    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }
    public String getNameClient() {
        return nameClient;
    }
    public void setNameClient(String nameClient) {
        this.nameClient = nameClient;
    }

}
