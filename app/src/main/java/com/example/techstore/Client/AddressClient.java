package com.example.techstore.Client;

import java.security.PrivateKey;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://provinces.open-api.vn/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
