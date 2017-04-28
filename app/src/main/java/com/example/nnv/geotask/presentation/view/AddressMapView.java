package com.example.nnv.geotask.presentation.view;

import android.location.Address;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by nnv on 25.04.17.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface AddressMapView extends MvpView {
    void showAddressOnMap(Address address, GoogleMap googleMap);
    void clearMap(GoogleMap googleMap);
    void showError(int errorId);
}
