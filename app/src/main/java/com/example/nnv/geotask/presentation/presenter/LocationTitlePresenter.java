package com.example.nnv.geotask.presentation.presenter;

import android.content.Context;
import android.location.Address;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.nnv.geotask.common.utils.LocationAddressesLoader;
import com.example.nnv.geotask.presentation.view.LocationTitleView;

import java.util.ArrayList;

/**
 * Created by nnv on 25.04.17.
 */
@InjectViewState
public class LocationTitlePresenter extends MvpPresenter<LocationTitleView>
        implements LocationAddressesLoader.LoaderDelegate{

    private LocationAddressesLoader mLoader;
    private Context mCtx;
    private ArrayList<Address> addresses;

    public LocationTitlePresenter(Context context) {
        this.mCtx = context.getApplicationContext();
        addresses = new ArrayList<>();
    }

    public void loadLocations(String location) {
        if (mLoader != null) {
            mLoader.cancel(true);
        }
        mLoader = new LocationAddressesLoader(mCtx, this);
        mLoader.execute(location);
    }

    public void getLocationList() {
        getViewState().updateLocationList(addresses);
    }

    public void clearLocationList() {
        addresses = new ArrayList<>();
    }

    @Override
    public void onLoaderReady(ArrayList<Address> resultList) {
        getViewState().updateLocationList(resultList);
        addresses = resultList;
    }

    @Override
    public void onError(String errorDescription) {
        getViewState().showError(errorDescription);
    }
}
