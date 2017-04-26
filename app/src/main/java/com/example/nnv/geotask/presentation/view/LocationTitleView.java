package com.example.nnv.geotask.presentation.view;

import android.location.Address;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

/**
 * Created by nnv on 25.04.17.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface LocationTitleView  extends MvpView {
    void updateLocationList(List<Address> addressList);
    void showError(String error);
}