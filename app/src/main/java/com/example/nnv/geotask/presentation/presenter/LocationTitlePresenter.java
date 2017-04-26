package com.example.nnv.geotask.presentation.presenter;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.nnv.geotask.common.LocationAddressesLoader;
import com.example.nnv.geotask.presentation.view.LocationTitleView;

import java.util.List;

/**
 * Created by nnv on 25.04.17.
 */
@InjectViewState
public class LocationTitlePresenter extends MvpPresenter<LocationTitleView> {
    //private Context mCtx;
    private int mId;
    private LocationAddressesLoader loader;

    public LocationTitlePresenter(Context context, int id) {
        //this.mCtx = context.getApplicationContext();
        this.mId = id;
        loader = new LocationAddressesLoader();
        loader.
    }


}
