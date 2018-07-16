package com.appsinfinity.fingercricket.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by admin on 7/5/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressModel {
    private String as;
    private String city;
    private String country;

    public String getAs() {
        return as;
    }

    public void setAs(String as) {
        this.as = as;
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
}
