package com.grow.shapeshifters.ui.add_clients;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddClientsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddClientsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add clients fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}