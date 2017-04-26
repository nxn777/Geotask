package com.example.nnv.geotask.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.os.AsyncTaskCompat;
import android.util.Log;

import com.example.nnv.geotask.R;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by nnv on 25.04.17.
 */
/*
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
}*/
public class LocationAddressesLoader extends AsyncTask<String, Void, List<Address>> {

    public interface LoaderDelegate {
        void onLoaderReady(List<Address> resultList);
        void onError(String errorDescription);
    }

    private Geocoder mGeoCoder;
    private LoaderDelegate delegate;
    private Context mCtx;

    public LocationAddressesLoader(Context ctx, LoaderDelegate delegate) {
        super();
        this.delegate = delegate;
        this.mCtx = ctx.getApplicationContext();
        mGeoCoder = new Geocoder(mCtx, Locale.getDefault());
    }

    @Override
    protected void onPreExecute() {
        if (!Geocoder.isPresent()) {
            Log.d(Globals.TAG, "onPreExecute: Geocoder is not presented");
            cancel(true);
            delegate.onError(mCtx.getString(R.string.geocoder_absent));
        }
    }

    @Override
    protected List<Address> doInBackground(String... params) {
        String queryData = params[0];
        List<Address> result = Collections.emptyList();
        try {
            result = mGeoCoder.getFromLocationName(queryData, Globals.MAX_RESULTS);
        } catch (IOException e) {
            Log.d(Globals.TAG, "loadInBackground: io error");
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<Address> addressList) {
        delegate.onLoaderReady(addressList);
        super.onPostExecute(addressList);
    }
}
