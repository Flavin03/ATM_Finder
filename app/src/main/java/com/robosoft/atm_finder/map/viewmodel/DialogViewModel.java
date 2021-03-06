package com.robosoft.atm_finder.map.viewmodel;

import android.app.Dialog;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.View;

import java.util.Observable;

public class DialogViewModel extends ViewModel {

    // private ObservableField<String> filter = new ObservableField<>();;
    private MutableLiveData<String> filter = new MutableLiveData<String>();

    private static final String TAG = "DialogViewModel";

    private Context context;

    private Dialog dialog;

    public DialogViewModel(Dialog dialog, Context context) {
        Log.d(TAG, "Parameterized constructor called");
        this.context = context;
        this.dialog = dialog;
    }

    public DialogViewModel() {
    }

    public void onBankClick(View v) {
        Log.d(TAG, "onBankClick");
        getFilter().postValue("Bank");
        filter.postValue("Bank");
        Log.d(TAG, "getFilter onBankClick : " + getFilter().getValue());
        dialog.dismiss();
    }

    public void onAtmClick(View v) {
        Log.d(TAG, "onAtmClick");
        getFilter().postValue("Atm");
        filter.postValue("Atm");
        Log.d(TAG, "getFilter onAtmClick : " + getFilter().getValue());
        dialog.dismiss();
    }

    public void onCancelClick(View v) {
        Log.d(TAG, "onCancelClick");
        dialog.dismiss();
    }

    public MutableLiveData<String> getFilter() {
        if (filter == null) {
            filter = new MutableLiveData<>();
        }
        return filter;
    }

}
