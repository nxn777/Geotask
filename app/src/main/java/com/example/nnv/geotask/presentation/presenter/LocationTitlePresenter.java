package com.example.nnv.geotask.presentation.presenter;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.example.nnv.geotask.common.LocationAddressesLoader;

import java.util.List;

/**
 * Created by nnv on 25.04.17.
 */

public class LocationTitlePresenter implements LoaderManager.LoaderCallbacks<List<Address>> {
    private Context mCtx;

    public LocationTitlePresenter(Context context) {
        this.mCtx = context.getApplicationContext();
    }

    @Override
    public Loader<List<Address>> onCreateLoader(int id, Bundle args) {
        return new LocationAddressesLoader(mCtx, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Address>> loader, List<Address> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Address>> loader) {

    }
}
