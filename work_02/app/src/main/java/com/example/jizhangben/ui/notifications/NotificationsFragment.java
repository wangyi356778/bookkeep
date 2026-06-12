package com.example.jizhangben.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.jizhangben.EditProfileActivity;
import com.example.jizhangben.LoginActivity;
import com.example.jizhangben.databinding.FragmentNotificationsBinding;
import com.example.jizhangben.repository.UserRepository;
import com.example.jizhangben.utils.TokenManager;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private UserRepository userRepository;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userRepository = new UserRepository();
        String phone = TokenManager.getPhone(requireContext());

        // 加载用户信息
        loadUserInfo(phone);

        // 加载记账总次数
        loadRecordCount(phone);

        // 退出登录按钮
        binding.btnLogout.setOnClickListener(v -> {
            TokenManager.clearToken(requireContext());
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        // 数据管理 - 导出提示
        binding.menuDataManage.setOnClickListener(v ->
                new android.app.AlertDialog.Builder(requireContext())
                        .setTitle("数据管理")
                        .setMessage("当前版本支持：\n\n" +
                                "• 查看所有记账记录\n" +
                                "• 删除单条记录（首页长按）\n" +
                                "• 后续将支持导出Excel功能")
                        .setPositiveButton("知道了", null)
                        .show());

        // 设置 - 显示当前配置信息
        binding.menuSettings.setOnClickListener(v ->
                new android.app.AlertDialog.Builder(requireContext())
                        .setTitle("应用设置")
                        .setMessage("服务器地址: " + com.example.jizhangben.network.RetrofitClient.getBaseUrl() +
                                "\n\n当前版本: v1.0.0")
                        .setPositiveButton("确定", null)
                        .show());

        // 关于应用 - 完整信息
        binding.menuAbout.setOnClickListener(v ->
                new android.app.AlertDialog.Builder(requireContext())
                        .setTitle("关于记账本")
                        .setMessage("版本: 1.0.0\n\n" +
                                "一款简洁实用的个人记账工具，\n" +
                                "帮助您轻松管理日常收支。\n\n" +
                                "技术栈:\n" +
                                "Android + Material Design\n" +
                                "Spring Boot + MyBatis + MySQL\n\n" +
                                "开发者: BookKeep Team")
                        .setPositiveButton("确定", null)
                        .setNeutralButton("开源地址", (dialog, which) -> {
                            try {
                                Intent browser = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://github.com/wangyi356778/bookkeep"));
                                startActivity(browser);
                            } catch (Exception ignored) {}
                        })
                        .show());

        // 点击头像区域跳转编辑个人信息
        binding.layoutProfileHeader.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivity(intent);
        });

        return root;
    }

    private void loadUserInfo(String phone) {
        if (phone == null) return;
        userRepository.getUserInfo(phone, new UserRepository.DataCallback() {
            @Override
            public void onSuccess(com.example.jizhangben.model.BaseResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    try {
                        @SuppressWarnings("unchecked")
                        java.util.LinkedHashMap<String, Object> user =
                                (java.util.LinkedHashMap<String, Object>) response.getData();

                        // 姓名（优先显示姓名，否则手机号）
                        String name = (String) user.get("name");
                        if (name != null && !name.isEmpty()) {
                            binding.tvProfileUsername.setText(name);
                        } else {
                            binding.tvProfileUsername.setText(phone);
                        }

                        // 详细信息：年龄/职业/性别
                        StringBuilder detail = new StringBuilder();
                        Object ageObj = user.get("age");
                        if (ageObj != null) {
                            detail.append(ageObj).append("岁");
                        }
                        String occupation = (String) user.get("occupation");
                        if (occupation != null && !occupation.isEmpty()) {
                            if (detail.length() > 0) detail.append(" · ");
                            detail.append(occupation);
                        }
                        String gender = (String) user.get("gender");
                        if (gender != null && !gender.isEmpty()) {
                            if (detail.length() > 0) detail.append(" · ");
                            detail.append(gender);
                        }

                        if (detail.length() > 0) {
                            binding.tvProfileDetail.setText(detail.toString());
                            binding.tvProfileDetail.setVisibility(View.VISIBLE);
                        } else {
                            binding.tvProfileDetail.setText("点击编辑个人信息");
                            binding.tvProfileDetail.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        binding.tvProfileUsername.setText(phone);
                        binding.tvProfileDetail.setText("点击编辑个人信息");
                    }
                } else {
                    binding.tvProfileUsername.setText(phone);
                    binding.tvProfileDetail.setText("点击编辑个人信息");
                }
            }

            @Override
            public void onError(String errorMessage) {
                binding.tvProfileUsername.setText(phone);
                binding.tvProfileDetail.setText("点击编辑个人信息");
            }
        });
    }

    private void loadRecordCount(String phone) {
        if (phone == null) return;
        userRepository.getRecordCount(phone, new UserRepository.DataCallback() {
            @Override
            public void onSuccess(com.example.jizhangben.model.BaseResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    int count = ((Number) response.getData()).intValue();
                    binding.tvRecordCount.setText(String.format("共记账 %d 次", count));
                }
            }

            @Override
            public void onError(String errorMessage) { /* 静默失败 */ }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 回到我的页面时刷新用户信息
        if (userRepository != null) {
            loadUserInfo(TokenManager.getPhone(requireContext()));
            loadRecordCount(TokenManager.getPhone(requireContext()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
