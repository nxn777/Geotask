package com.example.nnv.geotask.presentation.presenter;

import android.location.Address;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.presentation.view.AddressMapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

/**
 * Created by nnv on 25.04.17.
 */
@InjectViewState
public class MapPresenter extends MvpPresenter<AddressMapView> implements OnMapReadyCallback{
    private GoogleMap mGoogleMap;
    private Address mAddress;

    public void showAddress(Address address) {
        this.mAddress = address;
        if (mGoogleMap != null) {
            getViewState().clearMap(mGoogleMap);
            getViewState().showAddressOnMap(address, mGoogleMap);
        } else {
            getViewState().showError(R.string.map_not_ready);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(Globals.TAG, "onMapReady: ");
        this.mGoogleMap = googleMap;
        if (mAddress != null) {
            showAddress(mAddress);
        }
    }
}
