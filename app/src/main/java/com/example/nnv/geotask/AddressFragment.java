package com.example.nnv.geotask;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

/**
 * Created by nnv on 24.04.17.
 */

/**
 * A placeholder fragment containing a simple view.
 */
public class AddressFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public AddressFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AddressFragment newInstance(int sectionNumber) {
        AddressFragment fragment = new AddressFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_address, container, false);
        AutoCompleteTextView actvAddress= (AutoCompleteTextView) rootView.findViewById(R.id.actvAddress);
        actvAddress.setHint(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}
