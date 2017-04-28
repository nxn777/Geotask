package com.example.nnv.geotask.presentation.presenter;

import android.location.Address;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.nnv.geotask.presentation.view.AddressMapView;

/**
 * Created by nnv on 25.04.17.
 */
@InjectViewState
public class MapPresenter extends MvpPresenter<AddressMapView>{


    public void showAddress(Address address) {
        getViewState().clearMap();
        getViewState().showAddressOnMap(address);
    }
}
