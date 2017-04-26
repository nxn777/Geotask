package com.example.nnv.geotask.presentation.presenter;

import android.content.Context;
import android.location.Address;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.common.LocationAddressesLoader;
import com.example.nnv.geotask.presentation.view.LocationTitleView;

import java.util.List;

/**
 * Created by nnv on 25.04.17.
 */
@InjectViewState
public class LocationTitlePresenter extends MvpPresenter<LocationTitleView>
        implements LocationAddressesLoader.LoaderDelegate{

    private LocationAddressesLoader mLoader;
    private Context mCtx;

    public LocationTitlePresenter(Context context) {
        this.mCtx = context.getApplicationContext();
    }

    public void loadLocations(String location) {
        if (mLoader != null) {
            mLoader.cancel(true);
        }
        mLoader = new LocationAddressesLoader(mCtx, this);
        mLoader.execute(location);
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
