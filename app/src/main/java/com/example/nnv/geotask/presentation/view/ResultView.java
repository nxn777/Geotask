package com.example.nnv.geotask.presentation.view;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by nnv on 25.04.17.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface ResultView extends MvpView{
    void toggleSearching(boolean isSearching);
    void showError(String error);
    void showRoute();
}
