package com.grow.shapeshifters.ui.personal_trainings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PersonalTrainingsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public PersonalTrainingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Personal trainings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}