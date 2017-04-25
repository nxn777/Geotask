package com.example.nnv.geotask.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by nnv on 25.04.17.
 */

public class LocationAddressesLoader extends AsyncTaskLoader<List<Address>> {
    public static final String QUERY_DATA = "querydata";
    private Bundle mArgs;
    private Geocoder mGeoCoder;

    @Override
    protected void onStartLoading() {
        if (Geocoder.isPresent()) {
            forceLoad();
        } else {
            Log.d(Globals.TAG, "onStartLoading: Geocoder is not presented");
        }
    }

    @Override
    public List<Address> loadInBackground() {
        String queryData = mArgs.getString(QUERY_DATA);
        List<Address> result = Collections.emptyList();
        try {
            result = mGeoCoder.getFromLocationName(queryData, Globals.MAX_RESULTS);
        } catch (IOException e) {
            Log.d(Globals.TAG, "loadInBackground: io error");
        }
        return result;
    }

    public LocationAddressesLoader(Context context, Bundle args) {
        super(context);
        this.mArgs = args;
        this.mGeoCoder = new Geocoder(context, Locale.getDefault());
    }
}
