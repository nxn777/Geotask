package com.example.nnv.geotask.presentation.presenter;

import android.content.Context;
import android.location.Address;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.common.utils.DirectionsService;
import com.example.nnv.geotask.presentation.view.ResultView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by nnv on 25.04.17.
 */
@InjectViewState
public class ResultPresenter extends MvpPresenter<ResultView>{
    private DirectionsService mDirService;
    private Context mCtx;

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



    public void findRoute(Address fromAddr, Address toAddr) {
        HashMap<String, String> params = new HashMap<>();
        params.put("origin", getLatLngAsString(fromAddr));
        params.put("destination", getLatLngAsString(toAddr));
        params.put("key", mCtx.getString(R.string.api_key));
        Call<String> dirCall = mDirService.obtainDirections(params);
        getViewState().toggleSearching(true);
        dirCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                getViewState().toggleSearching(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                getViewState().toggleSearching(false);
                getViewState().showError(t.getLocalizedMessage());
            }
        });
    }

}
