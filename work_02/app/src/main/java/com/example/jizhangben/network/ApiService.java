package com.example.jizhangben.network;

import com.example.jizhangben.model.AddRecordRequest;
import com.example.jizhangben.model.BaseResponse;
import com.example.jizhangben.model.LoginRequest;
import com.example.jizhangben.model.LoginResponse;
import com.example.jizhangben.model.RegisterRequest;
import com.example.jizhangben.model.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("api/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/record")
    Call<BaseResponse> addRecord(@Body AddRecordRequest request, @Query("phone") String phone);

    @GET("api/records")
    Call<BaseResponse> getRecords(@Query("phone") String phone);

    @PUT("api/record/{id}")
    Call<BaseResponse> updateRecord(@Path("id") Long id, @Body AddRecordRequest request,
                                    @Query("phone") String phone);

    @DELETE("api/record/{id}")
    Call<BaseResponse> deleteRecord(@Path("id") Long id, @Query("phone") String phone);

    @GET("api/record/summary")
    Call<BaseResponse> getMonthlySummary(@Query("phone") String phone, @Query("month") String month);

    @GET("api/record/count")
    Call<BaseResponse> getRecordCount(@Query("phone") String phone);

    @GET("api/user/info")
    Call<BaseResponse> getUserInfo(@Query("phone") String phone);

    @PUT("api/user/info")
    Call<BaseResponse> updateUserProfile(@Body Object user, @Query("phone") String phone);
}
