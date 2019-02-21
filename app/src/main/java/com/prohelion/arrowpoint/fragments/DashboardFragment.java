package com.prohelion.arrowpoint.fragments;

import com.example.arrowpoint.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.prohelion.arrowpoint.utils.ArcProgress;
import com.prohelion.arrowpoint.utils.TextHelper;
import com.prohelion.canbus.model.CarData;

public class DashboardFragment extends UpdateablePlaceholderFragment {

	private static final String ARG_SECTION_NUMBER = "section_number";


	public DashboardFragment() {
	}


	@Override
	public void Update(View fragmentView, CarData carData) {

        // **************** Dashboard Detail ***************

        TextHelper textHelper = new TextHelper(fragmentView);

        // Performing a null check as it seems sometimes the object does not exist
        // may be due to race conditions at startup
        ArcProgress setpointAcr = (ArcProgress) fragmentView.findViewById(R.id.arc_progress);
        if (setpointAcr != null)
            setpointAcr.setProgress(carData.getLastMotorPowerSetpoint(),carData.getLastMaxSOMSetpoint(), carData.isRegen());

        textHelper.setText(R.id.txtSpeed, carData.getLastSpeed());
        textHelper.setText(R.id.txtSpeed2, carData.getLastSpeed());
        textHelper.setText(R.id.txtPower, carData.getLastBusPower());
        textHelper.setText(R.id.txtState, carData.getDriveMode());
        textHelper.setText(R.id.txtAlert, carData.getAlerts());
        textHelper.setText(R.id.txtMotorTemp, carData.getLastMotorTemp());
        textHelper.setText(R.id.txtMaxBatteryTemp, (double) carData.getLastMaxCellTemp() / 10, "#0.00");
        textHelper.setProgressBar(R.id.pbBattery, R.id.txtBattery, (int) carData.getLastSOC());
        textHelper.setText(R.id.txtSetpoint, carData.getLastMotorPowerSetpoint());
        textHelper.setImageVisibility(R.id.imSPCruise, carData.isSetPointCruiseControl());
        textHelper.setImageVisibility(R.id.imSpeedCruise, carData.isSpeedCruiseControl());
        if (carData.getLastMaxSOMSetpoint() < 100 ||carData.isSOMCruiseControl()){
            textHelper.setImageVisibility(R.id.imSOMCruise, true);
        } else {
            textHelper.setImageVisibility(R.id.imSOMCruise, false);
        }
        textHelper.setImageVisibility(R.id.imLeftBlinker, carData.isLeftBlinker());
        textHelper.setImageVisibility(R.id.imRightBlinker, carData.isRightBlinker());
        textHelper.setVisibility(R.id.txtDriverMode, carData.isDriverMode());



        if (carData.isTestLayout()){ //Used for layout testing, see if text sizes and positions are correct
            setpointAcr.setProgress(80,90, true);
            textHelper.setText(R.id.txtSpeed, 89);
            textHelper.setText(R.id.txtSpeed2, 89);
            textHelper.setText(R.id.txtPower, -6.03);
            textHelper.setText(R.id.txtState, "N");
            textHelper.setText(R.id.txtAlert, "Layout Test");
            textHelper.setText(R.id.txtMotorTemp, (double)(25.65));
            textHelper.setText(R.id.txtMaxBatteryTemp, (double)(35.34));
            textHelper.setProgressBar(R.id.pbBattery, R.id.txtBattery, 85);
            textHelper.setText(R.id.txtSetpoint, 95);
            textHelper.setImageVisibility(R.id.imSPCruise, true);
            textHelper.setImageVisibility(R.id.imSpeedCruise, true);
            textHelper.setImageVisibility(R.id.imSOMCruise, true);
            textHelper.setImageVisibility(R.id.imLeftBlinker, true);
            textHelper.setImageVisibility(R.id.imRightBlinker, true);
            textHelper.setVisibility(R.id.txtDriverMode, true);
        }

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
		return inflater.inflate(R.layout.fragment_dashboard_new,
				container, false);
	}


}
