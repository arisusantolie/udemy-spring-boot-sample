package com.appdeveloperblog.app.ws.mobileappws.controllers;

import com.appdeveloperblog.app.ws.mobileappws.dto.AddressDto;
import com.appdeveloperblog.app.ws.mobileappws.models.response.AddressesRest;
import com.appdeveloperblog.app.ws.mobileappws.services.AddressService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class AddressController {

    @Autowired
    AddressService addressService;

    @GetMapping("{id}/addresses")
    public List<AddressesRest> getUserAddresses(@PathVariable("id") String userId){

        List<AddressesRest> returnValue=new ArrayList<>();
        List<AddressDto> userAddressDto=addressService.getListAddressUser(userId);

        if(userAddressDto !=null && !userAddressDto.isEmpty()){
            //java.lang.reflect.Type //ini make library modelmapper
            Type listType=new TypeToken<List<AddressesRest>>() {}.getType();
            ModelMapper modelMapper=new ModelMapper();
            returnValue=modelMapper.map(userAddressDto,listType);
        }


        return returnValue;
    }

    @GetMapping("{id}/addresses/{addressid}")
    public AddressesRest getUserAddressesSingle(@PathVariable("addressid") String addressId){

        AddressesRest returnValue=new AddressesRest();
        AddressDto userAddressDto=addressService.getSingleAddress(addressId);

        if(userAddressDto !=null ){
            //java.lang.reflect.Type //ini make library modelmapper

            ModelMapper modelMapper=new ModelMapper();
            returnValue=modelMapper.map(userAddressDto,AddressesRest.class);
        }


        return returnValue;
    }
}
