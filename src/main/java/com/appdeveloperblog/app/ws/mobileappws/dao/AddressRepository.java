package com.appdeveloperblog.app.ws.mobileappws.dao;

import com.appdeveloperblog.app.ws.mobileappws.entity.AddressEntity;
import com.appdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity,Long> {

    public List<AddressEntity> findByUserDetails(UserEntity userEntity);

    public AddressEntity findByAddressId(String addressId);
}
