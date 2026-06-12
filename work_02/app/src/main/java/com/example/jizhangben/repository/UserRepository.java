package com.example.jizhangben.repository;

import com.example.jizhangben.model.AddRecordRequest;
import com.example.jizhangben.model.BaseResponse;
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

    /**
     * 注册（完整版：手机号+密码+确认密码+姓名+年龄+职业+性别）
     */
    public void register(String phone, String password, String confirmPassword,
                        String name, String age, String occupation, String gender,
                        RegisterCallback callback) {
        RegisterRequest request = new RegisterRequest(phone, password, confirmPassword,
                name, age, occupation, gender);
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

    /**
     * 登录（手机号+密码）
     */
    public void login(String phone, String password, LoginCallback callback) {
        LoginRequest request = new LoginRequest(phone, password);
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

    // ========== 记账相关 ==========

    /** 添加记账记录 */
    public void addRecord(String phone, AddRecordRequest request, DataCallback callback) {
        Call<BaseResponse> call = apiService.addRecord(request, phone);
        call.enqueue(new BaseCallback(callback));
    }

    /** 获取用户所有记录 */
    public void getRecords(String phone, DataCallback callback) {
        Call<BaseResponse> call = apiService.getRecords(phone);
        call.enqueue(new BaseCallback(callback));
    }

    /** 编辑记录 */
    public void updateRecord(Long id, String phone, AddRecordRequest request, DataCallback callback) {
        Call<BaseResponse> call = apiService.updateRecord(id, request, phone);
        call.enqueue(new BaseCallback(callback));
    }

    /** 删除记录 */
    public void deleteRecord(Long id, String phone, DataCallback callback) {
        Call<BaseResponse> call = apiService.deleteRecord(id, phone);
        call.enqueue(new BaseCallback(callback));
    }

    /** 月度汇总 */
    public void getMonthlySummary(String phone, String month, DataCallback callback) {
        Call<BaseResponse> call = apiService.getMonthlySummary(phone, month);
        call.enqueue(new BaseCallback(callback));
    }

    /** 记账总次数 */
    public void getRecordCount(String phone, DataCallback callback) {
        Call<BaseResponse> call = apiService.getRecordCount(phone);
        call.enqueue(new BaseCallback(callback));
    }

    // ========== 用户信息 ==========

    /** 获取用户信息 */
    public void getUserInfo(String phone, DataCallback callback) {
        Call<BaseResponse> call = apiService.getUserInfo(phone);
        call.enqueue(new BaseCallback(callback));
    }

    /** 更新用户信息 */
    public void updateUserProfile(Object user, String phone, DataCallback callback) {
        Call<BaseResponse> call = apiService.updateUserProfile(user, phone);
        call.enqueue(new BaseCallback(callback));
    }

    // ========== 通用回调 ==========

    private static class BaseCallback implements Callback<BaseResponse> {
        private final DataCallback callback;

        BaseCallback(DataCallback callback) { this.callback = callback; }

        @Override
        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                callback.onSuccess(response.body());
            } else {
                callback.onError("请求失败，服务器错误");
            }
        }

        @Override
        public void onFailure(Call<BaseResponse> call, Throwable t) {
            callback.onError("网络错误：" + t.getMessage());
        }
    }

    public interface RegisterCallback {
        void onSuccess(RegisterResponse response);
        void onError(String errorMessage);
    }

    public interface LoginCallback {
        void onSuccess(LoginResponse response);
        void onError(String errorMessage);
    }

    public interface DataCallback {
        void onSuccess(BaseResponse response);
        void onError(String errorMessage);
    }
}
