package com.example.jizhangben.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jizhangben.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 设置月度标签
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy年M月", java.util.Locale.CHINA);
        String monthLabel = sdf.format(new java.util.Date());
        binding.tvMonthLabel.setText(monthLabel);

        // 观察数据变化
        homeViewModel.getBalance().observe(getViewLifecycleOwner(), balance ->
                binding.tvBalance.setText("¥ " + String.format("%.2f", balance)));

        homeViewModel.getIncome().observe(getViewLifecycleOwner(), income ->
                binding.tvIncome.setText("+¥" + String.format("%.2f", income)));

        homeViewModel.getExpense().observe(getViewLifecycleOwner(), expense ->
                binding.tvExpense.setText("-¥" + String.format("%.2f", expense)));

        // FAB 添加按钮点击事件
        binding.fabAdd.setOnClickListener(v ->
                com.google.android.material.snackbar.Snackbar.make(root, "记账功能开发中...", 2000).show());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
