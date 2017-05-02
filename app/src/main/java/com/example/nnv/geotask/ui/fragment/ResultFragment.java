package com.example.nnv.geotask.ui.fragment;

import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.common.utils.LocationAdapter;
import com.example.nnv.geotask.presentation.presenter.ResultPresenter;
import com.example.nnv.geotask.presentation.view.ResultView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nnv on 02.05.17.
 */

public class ResultFragment extends MvpAppCompatFragment implements ResultView {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /** ResultView */
    @Override
    public void toggleSearching(boolean isSearching) {

    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void showRoute() {

    }


}
