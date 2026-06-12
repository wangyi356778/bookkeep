package com.bookkeep.bookkeepapi.service;

import com.bookkeep.bookkeepapi.entity.Record;
import com.bookkeep.bookkeepapi.entity.User;
import com.bookkeep.bookkeepapi.mapper.RecordMapper;
import com.bookkeep.bookkeepapi.mapper.UserMapper;
import com.bookkeep.bookkeepapi.model.AddRecordRequest;
import com.bookkeep.bookkeepapi.model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordService {

    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private UserMapper userMapper;

    // 添加记账记录
    public BaseResponse addRecord(Long userId, AddRecordRequest request) {
        if (request.getType() == null || (!"income".equals(request.getType()) && !"expense".equals(request.getType()))) {
            return new BaseResponse(false, "收支类型不正确");
        }
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            return new BaseResponse(false, "请选择类目");
        }
        if (request.getAmount() == null || request.getAmount() <= 0) {
            return new BaseResponse(false, "金额必须大于0");
        }
        if (request.getDate() == null || request.getDate().trim().isEmpty()) {
            return new BaseResponse(false, "请选择日期");
        }

        Record record = new Record(userId, request.getType(), request.getCategory(),
                request.getAmount(), request.getDate(), request.getRemark());
        recordMapper.insert(record);

        return new BaseResponse(true, "添加成功", record);
    }

    // 获取用户所有记录
    public BaseResponse getRecords(Long userId) {
        List<Record> records = recordMapper.findByUserId(userId);
        return new BaseResponse(true, "查询成功", records);
    }

    // 编辑记录
    public BaseResponse updateRecord(Long userId, Long recordId, AddRecordRequest request) {
        Record existing = recordMapper.findById(recordId);
        if (existing == null) {
            return new BaseResponse(false, "记录不存在");
        }
        if (!existing.getUserId().equals(userId)) {
            return new BaseResponse(false, "无权操作此记录");
        }
        if (request.getCategory() != null) existing.setCategory(request.getCategory());
        if (request.getAmount() != null && request.getAmount() > 0) existing.setAmount(request.getAmount());
        if (request.getDate() != null) existing.setDate(request.getDate());
        if (request.getRemark() != null) existing.setRemark(request.getRemark());

        recordMapper.update(existing);
        return new BaseResponse(true, "修改成功", existing);
    }

    // 删除记录
    public BaseResponse deleteRecord(Long userId, Long recordId) {
        Record existing = recordMapper.findById(recordId);
        if (existing == null) {
            return new BaseResponse(false, "记录不存在");
        }
        if (!existing.getUserId().equals(userId)) {
            return new BaseResponse(false, "无权操作此记录");
        }
        recordMapper.deleteById(recordId);
        return new BaseResponse(true, "删除成功");
    }

    // 月度汇总
    public BaseResponse getMonthlySummary(Long userId, String month) {
        Map<String, Object> summary = recordMapper.sumByMonth(userId, month);
        double totalIncome = summary.get("totalIncome") != null ? ((Number) summary.get("totalIncome")).doubleValue() : 0;
        double totalExpense = summary.get("totalExpense") != null ? ((Number) summary.get("totalExpense")).doubleValue() : 0;

        Map<String, Object> result = new HashMap<>();
        result.put("month", month);
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("balance", totalIncome - totalExpense);

        return new BaseResponse(true, "查询成功", result);
    }

    // 记账总次数
    public int getRecordCount(Long userId) {
        return recordMapper.countByUserId(userId);
    }

    // 分类统计
    public BaseResponse getCategoryStats(Long userId, String type) {
        List<Map<String, Object>> stats = recordMapper.sumByCategory(userId, type);
        return new BaseResponse(true, "查询成功", stats);
    }

    // 获取用户信息
    public User getUserInfo(String phone) {
        return userMapper.findByPhone(phone);
    }

    // 更新用户信息
    public BaseResponse updateUserProfile(User user) {
        userMapper.update(user);
        return new BaseResponse(true, "修改成功");
    }
}
