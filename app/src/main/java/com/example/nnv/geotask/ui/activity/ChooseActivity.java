package com.example.nnv.geotask.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.example.nnv.geotask.common.Globals;
import com.example.nnv.geotask.R;
import com.example.nnv.geotask.ui.fragment.MapFragment;

import java.util.HashMap;

public class ChooseActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MapFragmentPagerAdapter mMapFragmentPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapFragmentPagerAdapter = new MapFragmentPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mMapFragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!startResultActivity()) {
                    Snackbar.make(view, getString(R.string.no_selection), Snackbar.LENGTH_LONG)
                            .show();
                }
            }
        });
        ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        0);


    }



    private boolean startResultActivity() {
        Address fromAddr = ((MapFragmentPagerAdapter)mViewPager.getAdapter())
                .getFragments().get(0).selectedAddress();
        Address toAddr = ((MapFragmentPagerAdapter)mViewPager.getAdapter())
                .getFragments().get(1).selectedAddress();
        if (fromAddr != null && toAddr != null) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(Globals.FROM_KEY, fromAddr);
            intent.putExtra(Globals.TO_KEY, toAddr);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    private class MapFragmentPagerAdapter extends FragmentPagerAdapter {
        private SparseArrayCompat<MapFragment> fragments = new SparseArrayCompat<>();

        MapFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public SparseArrayCompat<MapFragment> getFragments() {
            return this.fragments;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return new MapFragment();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MapFragment fragment = (MapFragment) super.instantiateItem(container, position);
            this.fragments.put(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.from);
                case 1:
                    return getString(R.string.to);
            }
            return null;
        }
    }
}
