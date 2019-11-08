package com.collathon.jamukja.ui.pick;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PickViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PickViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("찜목록");
    }

    public LiveData<String> getText() {
        return mText;
    }
}