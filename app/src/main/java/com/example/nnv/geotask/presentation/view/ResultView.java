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
 * Moxy View describing result view: map and status view of results screen
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface ResultView extends MvpView{
    /**
     * Toggles UI according to state
     * @param state State to be toggled to
     */
    void toggleUI(Globals.ResultState state);

    /**
     * Shows error message
     * @param error String message to be shown
     */
    void showError(String error);

    /**
     * Shows route and phone's location (if any) on map
     * @param googleMap google map to be shown on
     * @param path  List of points for polyline representing route
     * @param myLocation Location of the phone
     */
    void showRoute(GoogleMap googleMap, List<LatLng> path, @Nullable Location myLocation);

    /**
     * Updates google map with phone's location
     * @param googleMap google map to be updated
     * @param path existing path on google maps for adjusting zoom
     * @param myLocation Location of the phone
     */
    void updateMapWithMylocation(GoogleMap googleMap, List<LatLng> path, @NonNull Location myLocation);
}
