package com.grow.shapeshifters.ui.manage_clients;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManageClientsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ManageClientsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is manage clients fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}