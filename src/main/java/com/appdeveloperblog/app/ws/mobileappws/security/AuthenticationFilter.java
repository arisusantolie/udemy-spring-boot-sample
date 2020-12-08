package com.appdeveloperblog.app.ws.mobileappws.security;

import com.appdeveloperblog.app.ws.mobileappws.SpringApplicationContext;
import com.appdeveloperblog.app.ws.mobileappws.dto.UserDto;
import com.appdeveloperblog.app.ws.mobileappws.models.request.UserLoginRequestModel;
import com.appdeveloperblog.app.ws.mobileappws.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override //ketika loggin method ini akan otomatis ke trigger
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            UserLoginRequestModel creds=new ObjectMapper()
                    .readValue(request.getInputStream(),UserLoginRequestModel.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(),creds.getPassword(),new ArrayList<>())
            );
        }catch (IOException ex){
            throw new RuntimeException();
        }
    }

    @Override //akan ke trigger ketika login berhasil
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

//        String userName=((User) authResult.getPrincipal()).getUsername(); //karna makai custom tidak amke User dari spring
        String userName=((UserPrincipal) authResult.getPrincipal()).getUsername();
        Claims claims = Jwts.claims().setSubject(((UserPrincipal) authResult.getPrincipal()).getUsername());

        var roles =((UserPrincipal) authResult.getPrincipal()).getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token= Jwts.builder() //untuk set Json Web Token
                .setSubject(userName)
                .claim("Roles",roles)//nambah data ke data payload jwt
                .claim("test","test")
                .setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512,  SecurityConstants.getTokenSecret())
                .compact();

        UserService userService=(UserService) SpringApplicationContext.getBean("userService"); //cast ke userservice untuk bisa dapat user id
        UserDto userDto=userService.getUser(userName);
        response.addHeader(SecurityConstants.HEADER_STRING,SecurityConstants.TOKEN_PREFIX+token);
        response.addHeader("UserId",userDto.getUserId()); //userId itu key, lalu habis itu isi.

    }

//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//
//        ObjectMapper mapper = new ObjectMapper();
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(mapper.writeValueAsString("unAuthorized"));
//
//    }
}
