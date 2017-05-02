package com.example.nnv.geotask.ui.fragment;

import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.common.utils.LocationAdapter;
import com.example.nnv.geotask.presentation.presenter.ResultPresenter;
import com.example.nnv.geotask.presentation.view.ResultView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nnv on 02.05.17.
 */

public class ResultFragment extends MvpAppCompatFragment implements ResultView {
    private ProgressBar mProgressbar;
    private TextView mTvStatus;


    @InjectPresenter
    ResultPresenter mResultPresenter;

    @ProvidePresenter
    public ResultPresenter provideResultPresenter() {
        return new ResultPresenter(getContext());
    }

    /** LifeCycle */

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
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapResultFragment);
        toggleUI(Globals.ResultState.Searching); //TODO: add initializing state
        Bundle params = getArguments();
        mResultPresenter.setAddresses((Address) params.getParcelable(Globals.FROM_KEY),
                (Address) params.getParcelable(Globals.TO_KEY));
        mapFragment.getMapAsync(mResultPresenter);
    }

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

    /** ResultView */

    @Override
    public void showError(String error) {
        Snackbar.make(getActivity().findViewById(R.id.main_content), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showRoute(GoogleMap googleMap, String path) {
        Log.i(Globals.TAG, "showRoute: " + path);
    }


}
