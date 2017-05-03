package com.example.nnv.geotask.common.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nnv on 25.04.17.
 * AsyncTask for performing Geocoder call(obtaining Addresses by their description)
 */

public class LocationAddressesLoader extends AsyncTask<String, Void, ArrayList<Address>> {
    /**
     * interface for interacting with this loader
     */
    public interface LoaderDelegate {
        void onLoaderReady(ArrayList<Address> resultList);
        void onError(String errorDescription);
    }

    private final Geocoder mGeoCoder;
    private final LoaderDelegate delegate;
    private final Context mCtx;

    public LocationAddressesLoader(Context ctx, LoaderDelegate delegate) {
        super();
        this.delegate = delegate;
        this.mCtx = ctx.getApplicationContext();
        mGeoCoder = new Geocoder(mCtx, Locale.getDefault());
    }

    @Override
    protected void onPreExecute() {
        if (!Geocoder.isPresent()) {
            Log.i(Globals.TAG, "onPreExecute: Geocoder is not presented");
            cancel(true);
            delegate.onError(mCtx.getString(R.string.geocoder_absent));
        }
    }

    @Override
    protected ArrayList<Address> doInBackground(String... params) {
        String queryData = params[0];
        ArrayList<Address> result = new ArrayList<>();
        try {
            List<Address> res = mGeoCoder.getFromLocationName(queryData, Globals.MAX_RESULTS);
            result.addAll(res);
        } catch (IOException e) {
            Log.i(Globals.TAG, "loadInBackground: io error");
            delegate.onError(mCtx.getString(R.string.geocoder_error));
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Address> addressList) {
        delegate.onLoaderReady(addressList);
        super.onPostExecute(addressList);
    }
}
