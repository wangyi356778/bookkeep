package com.bookkeep.bookkeepapi.model;

public class AddRecordRequest {
    private String type;        // income / expense
    private String category;
    private Double amount;
    private String date;        // yyyy-MM-dd
    private String remark;

    public AddRecordRequest() {}

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
