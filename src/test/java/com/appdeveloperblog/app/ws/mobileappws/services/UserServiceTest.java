package com.appdeveloperblog.app.ws.mobileappws.services;

import com.appdeveloperblog.app.ws.mobileappws.dao.UserRepository;
import com.appdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp()throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    final void testGetUser(){
        UserEntity userEntity=new UserEntity();

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(userEntity);
    }
}
