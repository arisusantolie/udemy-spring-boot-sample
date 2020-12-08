package com.appdeveloperblog.app.ws.mobileappws.dao;

import com.appdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);
}
