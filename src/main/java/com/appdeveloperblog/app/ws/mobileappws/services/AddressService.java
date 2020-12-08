package com.appdeveloperblog.app.ws.mobileappws.services;

import com.appdeveloperblog.app.ws.mobileappws.dao.AddressRepository;
import com.appdeveloperblog.app.ws.mobileappws.dao.UserRepository;
import com.appdeveloperblog.app.ws.mobileappws.dto.AddressDto;
import com.appdeveloperblog.app.ws.mobileappws.entity.AddressEntity;
import com.appdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import com.appdeveloperblog.app.ws.mobileappws.exception.CustomGenericException;
import com.appdeveloperblog.app.ws.mobileappws.models.response.AddressesRest;
import com.appdeveloperblog.app.ws.mobileappws.models.response.UserRest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    public List<AddressDto> getListAddressUser(String userId){
        List<AddressDto> returnValue=new ArrayList<>();

        UserEntity userEntity=userRepository.findByUserId(userId);

        if(userEntity==null){
            throw new CustomGenericException("User Tidak Ada");
        }
        List<AddressEntity> addresses=addressRepository.findByUserDetails(userEntity);
        ModelMapper modelMapper=new ModelMapper();
        addresses.forEach(x->{
            returnValue.add(modelMapper.map(x,AddressDto.class));
        });
        return returnValue;

    }

    public AddressDto getSingleAddress(String addressId){
        AddressEntity addressEntity=addressRepository.findByAddressId(addressId);

        if(addressEntity==null){
            throw new CustomGenericException("Address Doesnt Exist");
        }

        AddressDto returnValue=new AddressDto();

        ModelMapper modelMapper=new ModelMapper();

        returnValue=modelMapper.map(addressEntity,AddressDto.class);

        return returnValue;
    }
}
