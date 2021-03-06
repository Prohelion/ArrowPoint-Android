package com.prohelion.arrowpoint.fragments;

import com.example.arrowpoint.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.prohelion.arrowpoint.utils.TextHelper;
import com.prohelion.canbus.model.CarData;

public class SystemsDetailFragment extends UpdateablePlaceholderFragment {

	private static final String ARG_SECTION_NUMBER = "section_number";

	public SystemsDetailFragment() {
	}


	@Override
	public void Update(View fragmentView, CarData carData) {

		// **************** Systems Detail ***************

		TextHelper textHelper = new TextHelper(fragmentView);

        // Set the text helper colour

        textHelper.setText(R.id.txtMotorTempDetail,carData.getLastMotorTemp(),"#0.0' \u2103'");
        textHelper.setText(R.id.txtControllerTemp,carData.getLastControllerTemp(),"#0.0' \u2103'");
        textHelper.setText(R.id.txtMinBattery,(double)carData.getLastMinimumCellV()/1000,"#0.000' V'");
        textHelper.setText(R.id.txtMaxBattery,(double)carData.getLastMaximumCellV()/1000,"#0.000' V'");
        textHelper.setText(R.id.txtMaxBatteryTemp, (double) carData.getLastMaxCellTemp() / 10, "#0.0' \u2103'");
        textHelper.setText(R.id.txt12VoltDetail,(double)carData.getLastTwelveVBusVolts()/1000, "#0.000' V'");

        if (carData.isIdle()) textHelper.setText(R.id.lblState,"Idle");
        if (carData.isReverse()) textHelper.setText(R.id.lblState,"Reverse");
        if (carData.isNeutral()) textHelper.setText(R.id.lblState,"Neutral");
        if (carData.isDrive()) textHelper.setText(R.id.lblState,"Drive");

        textHelper.setColourStatus(R.id.lblState, carData.isIdle() || carData.isReverse() || carData.isNeutral() || carData.isDrive());
        textHelper.setColourStatus(R.id.lblRegen, carData.isRegen());
        textHelper.setColourStatus(R.id.lblBrakes, carData.isBrakes());
        textHelper.setColourStatus(R.id.lblHorn, carData.isHorn());

        textHelper.setText(R.id.txtAlerts,carData.getAlerts());

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
		return inflater.inflate(R.layout.fragment_systems_detail_new,
				container, false);
	}


}

