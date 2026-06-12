package com.example.jizhangben.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jizhangben.AddRecordActivity;
import com.example.jizhangben.databinding.FragmentHomeBinding;
import com.example.jizhangben.repository.UserRepository;
import com.example.jizhangben.utils.TokenManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private UserRepository userRepository;
    private RecordAdapter recordAdapter;
    private List<Map<String, Object>> recordList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userRepository = new UserRepository();

        // 设置月度标签
        String monthLabel = new java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
                .format(new java.util.Date());
        binding.tvMonthLabel.setText(monthLabel);

        // 设置RecyclerView
        recordAdapter = new RecordAdapter(recordList);
        binding.rvRecords.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRecords.setAdapter(recordAdapter);

        // 长按删除记录
        recordAdapter.setOnRecordLongClick((position, recordId) -> showDeleteDialog(recordId));

        // 加载数据
        loadMonthlySummary();
        loadRecords();

        // FAB 跳转记账页面
        binding.fabAdd.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddRecordActivity.class)));

        return root;
    }

    private void loadMonthlySummary() {
        String phone = TokenManager.getPhone(requireContext());
        if (phone == null) return;

        String currentMonth = new java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
                .format(new java.util.Date());

        userRepository.getMonthlySummary(phone, currentMonth, new UserRepository.DataCallback() {
            @Override
            public void onSuccess(com.example.jizhangben.model.BaseResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    try {
                        @SuppressWarnings("unchecked")
                        LinkedHashMap<String, Object> summary =
                                (LinkedHashMap<String, Object>) response.getData();
                        double income = ((Number) summary.get("totalIncome")).doubleValue();
                        double expense = ((Number) summary.get("totalExpense")).doubleValue();
                        double balance = ((Number) summary.get("balance")).doubleValue();

                        binding.tvBalance.setText(String.format(java.util.Locale.CHINA, "¥ %.2f", balance));
                        binding.tvIncome.setText(String.format(java.util.Locale.CHINA, "+¥ %.2f", income));
                        binding.tvExpense.setText(String.format(java.util.Locale.CHINA, "-¥ %.2f", expense));
                    } catch (Exception ignored) {}
                }
            }

            @Override
            public void onError(String errorMessage) { /* 静默 */ }
        });
    }

    private void loadRecords() {
        String phone = TokenManager.getPhone(requireContext());
        if (phone == null) return;

        userRepository.getRecords(phone, new UserRepository.DataCallback() {
            @Override
            public void onSuccess(com.example.jizhangben.model.BaseResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    try {
                        @SuppressWarnings("unchecked")
                        List<LinkedHashMap<String, Object>> data =
                                (List<LinkedHashMap<String, Object>>) response.getData();
                        recordList.clear();
                        for (Map<String, Object> item : data) {
                            recordList.add(item);
                        }
                        recordAdapter.notifyDataSetChanged();

                        // 切换空状态显示
                        if (recordList.isEmpty()) {
                            binding.layoutEmpty.setVisibility(View.VISIBLE);
                            binding.rvRecords.setVisibility(View.GONE);
                            binding.tvRecordTitle.setVisibility(View.GONE);
                        } else {
                            binding.layoutEmpty.setVisibility(View.GONE);
                            binding.rvRecords.setVisibility(View.VISIBLE);
                            binding.tvRecordTitle.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ignored) {}
                }
            }

            @Override
            public void onError(String errorMessage) { /* 静默 */ }
        });
    }

    private void showDeleteDialog(Long recordId) {
        new AlertDialog.Builder(requireContext())
                .setTitle("删除记录")
                .setMessage("确定要删除这条记录吗？")
                .setPositiveButton("删除", (dialog, which) -> deleteRecord(recordId))
                .setNegativeButton("取消", null)
                .show();
    }

    private void deleteRecord(Long recordId) {
        String phone = TokenManager.getPhone(requireContext());
        if (phone == null) return;

        userRepository.deleteRecord(recordId, phone, new UserRepository.DataCallback() {
            @Override
            public void onSuccess(com.example.jizhangben.model.BaseResponse response) {
                if (response.isSuccess()) {
                    loadRecords();      // 刷新列表
                    loadMonthlySummary(); // 刷新汇总
                    android.widget.Toast.makeText(requireContext(), "已删除",
                            android.widget.Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMessage) {
                android.widget.Toast.makeText(requireContext(), errorMessage,
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次回到首页刷新数据
        if (userRepository != null) {
            loadRecords();
            loadMonthlySummary();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
