package com.bookkeep.bookkeepapi.controller;

import com.bookkeep.bookkeepapi.entity.User;
import com.bookkeep.bookkeepapi.mapper.UserMapper;
import com.bookkeep.bookkeepapi.model.AddRecordRequest;
import com.bookkeep.bookkeepapi.model.BaseResponse;
import com.bookkeep.bookkeepapi.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RecordController {

    @Autowired
    private RecordService recordService;
    @Autowired
    private UserMapper userMapper;

    // 添加记账记录
    @PostMapping("/record")
    public BaseResponse addRecord(@RequestBody AddRecordRequest request,
                                  @RequestParam String phone) {
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            return new BaseResponse(false, "用户不存在");
        }
        return recordService.addRecord(user.getId(), request);
    }

    // 获取用户所有记录
    @GetMapping("/records")
    public BaseResponse getRecords(@RequestParam String phone) {
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            return new BaseResponse(false, "用户不存在");
        }
        return recordService.getRecords(user.getId());
    }

    // 编辑记录
    @PutMapping("/record/{id}")
    public BaseResponse updateRecord(@PathVariable Long id,
                                     @RequestBody AddRecordRequest request,
                                     @RequestParam String phone) {
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            return new BaseResponse(false, "用户不存在");
        }
        return recordService.updateRecord(user.getId(), id, request);
    }

    // 删除记录
    @DeleteMapping("/record/{id}")
    public BaseResponse deleteRecord(@PathVariable Long id,
                                     @RequestParam String phone) {
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            return new BaseResponse(false, "用户不存在");
        }
        return recordService.deleteRecord(user.getId(), id);
    }

    // 月度汇总
    @GetMapping("/record/summary")
    public BaseResponse getMonthlySummary(@RequestParam String phone,
                                          @RequestParam(required = false) String month) {
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            return new BaseResponse(false, "用户不存在");
        }
        if (month == null || month.isEmpty()) {
            month = java.time.LocalDate.now().toString().substring(0, 7); // 当前月份 yyyy-MM
        }
        return recordService.getMonthlySummary(user.getId(), month);
    }

    // 记账总次数
    @GetMapping("/record/count")
    public BaseResponse getRecordCount(@RequestParam String phone) {
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            return new BaseResponse(false, "用户不存在");
        }
        int count = recordService.getRecordCount(user.getId());
        return new BaseResponse(true, "查询成功", count);
    }

    // 分类统计
    @GetMapping("/record/category-stats")
    public BaseResponse getCategoryStats(@RequestParam String phone,
                                         @RequestParam String type) {
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            return new BaseResponse(false, "用户不存在");
        }
        return recordService.getCategoryStats(user.getId(), type);
    }

    // 获取用户信息
    @GetMapping("/user/info")
    public BaseResponse getUserInfo(@RequestParam String phone) {
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            return new BaseResponse(false, "用户不存在");
        }
        return new BaseResponse(true, "查询成功", user);
    }

    // 更新用户信息
    @PutMapping("/user/info")
    public BaseResponse updateUserProfile(@RequestBody User user,
                                         @RequestParam String phone) {
        User existing = userMapper.findByPhone(phone);
        if (existing == null) {
            return new BaseResponse(false, "用户不存在");
        }
        user.setId(existing.getId());
        user.setPhone(existing.getPhone()); // 手机号不可修改
        user.setPassword(existing.getPassword());
        return recordService.updateUserProfile(user);
    }
}
