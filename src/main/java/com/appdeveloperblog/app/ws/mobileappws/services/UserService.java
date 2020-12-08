package com.appdeveloperblog.app.ws.mobileappws.services;

import com.appdeveloperblog.app.ws.mobileappws.dao.UserRepository;
import com.appdeveloperblog.app.ws.mobileappws.dto.UserDto;
import com.appdeveloperblog.app.ws.mobileappws.entity.AddressEntity;
import com.appdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import com.appdeveloperblog.app.ws.mobileappws.exception.*;
import com.appdeveloperblog.app.ws.mobileappws.security.UserPrincipal;
import com.appdeveloperblog.app.ws.mobileappws.utils.UtilsGenerateRandomId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UtilsGenerateRandomId utilsGenerateRandomId;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserDto createUser(UserDto user){

        UserEntity existUserEntity=userRepository.findByEmail(user.getEmail());
        ModelMapper modelMapper=new ModelMapper();
        if(existUserEntity != null) throw new RuntimeException("Record Already Exist");

       List<AddressEntity> listAddressDto=new ArrayList<>();



        UserEntity userEntity= modelMapper.map(user,UserEntity.class);
//        BeanUtils.copyProperties(user,userEntity);




        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userEntity.setUserId(utilsGenerateRandomId.generateRandomId(30));
        userEntity.setEmailVerificationStatus(true);

        AtomicInteger i= new AtomicInteger();
        user.getAddresses().forEach(x->{

            AddressEntity addressEntity=modelMapper.map(user.getAddresses().get(i.get()),AddressEntity.class);
            addressEntity.setUserDetails(userEntity);
            addressEntity.setAddressId(utilsGenerateRandomId.generateRandomId(30));
            System.out.println(addressEntity.getCity());

            listAddressDto.add(addressEntity);
            i.getAndIncrement();
        });
        userEntity.setAddresses(listAddressDto);
        UserEntity storedUserDetails= userRepository.save(userEntity);

        UserDto returnValue= modelMapper.map(storedUserDetails,UserDto.class);

//        BeanUtils.copyProperties(storedUserDetails,returnValue);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //method untuk ketika user login kepanggil
         UserEntity userEntity= userRepository.findByEmail(email);

         if(userEntity == null) throw new UsernameNotFoundException(email); //usernamefound itu dari spring security udah exist

        return new UserPrincipal(userEntity);

//        return new User(userEntity.getEmail(),userEntity.getEncryptedPassword(),new ArrayList<>()); // email,password,dll seperti status aktif block,authority,
    }

    public UserDto getUser(String email){
        UserEntity userEntity= userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email); //usernamefound itu dari spring security udah exist


        UserDto returnValue=new UserDto();

        BeanUtils.copyProperties(userEntity,returnValue);

         return returnValue;
    }

    public UserDto getUserByUserId(String userId){
        UserEntity storedData=userRepository.findByUserId(userId);

        System.out.println(storedData);
        if(storedData==null){
            throw new RuntimeException("doesnt exist User");
        }
        UserDto returnValue=new UserDto();
        BeanUtils.copyProperties(storedData,returnValue);

        return returnValue;
    }


    public UserDto updateUser(String userId,UserDto user){

        UserEntity existUserEntity=userRepository.findByUserId(userId);



//        if(existUserEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        if(existUserEntity == null) throw new CustomGenericException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());

        existUserEntity.setFirstName(user.getFirstName());

        existUserEntity.setLastName(user.getLastName());
        existUserEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        UserEntity updateUserDetail= userRepository.save(existUserEntity);

        UserDto returnValue=new UserDto();

        BeanUtils.copyProperties(updateUserDetail,returnValue);

        return returnValue;
    }

    public Long deleteUser(String UserId){
        UserEntity userEntity=userRepository.findByUserId(UserId);

        if(userEntity==null){
            throw new CustomGenericException("Doesn't Exist this user");
        }

        userRepository.delete(userEntity);

        return userEntity.getId();
    }
}
