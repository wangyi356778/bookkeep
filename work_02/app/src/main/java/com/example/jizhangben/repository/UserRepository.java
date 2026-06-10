package com.example.jizhangben.repository;

import com.example.jizhangben.model.LoginRequest;
import com.example.jizhangben.model.LoginResponse;
import com.example.jizhangben.model.RegisterRequest;
import com.example.jizhangben.model.RegisterResponse;
import com.example.jizhangben.network.ApiService;
import com.example.jizhangben.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private ApiService apiService;

    public UserRepository() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public void register(String username, String password, String age, RegisterCallback callback) {
        RegisterRequest request = new RegisterRequest(username, password, age);
        Call<RegisterResponse> call = apiService.register(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("注册失败，服务器错误");
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                callback.onError("网络错误：" + t.getMessage());
            }
        });
    }

    public void login(String username, String password, LoginCallback callback) {
        LoginRequest request = new LoginRequest(username, password);
        Call<LoginResponse> call = apiService.login(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("登录失败，服务器错误");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError("网络错误：" + t.getMessage());
            }
        });
    }

    public interface RegisterCallback {
        void onSuccess(RegisterResponse response);
        void onError(String errorMessage);
    }

    public interface LoginCallback {
        void onSuccess(LoginResponse response);
        void onError(String errorMessage);
    }
}