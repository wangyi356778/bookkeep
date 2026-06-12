package com.bookkeep.bookkeepapi.entity;

public class Record {
    private Long id;
    private Long userId;
    private String type;        // income(收入) / expense(支出)
    private String category;   // 类目：工资、餐饮、交通等
    private Double amount;
    private String date;       // 日期 yyyy-MM-dd
    private String remark;     // 备注

    public Record() {}

    public Record(Long userId, String type, String category, Double amount, String date, String remark) {
        this.userId = userId;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.remark = remark;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
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
