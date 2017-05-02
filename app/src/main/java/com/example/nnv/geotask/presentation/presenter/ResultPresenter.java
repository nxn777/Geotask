package com.example.nnv.geotask.presentation.presenter;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.common.utils.DirectionsService;
import com.example.nnv.geotask.presentation.view.ResultView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by nnv on 25.04.17.
 */
@InjectViewState
public class ResultPresenter extends MvpPresenter<ResultView> implements OnMapReadyCallback{
    private DirectionsService mDirService;
    private Context mCtx;
    private GoogleMap mGoogleMap;
    private Address mFromAddr, mToAddr;
    private volatile Location myLocation;

    public ResultPresenter(Context context) {
        this.mCtx = context.getApplicationContext();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Globals.BASE_DIRECTIONS_URL)
                .build();
        mDirService = retrofit.create(DirectionsService.class);
        myLocation = null;
        updateMyLocation();
    }

    private void updateMyLocation() {
        try {
            LocationManager locationManager = (LocationManager) mCtx.getSystemService(LOCATION_SERVICE);
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (myLocation == null) { //GPS has not yet set this iVar
                        myLocation = location;
                        Log.i(Globals.TAG, "onLocationChanged: NETWORK_PROVIDER " + myLocation.toString());
                    }
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(String provider) {}
                @Override
                public void onProviderDisabled(String provider) {}
            }, null);
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location == null) { return; }
                    myLocation = location; //We always accept GPS location
                    Log.i(Globals.TAG, "onLocationChanged: GPS_PROVIDER " + myLocation.toString());
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
                @Override
                public void onProviderEnabled(String provider) {}
                @Override
                public void onProviderDisabled(String provider) {}
            }, null);
        } catch (SecurityException e) {
            Log.i(Globals.TAG, "ResultPresenter: no permission to show my location");
        } catch (IllegalArgumentException e) {
            Log.i(Globals.TAG, "ResultPresenter: no location providers to show my location");
        }
    }

    private String getLatLngAsString(Address addr) {
        return String.valueOf(addr.getLatitude()) + "," +
                String.valueOf(addr.getLongitude());
    }

    //micro parser
    private String getPath(String body) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(body).getAsJsonObject();
            return obj.getAsJsonArray("routes").get(0).getAsJsonObject()
                    .getAsJsonObject("overview_polyline").get("points").getAsString();
        } catch (NullPointerException e) {
            return "";
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }
    private boolean isRouteFound(String body) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(body).getAsJsonObject();
        return obj.get("status").getAsString().equals("OK");
    }

    private void findRoute(Address fromAddr, Address toAddr) {
        HashMap<String, String> params = new HashMap<>();
        params.put("origin", getLatLngAsString(fromAddr));
        params.put("destination", getLatLngAsString(toAddr));
        params.put("key", mCtx.getString(R.string.api_key));
        Call<ResponseBody> dirCall = mDirService.obtainDirections(params);
        getViewState().toggleUI(Globals.ResultState.Searching);
        dirCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String body = response.body().string();
                    if (!isRouteFound(body)) {
                        getViewState().toggleUI(Globals.ResultState.NotFound);
                        return;
                    }
                    if (mGoogleMap != null ) {
                        String path = getPath(body);
                        if (path.equals("")) {
                            getViewState().showError(mCtx.getString(R.string.result_decode_error));
                            return;
                        }
                        getViewState().toggleUI(Globals.ResultState.Found);
                        getViewState().showRoute(mGoogleMap, path, myLocation);
                    } else {
                        getViewState().showError(mCtx.getString(R.string.map_not_ready));
                    }
                } catch (IOException e) {
                    Log.i(Globals.TAG, "onResponse: io error");
                    getViewState().showError(mCtx.getString(R.string.result_io_error));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                getViewState().toggleUI(Globals.ResultState.NotFound);
                getViewState().showError(t.getLocalizedMessage());
            }
        });
    }

    public void setAddresses(Address fromAddr, Address toAddr) {
        this.mFromAddr = fromAddr;
        this.mToAddr = toAddr;
    }

    /** OnMapReadyCallback*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        findRoute(mFromAddr, mToAddr);
    }
}
//TODO: redesign - make map fullscreen, other controls place above it