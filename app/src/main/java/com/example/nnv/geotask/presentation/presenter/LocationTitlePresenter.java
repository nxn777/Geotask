package com.example.nnv.geotask.presentation.presenter;

import android.content.Context;
import android.location.Address;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.nnv.geotask.common.LocationAddressesLoader;
import com.example.nnv.geotask.presentation.view.LocationTitleView;

import java.util.List;

/**
 * Created by nnv on 25.04.17.
 */
@InjectViewState
public class LocationTitlePresenter extends MvpPresenter<LocationTitleView>
        implements LocationAddressesLoader.LoaderDelegate{

    private LocationAddressesLoader loader;

    public LocationTitlePresenter(Context context) {
        loader = new LocationAddressesLoader(context, this);
    }

    public void loadLocations(String location) {
        loader.execute(location);
    }

    @Override
    public void onLoaderReady(List<Address> resultList) {
        getViewState().updateLocationList(resultList);
    }

    @Override
    public void onError(String errorDescription) {
        getViewState().showError(errorDescription);
    }
}
