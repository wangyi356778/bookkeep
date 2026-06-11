package com.example.jizhangben.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jizhangben.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dashboardViewModel.getTotalIncome().observe(getViewLifecycleOwner(), income ->
                binding.tvStatIncome.setText("+¥" + String.format("%.2f", income)));

        dashboardViewModel.getTotalExpense().observe(getViewLifecycleOwner(), expense ->
                binding.tvStatExpense.setText("-¥" + String.format("%.2f", expense)));

        dashboardViewModel.getBalance().observe(getViewLifecycleOwner(), balance ->
                binding.tvStatBalance.setText("¥ " + String.format("%.2f", balance)));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
