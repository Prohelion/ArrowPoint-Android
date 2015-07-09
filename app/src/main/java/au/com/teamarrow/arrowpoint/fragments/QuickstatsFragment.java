package au.com.teamarrow.arrowpoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arrowpoint.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import au.com.teamarrow.arrowpoint.utils.TextHelper;
import au.com.teamarrow.canbus.model.CarData;

/**
 * Created by CTuesley on 2/07/2015.
 */


public class QuickstatsFragment extends UpdateablePlaceholderFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";


    public QuickstatsFragment() {
    }

    @Override
    public void Update(View fragmentView, CarData carData) {

        // **************** Dashboard Detail ***************

        TextHelper textHelper = new TextHelper(fragmentView);

        // Quick Stats
        if (carData.isIdle()) textHelper.setText(R.id.currentDriveModeValue,"Idle");
        if (carData.isReverse()) textHelper.setText(R.id.currentDriveModeValue,"Reverse");
        if (carData.isNeutral()) textHelper.setText(R.id.currentDriveModeValue,"Neutral");
        if (carData.isDrive()) textHelper.setText(R.id.currentDriveModeValue,"Drive");

        textHelper.setText(R.id.currentSpeedValue, carData.getLastSpeed());
    }

    /**
     * Returns a new instance of this fragment for the given section number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quickstats,
                container, false);
        return rootView;
    }


}