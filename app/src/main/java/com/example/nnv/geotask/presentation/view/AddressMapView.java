package com.example.nnv.geotask.presentation.view;

import android.location.Address;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by nnv on 25.04.17.
 * Moxy View describing map with markers on address search screen
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface AddressMapView extends MvpView {
    /**
     * Shows marker on google map with address location
     * @param address Address for marker
     * @param googleMap Map to be shown on
     */
    void showAddressOnMap(Address address, GoogleMap googleMap);

    /**
     * Clears google map: removes everything(markers, polylines, etc)
     * @param googleMap google map to be cleared
     */
    void clearMap(GoogleMap googleMap);

    /**
     * Shows error
     * @param errorId int errorId from strings resource
     */
    void showError(int errorId);
}
