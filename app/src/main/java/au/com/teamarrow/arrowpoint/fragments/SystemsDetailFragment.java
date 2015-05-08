package au.com.teamarrow.arrowpoint.fragments;

import com.example.arrowpoint.R;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.com.teamarrow.arrowpoint.utils.TextHelper;
import au.com.teamarrow.canbus.model.CarData;

public class SystemsDetailFragment extends UpdateablePlaceholderFragment {

	private static final String ARG_SECTION_NUMBER = "section_number";


	public SystemsDetailFragment() {
	}


	@Override
	public void Update(View fragmentView, CarData carData) {

		// **************** Systems Detail ***************

		TextHelper textHelper = new TextHelper(fragmentView);

		textHelper.setText(R.id.txtMotorTempDetail,carData.getLastMotorTemp(),"#0.0' c'");
		textHelper.setText(R.id.txtControllerTemp,carData.getLastControllerTemp(),"#0.0' c'");
		textHelper.setText(R.id.txtMinBattery,(double)carData.getLastMinimumCellV()/1000,"#0.0' v'");
		textHelper.setText(R.id.txtMaxBattery,(double)carData.getLastMaximumCellV()/1000,"#0.0' v'");
		textHelper.setText(R.id.txtMaxBatteryTemp, (double) carData.getLastMaxCellTemp() / 10, "#0.0' c'");
		textHelper.setText(R.id.txt12VoltDetail,(double)carData.getLastTwelveVBusVolts()/1000, "#0.0 ' v'");

		if (carData.isIdle()) textHelper.setText(R.id.lblState,"Idle");
		if (carData.isReverse()) textHelper.setText(R.id.lblState,"Reverse");
		if (carData.isNeutral()) textHelper.setText(R.id.lblState,"Neutral");
		if (carData.isDrive()) textHelper.setText(R.id.lblState,"Drive");

		textHelper.setColourStatus(R.id.lblState, carData.isIdle() || carData.isReverse() || carData.isNeutral() || carData.isDrive());
		textHelper.setColourStatus(R.id.lblRegen, carData.isRegen());
		textHelper.setColourStatus(R.id.lblBrakes, carData.isBrakes());
		textHelper.setColourStatus(R.id.lblHorn, carData.isHorn());

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
		View rootView = inflater.inflate(R.layout.fragment_systems_detail,
				container, false);
		return rootView;
	}


}

