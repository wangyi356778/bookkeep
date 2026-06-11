package com.example.jizhangben.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Double> balance = new MutableLiveData<>(0.0);
    private MutableLiveData<Double> income = new MutableLiveData<>(0.0);
    private MutableLiveData<Double> expense = new MutableLiveData<>(0.0);

    public LiveData<Double> getBalance() { return balance; }
    public LiveData<Double> getIncome() { return income; }
    public LiveData<Double> getExpense() { return expense; }

    public void setSummary(double totalIncome, double totalExpense) {
        income.setValue(totalIncome);
        expense.setValue(totalExpense);
        balance.setValue(totalIncome - totalExpense);
    }
}
