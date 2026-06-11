package com.example.jizhangben.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotificationsViewModel extends ViewModel {

    private MutableLiveData<String> username = new MutableLiveData<>("未登录");

    public LiveData<String> getUsername() { return username; }

    public void setUsername(String name) {
        username.setValue(name);
    }
}
