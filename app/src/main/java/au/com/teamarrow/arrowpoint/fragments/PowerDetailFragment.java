package au.com.teamarrow.arrowpoint.fragments;

import com.example.arrowpoint.R;


import java.text.DecimalFormat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;

import au.com.teamarrow.arrowpoint.utils.TextHelper;
import au.com.teamarrow.canbus.model.CarData;

public class PowerDetailFragment extends UpdateablePlaceholderFragment {

	private static final String ARG_SECTION_NUMBER = "section_number";

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

	public PowerDetailFragment() {
	}


	@Override
	public void Update(View fragmentView, CarData carData) {

		// **************** Power Detail ***************

		TextHelper textHelper = new TextHelper(fragmentView);

		// Solar Detail Data
		textHelper.setText(R.id.txtArray1Voltage,carData.getLastArray1Volts());
		textHelper.setText(R.id.txtArray2Voltage,carData.getLastArray2Volts());
		textHelper.setText(R.id.txtArray3Voltage,carData.getLastArray3Volts());
		textHelper.setText(R.id.txtArray1Current,carData.getLastArray1Amps());
		textHelper.setText(R.id.txtArray2Current,carData.getLastArray2Amps());
		textHelper.setText(R.id.txtArray3Current,carData.getLastArray3Amps());
		textHelper.setText(R.id.txtArray1Power,carData.getLastArray1Power());
		textHelper.setText(R.id.txtArray2Power,carData.getLastArray2Power());
		textHelper.setText(R.id.txtArray3Power,carData.getLastArray3Power());
		textHelper.setText(R.id.txtTotalArrayPower,carData.getLastArrayTotalPower());
		textHelper.setText(R.id.txtControllerVoltage,carData.getLastBusVolts());
		textHelper.setText(R.id.txtControllerCurrent,carData.getLastBusAmps());
		textHelper.setText(R.id.txtControllerPower,carData.getLastBusPower()*-1000);
		textHelper.setText(R.id.txtBatteryVoltage,carData.getLastBatteryVolts());
		textHelper.setText(R.id.txtBatteryCurrent,carData.getLastBatteryAmps());
		textHelper.setText(R.id.txtBatteryPower,carData.getLastArrayTotalPower()-(carData.getLastBusPower()*1000));


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_power_detail_new,
				container, false);
		return rootView;
	}


}