package com.example.nnv.geotask.presentation.view;

import android.location.Address;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.nnv.geotask.common.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nnv on 25.04.17.
 */

@StateStrategyType(AddToEndSingleStrategy.class)
public interface LocationTitleView  extends MvpView {
    void updateLocationList(ArrayList<Address> addressList);
    void showError(String error);
    void toggleControls(Globals.SearchState searchState);
}
