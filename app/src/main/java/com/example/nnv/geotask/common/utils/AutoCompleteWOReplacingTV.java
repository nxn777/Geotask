package com.example.nnv.geotask.common.utils;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by nnv on 28.04.17.
 * This inherited class is for overriding replaceText(CharSequence text)
 * By default it puts the value of selected dropdownlist item to edit field,
 * we dont need that
 */

public class AutoCompleteWOReplacingTV extends android.support.v7.widget.AppCompatAutoCompleteTextView{
    public AutoCompleteWOReplacingTV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void replaceText(CharSequence text) {
        //do nothing
    }
}
