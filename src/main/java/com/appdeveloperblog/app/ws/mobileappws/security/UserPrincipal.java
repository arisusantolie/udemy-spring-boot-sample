package com.appdeveloperblog.app.ws.mobileappws.security;

import com.appdeveloperblog.app.ws.mobileappws.entity.AuthorityEntity;
import com.appdeveloperblog.app.ws.mobileappws.entity.RoleEntity;
import com.appdeveloperblog.app.ws.mobileappws.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {


    private static final long serialVersionUID = -1283748176471911896L;

    UserEntity userEntity;

    public UserPrincipal(UserEntity entity) {
        this.userEntity=entity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities=new ArrayList<>();
        List<AuthorityEntity> authorityEntities=new ArrayList<>();

        //get user roles
        Collection<RoleEntity> roles=userEntity.getRoles();
        if(roles==null) return authorities;

        roles.forEach(x->{
            authorities.add(new SimpleGrantedAuthority(x.getName()));
            authorityEntities.addAll(x.getAuthorities());
        });

        authorityEntities.forEach(authority->{
            authorities.add(new SimpleGrantedAuthority(authority.getName()));
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userEntity.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return this.userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
