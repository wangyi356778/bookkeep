package com.example.jizhangben.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jizhangben.LoginActivity;
import com.example.jizhangben.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 观察用户名
        notificationsViewModel.getUsername().observe(getViewLifecycleOwner(), username ->
                binding.tvProfileUsername.setText(username));

        // 退出登录按钮
        binding.btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        // 菜单项点击（预留功能）
        binding.menuDataManage.setOnClickListener(v ->
                com.google.android.material.snackbar.Snackbar.make(root, "数据管理功能开发中...", 1500).show());
        binding.menuSettings.setOnClickListener(v ->
                com.google.android.material.snackbar.Snackbar.make(root, "设置功能开发中...", 1500).show());
        binding.menuAbout.setOnClickListener(v ->
                com.google.android.material.snackbar.Snackbar.make(root, "记账本 v1.0.0", 1500).show());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
