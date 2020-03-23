package com.wasteless.ui.search;

import android.graphics.ColorSpace;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;



public class SearchModel {

    private ArrayList<Number> dummyData;


    public void generateDummyData() {
        for (int i = 0;  i < 20; i++ ) {
            dummyData.add(i);
        }
        System.out.println(dummyData);
        Log.d("dummyData", "" + dummyData);
    }

//    generateDummyData();


    private MutableLiveData<String> searchValue;
    private MutableLiveData<String> filter;

    public ArrayList<Number> getDummyData() {
        generateDummyData();
        return this.dummyData;
    }

    public void setFilter(MutableLiveData<String> filter) {
        this.filter = filter;
    }

    public void setSearchValue(MutableLiveData<String> searchValue) {
        this.searchValue = searchValue;
    }

}
