package com.example.jizhangben.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // ⚠️ 注意：如果使用模拟器，访问电脑上的后端，请使用 10.0.2.2
    // 如果使用真机，填电脑的局域网 IP（如 192.168.1.100）
    private static final String BASE_URL = "http://172.24.14.136:8080/";
    private static Retrofit retrofit;

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