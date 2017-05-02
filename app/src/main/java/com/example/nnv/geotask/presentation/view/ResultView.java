package com.example.nnv.geotask.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.nnv.geotask.common.Globals;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by nnv on 25.04.17.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface ResultView extends MvpView{
    void toggleUI(Globals.ResultState state);
    void showError(String error);
    void showRoute(GoogleMap googleMap, String path);
}
