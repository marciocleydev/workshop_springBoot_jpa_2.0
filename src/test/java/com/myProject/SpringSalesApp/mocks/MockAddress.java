package com.myProject.SpringSalesApp.mocks;

import com.myProject.SpringSalesApp.DTO.AddressDTO;
import com.myProject.SpringSalesApp.entities.Address;

import java.util.ArrayList;
import java.util.List;

public class MockAddress {

    public Address mockEntity(Integer number){
        Address  address = new Address();
        address.setId(number.longValue());
        address.setCountry("country test" + number);
        address.setCity("city test" + number);
        address.setComplement("complement test" + number);
        address.setNumber(number.toString() + "00");
        address.setState("state test" + number);
        address.setStreet("street test" + number);
        address.setZipCode("zipcode test" + number);
        return address;
    }
    public AddressDTO mockDTO(Integer number){
        AddressDTO address = new AddressDTO();
        address.setId(number.longValue());
        address.setCountry("country test" + number);
        address.setCity("city test" + number);
        address.setComplement("complement test" + number);
        address.setNumber(number.toString() + "00");
        address.setState("state test" + number);
        address.setStreet("street test" + number);
        address.setZipCode("zipcode test" + number);
        return address;
    }
    public List<Address> mockEntityList(){
        List<Address> addresses = new ArrayList<>();
        for (int i =0; i< 20; i++){
            addresses.add(mockEntity(i));
        }
        return addresses;
    }
    public List<AddressDTO>mockDTOList(){
        List<AddressDTO> addresses = new ArrayList<>();
        for (int i =0; i< 20; i++){
            addresses.add(mockDTO(i));
        }
        return addresses;
    }
}
