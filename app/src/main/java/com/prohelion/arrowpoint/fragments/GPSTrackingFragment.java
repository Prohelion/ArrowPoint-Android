package com.prohelion.arrowpoint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.arrowpoint.R;

import com.prohelion.arrowpoint.utils.TextHelper;
import com.prohelion.canbus.model.CarData;

public class GPSTrackingFragment extends UpdateablePlaceholderFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_gps, container, false);
    }

    @Override
    public void onDestroyView() {
        // Unregister since the activity is about to be closed.
        super.onDestroyView();
    }

    @Override
    public void Update(View fragmentView, CarData carData) {
        TextHelper textHelper = new TextHelper(fragmentView);

        String location = new StringBuilder().append("Your location is lat(").append(carData.getLatitude()).append(") - long (").append(carData.getLongitude()).append(")").toString();

        textHelper.setText(R.id.txtGpsData,location);
    }

}