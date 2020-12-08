package com.appdeveloperblog.app.ws.mobileappws.entity;

import com.appdeveloperblog.app.ws.mobileappws.dto.UserDto;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "address")
public class AddressEntity implements Serializable {
    private static final long serialVersionUID = 7318059623703257629L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,length = 30)
    private String addressId;

    @Column(nullable = false,length = 30)
    private String city;

    @Column(nullable = false,length = 15)
    private String country;

    @Column(nullable = false,length = 7)
    private String postalCode;

    @Column(nullable = false,length = 100)
    private String streetName;

    @Column(nullable = false,length = 10)
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userDetails;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }
}