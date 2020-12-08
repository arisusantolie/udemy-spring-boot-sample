package com.appdeveloperblog.app.ws.mobileappws;

import com.appdeveloperblog.app.ws.mobileappws.dao.AuthorityRepository;
import com.appdeveloperblog.app.ws.mobileappws.dao.RoleRepository;
import com.appdeveloperblog.app.ws.mobileappws.dao.UserRepository;
import com.appdeveloperblog.app.ws.mobileappws.entity.AuthorityEntity;
import com.appdeveloperblog.app.ws.mobileappws.entity.RoleEntity;
import com.appdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import com.appdeveloperblog.app.ws.mobileappws.utils.UtilsGenerateRandomId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;

@Component
public class InitialUserSetup {
    // untuk buat component dimana nantinya setelah aplikasi di run, akan jalan method yang menggunakan anotattion event listerner / aplikasi start -> method di ekseskusi

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UtilsGenerateRandomId  utilsGenerateRandomId;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;

    @EventListener
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event){
        System.out.println("testtttt bro");

        AuthorityEntity readAuthority= createAuthority("READ_AUTHORITY");
        AuthorityEntity writeAuthority= createAuthority("WRITE_AUTHORITY");
        AuthorityEntity deleteAuthority= createAuthority("DELETE_AUTHORITY");


        RoleEntity roleUser= createRole("ROLE_USER", Arrays.asList(readAuthority,writeAuthority));
        RoleEntity roleAdmin=createRole("ROLE_ADMIN", Arrays.asList(readAuthority,writeAuthority,deleteAuthority));

        if(roleAdmin==null) return;

//        UserEntity adminUser=new UserEntity();
//        adminUser.setFirstName("Ari");
//        adminUser.setLastName("lie");
//        adminUser.setEmail("arilie123@gmail.com");
//        adminUser.setEmailVerificationStatus(true);
//        adminUser.setUserId(utilsGenerateRandomId.generateRandomId(30));
//        adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("demak1997"));
//        adminUser.setRoles(Arrays.asList(roleAdmin));
//        userRepository.save(adminUser);
    }

    private AuthorityEntity createAuthority(String name){
        AuthorityEntity authorityEntity=authorityRepository.findByName(name);

        if(authorityEntity==null){
            authorityEntity = new AuthorityEntity(name);
            authorityRepository.save(authorityEntity);
        }
        return authorityEntity;
    }

    private RoleEntity createRole(String name,
                                  Collection<AuthorityEntity> authorities){
        RoleEntity roleEntity=roleRepository.findByName(name);

        if(roleEntity==null){
            roleEntity = new RoleEntity(name);
            roleEntity.setAuthorities(authorities);
            roleRepository.save(roleEntity);
        }
        return roleEntity;
    }
}
