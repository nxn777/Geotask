package com.example.nnv.geotask.common.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by nnv on 28.04.17.
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
