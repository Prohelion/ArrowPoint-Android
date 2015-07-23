package au.com.teamarrow.arrowpoint.fragments;

import com.example.arrowpoint.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.com.teamarrow.arrowpoint.utils.ArcProgress;
import au.com.teamarrow.arrowpoint.utils.TextHelper;
import au.com.teamarrow.arrowpoint.utils.SetPointProgress;
import au.com.teamarrow.canbus.model.CarData;

public class DashboardFragment extends UpdateablePlaceholderFragment {

	private static final String ARG_SECTION_NUMBER = "section_number";


	public DashboardFragment() {
	}


	@Override
	public void Update(View fragmentView, CarData carData) {

        // **************** Dashboard Detail ***************

        TextHelper textHelper = new TextHelper(fragmentView);
        ArcProgress setpointAcr = (ArcProgress) fragmentView.findViewById(R.id.arc_progress);
        setpointAcr.setProgress(carData.getLastMotorPowerSetpoint(), carData.isRegen());
        textHelper.setText(R.id.txtSpeed, carData.getLastSpeed());
        textHelper.setText(R.id.txtPower, carData.getLastBusPower());
        textHelper.setText(R.id.txtState, carData.getDriveMode());
        textHelper.setText(R.id.txtAlert, carData.getAlerts());
        textHelper.setText(R.id.txtMotorTemp, carData.getLastMotorTemp());
        textHelper.setText(R.id.txtMaxBatteryTemp, carData.getLastMaxCellTemp() / 10);
        textHelper.setProgressBar(R.id.pbBattery, R.id.txtBattery, (int) carData.getLastSOC());
        textHelper.setText(R.id.txtSetpoint, carData.getLastMotorPowerSetpoint());
        textHelper.setImageVisibility(R.id.imSPCruise, carData.isSetPointCruiseControl());
        textHelper.setImageVisibility(R.id.imSpeedCruise, carData.isSpeedCruiseControl());
        textHelper.setImageVisibility(R.id.imLeftBlinker, carData.isLeftBlinker());
        textHelper.setImageVisibility(R.id.imRightBlinker, carData.isRightBlinker());

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
		View rootView = inflater.inflate(R.layout.fragment_dashboard_new,
				container, false);


		return rootView;
	}


}
