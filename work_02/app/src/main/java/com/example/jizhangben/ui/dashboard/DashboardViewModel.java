package com.example.jizhangben.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<Double> totalIncome = new MutableLiveData<>(0.0);
    private MutableLiveData<Double> totalExpense = new MutableLiveData<>(0.0);
    private MutableLiveData<Double> balance = new MutableLiveData<>(0.0);

    public LiveData<Double> getTotalIncome() { return totalIncome; }
    public LiveData<Double> getTotalExpense() { return totalExpense; }
    public LiveData<Double> getBalance() { return balance; }

    public void setStatistics(double income, double expense) {
        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        balance.setValue(income - expense);
    }
}
