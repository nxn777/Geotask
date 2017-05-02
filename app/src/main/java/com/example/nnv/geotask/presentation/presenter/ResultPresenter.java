package com.example.nnv.geotask.presentation.presenter;

import android.content.Context;
import android.location.Address;

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

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by nnv on 25.04.17.
 */
@InjectViewState
public class ResultPresenter extends MvpPresenter<ResultView> implements OnMapReadyCallback{
    private DirectionsService mDirService;
    private Context mCtx;
    private GoogleMap mGoogleMap;
    private Address mFromAddr, mToAddr;

    public ResultPresenter(Context context) {
        this.mCtx = context.getApplicationContext();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Globals.BASE_DIRECTIONS_URL)
                .build();
        mDirService = retrofit.create(DirectionsService.class);
    }

    private String getLatLngAsString(Address addr) {
        return String.valueOf(addr.getLatitude()) + "," +
                String.valueOf(addr.getLongitude());
    }

    private String getPath(String body) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(body).getAsJsonObject();
        return obj.getAsJsonObject("overview_polyline").get("points").getAsString();
    }

    public void findRoute(Address fromAddr, Address toAddr) {
        HashMap<String, String> params = new HashMap<>();
        params.put("origin", getLatLngAsString(fromAddr));
        params.put("destination", getLatLngAsString(toAddr));
        params.put("key", mCtx.getString(R.string.api_key));
        Call<String> dirCall = mDirService.obtainDirections(params);
        getViewState().toggleUI(Globals.ResultState.Searching);
        dirCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                getViewState().toggleUI(Globals.ResultState.Found); //TODO: maybe not
                if (mGoogleMap != null) {
                    getViewState().showRoute(mGoogleMap, getPath(response.body()));
                } else {
                    getViewState().showError(mCtx.getString(R.string.map_not_ready));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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
