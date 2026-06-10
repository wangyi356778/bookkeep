package com.example.jizhangben.network;

import com.example.jizhangben.model.LoginRequest;
import com.example.jizhangben.model.LoginResponse;
import com.example.jizhangben.model.RegisterRequest;
import com.example.jizhangben.model.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}