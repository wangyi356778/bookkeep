package com.example.jizhangben.model;

public class BaseResponse {
    private boolean success;
    private String message;
    private Object data;

    public BaseResponse() {}

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}
