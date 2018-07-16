package com.appsinfinity.fingercricket.api;

import com.appsinfinity.fingercricket.models.AddressModel;

import retrofit.Callback;
import retrofit.http.GET;

public interface ApiService {

    @GET(APIConstant.ADDRESS_URL)
    void address(Callback<AddressModel> addressModelRestCallback);


}
