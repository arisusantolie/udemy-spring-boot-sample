package com.appdeveloperblog.app.ws.mobileappws.security;

import com.appdeveloperblog.app.ws.mobileappws.dao.UserRepository;
import com.appdeveloperblog.app.ws.mobileappws.services.UserService;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@EnableGlobalMethodSecurity // untuk bisa menggunakan annotasi security authorize gituan dll di level controller dan service
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,UserRepository userRepository) { //untuk manggil user detail service yang di override
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository=userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { //untuk configure endpoint , nge protect endpoint dll
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                .permitAll()
                .antMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN") //sebenarnya di db ROLE_ADMIN, tp cukup kasih ADMIN, karna spring otomati inject jadi ROLE_ADMIN
//                .antMatchers(HttpMethod.DELETE,"/users/**").hasAuthority("DELETE_AUTHORITY")
//                .antMatchers(HttpMethod.DELETE,"/users/**").hasAnyAuthority("DELETE_AUTHORITY","DELETE_ALL_AUTHORITY")
//                .antMatchers(HttpMethod.DELETE,"/users/**").hasAnyAuthority("ADMIN","PERSONALIA")
                .anyRequest().authenticated().and().addFilter(getAuthenticationFilter()) //custom url login addfilter
                .addFilter(new AuthorizationFilter(authenticationManager(),userRepository)) //authorization filter
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); //biar spring security tidak membaut session, sehingga kalau header awalnya dikirim lalu di hit lalu tanpa dikirim otomatis tidak bisa masuk karna session tidak ada
//                .anyRequest().authenticated().and().addFilter(new AuthenticationFilter(authenticationManager())); default url login jadi localhost:8080/login
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder); //untuk decode password dari db
    }

    public AuthenticationFilter getAuthenticationFilter() throws Exception{ //untuk customize url login
        final AuthenticationFilter filter=new AuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/users/login");
        return filter;
    }
}
