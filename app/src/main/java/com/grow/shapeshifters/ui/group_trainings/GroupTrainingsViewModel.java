package com.grow.shapeshifters.ui.group_trainings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GroupTrainingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public GroupTrainingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Group training - under implementation");
    }

    public LiveData<String> getText() {
        return mText;
    }
}