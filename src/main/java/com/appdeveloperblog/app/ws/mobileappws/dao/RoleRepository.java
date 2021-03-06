package com.appdeveloperblog.app.ws.mobileappws.dao;

import com.appdeveloperblog.app.ws.mobileappws.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity,Long> {
    RoleEntity findByName(String name);
}
