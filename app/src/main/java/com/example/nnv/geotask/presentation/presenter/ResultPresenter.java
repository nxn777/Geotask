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
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by nnv on 25.04.17.
 * Moxy presenter for ResultView - GoogleMaps and Status result screen
 */
@InjectViewState
public class ResultPresenter extends MvpPresenter<ResultView> implements OnMapReadyCallback{
    private final DirectionsService mDirService;
    private final Context mCtx;
    private GoogleMap mGoogleMap;
    private Address mFromAddr, mToAddr;
    private volatile Location myLocation; //can be probably accessed from different threads
    private volatile List<LatLng> mDecodedPath; //can be probably accessed from different threads

    public ResultPresenter(Context context) {
        this.mCtx = context.getApplicationContext();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Globals.BASE_DIRECTIONS_URL)
                .build();
        mDirService = retrofit.create(DirectionsService.class);
        myLocation = null;
        mDecodedPath = new LinkedList<>();
        updateMyLocation();
    }

    /**
     * Updates phone's location single time
     * Uses Network and GPS providers
     * GPS provider results have higher priority.
     * If route path already exists, the method updates google map bounds and location marker
     * with new ones.
     * Does nothing if no geolocation permissions are given
     */
    private void updateMyLocation() {
        try {
            LocationManager locationManager = (LocationManager) mCtx.getSystemService(LOCATION_SERVICE);
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (myLocation == null) { //GPS has not yet set this iVar
                        myLocation = location;
                        if (!mDecodedPath.isEmpty()) { //No need to show location before we find a route
                            getViewState().updateMapWithMylocation(mGoogleMap, mDecodedPath, myLocation);
                        }
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
                    if (!mDecodedPath.isEmpty()) { //No need to show location before we find a route
                        getViewState().updateMapWithMylocation(mGoogleMap, mDecodedPath, myLocation);
                    }
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

    /**
     * Micro parser
     * Searches for routes[0].overview_polyline.points field
     * in json response of google directions api
     * @param body json response
     * @return String containing value of "points" field. Empty if not found
     */
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
        } catch (IllegalStateException e) {
            return "";
        } catch (JsonParseException e2) {
            return "";
        }
    }

    /**
     * Micro parser
     * Checks, if json contains field "status" and it equals "OK" - it means that route exists
     * Returns true if it so, false otherwise.
     * @param body Json response of google directions api
     * @return boolean
     */
    private boolean isRouteFound(String body) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(body).getAsJsonObject();
            return obj.get("status").getAsString().equals("OK");
        } catch (NullPointerException e) {
            return false;
        } catch (IllegalStateException e) {
            return false;
        } catch (JsonParseException e2) {
            return false;
        }
    }

    /**
     * Searches route between addresses - performs async call to google directions api.
     * Updates UI accordingly
     * @param fromAddr starting Address
     * @param toAddr target Address
     */
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
                    String path = getPath(body);
                    if (path.equals("")) {
                        getViewState().showError(mCtx.getString(R.string.result_decode_error));
                        getViewState().toggleUI(Globals.ResultState.NotFound);
                        return;
                    }
                    mDecodedPath = PolyUtil.decode(path);
                    if (!isRouteFound(body)) {
                        getViewState().toggleUI(Globals.ResultState.NotFound);
                        return;
                    }
                    if (mGoogleMap != null ) {
                        getViewState().toggleUI(Globals.ResultState.Found);
                        getViewState().showRoute(mGoogleMap, mDecodedPath, myLocation);
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
