package com.example.nnv.geotask.ui.fragment;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.presentation.presenter.ResultPresenter;
import com.example.nnv.geotask.presentation.view.ResultView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by nnv on 02.05.17.
 *
 */

public class ResultFragment extends MvpAppCompatFragment implements ResultView {
    private ProgressBar mProgressbar;
    private TextView mTvStatus;
    private FrameLayout mFrame;

    @InjectPresenter
    ResultPresenter mResultPresenter;

    @ProvidePresenter
    public ResultPresenter provideResultPresenter() {
        return new ResultPresenter(getContext());
    }

    // LifeCycle

    public ResultFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mProgressbar = (ProgressBar) view.findViewById(R.id.resultProgressBar);
        this.mTvStatus = (TextView) view.findViewById(R.id.resultTextView);
        this.mFrame = (FrameLayout) view.findViewById(R.id.resultFrame);
        final LinearLayout llStatus = (LinearLayout) view.findViewById(R.id.resultStatus);
        llStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llStatus.setVisibility(View.GONE);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapResultFragment);
        toggleUI(Globals.ResultState.Searching);
        Bundle params = getArguments();
        mResultPresenter.setAddresses((Address) params.getParcelable(Globals.FROM_KEY),
                (Address) params.getParcelable(Globals.TO_KEY));
        mapFragment.getMapAsync(mResultPresenter);
    }

    // Implementation of ResultView

    @Override
    public void toggleUI(Globals.ResultState state) {
        switch (state) {
            case Searching:
                mProgressbar.setVisibility(View.VISIBLE);
                mTvStatus.setText(getString(R.string.result_searching));
                break;
            case Found:
                mProgressbar.setVisibility(View.GONE);
                mTvStatus.setText(getString(R.string.result_found));
                break;
            case NotFound:
                mProgressbar.setVisibility(View.GONE);
                mTvStatus.setText(getString(R.string.result_notfound));
                break;
        }
    }

    @Override
    public void showError(String error) {
        if (mFrame != null) {
            Snackbar.make(mFrame, error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showRoute(GoogleMap googleMap, List<LatLng> path, Location myLocation) {
        //
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng position : path) {
            boundsBuilder.include(position);
        }
        if (myLocation != null) {
            LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(myLatLng).title(getString(R.string.i)));
            boundsBuilder.include(myLatLng);
        } else {
            Log.i(Globals.TAG, "showRoute: got null myLocation");
        }
        googleMap.addPolyline(new PolylineOptions().addAll(path));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),
                Globals.convertDpToPixel(16, getContext())));
    }

    @Override
    public void updateMapWithMylocation(GoogleMap googleMap, List<LatLng> path, @NonNull Location myLocation) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng position : path) {
            boundsBuilder.include(position);
        }
        LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(myLatLng).title(getString(R.string.i)));
        boundsBuilder.include(myLatLng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(),
                Globals.convertDpToPixel(16, getContext())));
    }
}
