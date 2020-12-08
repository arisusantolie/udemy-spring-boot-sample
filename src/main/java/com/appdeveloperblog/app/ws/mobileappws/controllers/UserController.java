package com.appdeveloperblog.app.ws.mobileappws.controllers;

import com.appdeveloperblog.app.ws.mobileappws.dao.UserRepository;
import com.appdeveloperblog.app.ws.mobileappws.dto.UserDto;
import com.appdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import com.appdeveloperblog.app.ws.mobileappws.exception.CustomGenericException;
import com.appdeveloperblog.app.ws.mobileappws.exception.ErrorMessage;
import com.appdeveloperblog.app.ws.mobileappws.exception.ErrorMessages;
import com.appdeveloperblog.app.ws.mobileappws.exception.UserServiceException;
import com.appdeveloperblog.app.ws.mobileappws.models.request.UserDetailsRequestModel;
import com.appdeveloperblog.app.ws.mobileappws.models.response.UserRest;
import com.appdeveloperblog.app.ws.mobileappws.security.SecurityConstants;
import com.appdeveloperblog.app.ws.mobileappws.security.UserPrincipal;
import com.appdeveloperblog.app.ws.mobileappws.services.UserService;
import io.jsonwebtoken.Jwts;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Date;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        if (userDetails.getFirstName().isEmpty())
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());

//        UserDto userDto=new UserDto();
//        BeanUtils.copyProperties(userDetails,userDto);

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto creadtedUser = userService.createUser(userDto);

        System.out.println(creadtedUser.getAddresses());

        returnValue= modelMapper.map(creadtedUser,UserRest.class);
//
//        BeanUtils.copyProperties(creadtedUser, returnValue);

        return returnValue;
    }

    @GetMapping("/{id}")
    public UserRest getUser(@PathVariable("id") String id) {

        UserRest returnValue = new UserRest();

        UserDto existData = userService.getUserByUserId(id);


        //        Users existUser=usersRepository.findByUserId(userID).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        //Users existUser=usersRepository.findByUserId(userID).orElseThrow(()->new Exception(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
//        if(existUser==null) throw new UsernameNotFoundException(userID);
//        Users existUser=usersRepository.findByUserId(userID).orElseThrow(()->new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
//        Users existUser=usersRepository.findByUserId(userID).orElseThrow(()->new NullPointerException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        BeanUtils.copyProperties(existData, returnValue);
        return returnValue;
    }

    @PutMapping("/{id}")
    public UserRest updateUser(@PathVariable("id") String id, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto creadtedUser = userService.updateUser(id, userDto);

        BeanUtils.copyProperties(creadtedUser, returnValue);

        return returnValue;
    }

    @DeleteMapping("/{id}")
    public ErrorMessage deleteUser(@PathVariable("id") String id){
        Long Iduser = userService.deleteUser(id);

        return new ErrorMessage(new Date(),"Delete Successfull for User id : "+Iduser) ;
    }

    @GetMapping("testing")
    public Collection<GrantedAuthority> userPrincipalget(HttpServletRequest request){

        String token=request.getHeader(SecurityConstants.HEADER_STRING);
        String user= Jwts.parser()
                .setSigningKey(SecurityConstants.getTokenSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        UserEntity userEntity=userRepository.findByEmail(user);
        UserPrincipal userPrincipal=new UserPrincipal(userEntity);
        return (Collection<GrantedAuthority>) userPrincipal.getAuthorities();
    }



}
