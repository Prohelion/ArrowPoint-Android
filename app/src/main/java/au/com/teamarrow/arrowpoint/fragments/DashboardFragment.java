package au.com.teamarrow.arrowpoint.fragments;

import com.example.arrowpoint.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.com.teamarrow.arrowpoint.utils.TextHelper;
import au.com.teamarrow.canbus.model.CarData;

public class DashboardFragment extends UpdateablePlaceholderFragment {

	private static final String ARG_SECTION_NUMBER = "section_number";


	public DashboardFragment() {
	}


	@Override
	public void Update(View fragmentView, CarData carData) {

		// **************** Dashboard Detail ***************

		TextHelper textHelper = new TextHelper(fragmentView);

		textHelper.setText(R.id.Speed, carData.getLastSpeed());
		textHelper.setText(R.id.txtMotorTemp, carData.getLastMotorTemp(),"#0.0' c'");
		textHelper.setText(R.id.txtMinCellV, (double)carData.getLastMinimumCellV()/1000,"#0.0' v'");
		//textHelper.setText(R.id.txtLastLockedSOC, "Last Locked SOC:" + carData.getLastLockedSOC());
		textHelper.setText(R.id.Power, carData.getLastBusPower());
		textHelper.setProgressBar(R.id.Battery, R.id.BatteryText, (int) carData.getLastSOC());
		textHelper.setProgressBar(R.id.pbSetpoint, R.id.pbSetpointText, carData.getLastMotorPowerSetpoint());

        if (carData.isIdle()) textHelper.setText(R.id.lblState,"Idle");
        if (carData.isReverse()) textHelper.setText(R.id.lblState,"Reverse");
        if (carData.isNeutral()) textHelper.setText(R.id.lblState,"Neutral");
        if (carData.isDrive()) textHelper.setText(R.id.lblState,"Drive");
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
		View rootView = inflater.inflate(R.layout.fragment_dashboard,
				container, false);
		return rootView;
	}


}
