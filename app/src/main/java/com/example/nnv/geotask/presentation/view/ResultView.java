package com.example.nnv.geotask.presentation.view;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.example.nnv.geotask.common.Globals;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by nnv on 25.04.17.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface ResultView extends MvpView{
    void toggleUI(Globals.ResultState state);
    void showError(String error);
    void showRoute(GoogleMap googleMap, List<LatLng> path, @Nullable Location myLocation);
    void updateMapWithMylocation(GoogleMap googleMap, List<LatLng> path, @NonNull Location myLocation);
}
