package com.example.nnv.geotask.ui.activity;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nnv.geotask.R;
import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.ui.fragment.ResultFragment;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent incoming = getIntent();
        Address fromAddr = incoming.getParcelableExtra(Globals.FROM_KEY);
        Address toAddr = incoming.getParcelableExtra(Globals.TO_KEY);
        ResultFragment resultFragment = new ResultFragment();
        Bundle fragmentParams = new Bundle();
        fragmentParams.putParcelable(Globals.FROM_KEY, fromAddr);
        fragmentParams.putParcelable(Globals.TO_KEY, toAddr);
        resultFragment.setArguments(fragmentParams);
        resultFragment.setRetainInstance(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.resultContainer, resultFragment)
                .commit();
    }
}
