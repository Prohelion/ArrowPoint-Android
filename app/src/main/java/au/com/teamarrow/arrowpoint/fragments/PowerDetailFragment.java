package au.com.teamarrow.arrowpoint.fragments;

import com.example.arrowpoint.R;


import java.text.DecimalFormat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;

import au.com.teamarrow.canbus.model.CarData;

public class PowerDetailFragment extends UpdateableFragment {

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

		NumberFormat formatterWithDecimal = new DecimalFormat("#0.00");

		// Solar Detail Data
		TextView array1Volts = (TextView)fragmentView.findViewById(R.id.txtArray1Voltage);
		TextView array2Volts = (TextView)fragmentView.findViewById(R.id.txtArray2Voltage);
		TextView array3Volts = (TextView)fragmentView.findViewById(R.id.txtArray3Voltage);
		TextView array1Amps = (TextView)fragmentView.findViewById(R.id.txtArray1Current);
		TextView array2Amps = (TextView)fragmentView.findViewById(R.id.txtArray2Current);
		TextView array3Amps = (TextView)fragmentView.findViewById(R.id.txtArray3Current);
		TextView array1Power = (TextView)fragmentView.findViewById(R.id.txtArray1Power);
		TextView array2Power = (TextView)fragmentView.findViewById(R.id.txtArray2Power);
		TextView array3Power = (TextView)fragmentView.findViewById(R.id.txtArray3Power);
		TextView totalArrayPower = (TextView)fragmentView.findViewById(R.id.txtTotalArrayPower);
		TextView controllerVolts = (TextView)fragmentView.findViewById(R.id.txtControllerVoltage);
		TextView controllerAmps = (TextView)fragmentView.findViewById(R.id.txtControllerCurrent);
		TextView controllerPower = (TextView)fragmentView.findViewById(R.id.txtControllerPower);
		TextView batteryVolts = (TextView)fragmentView.findViewById(R.id.txtBatteryVoltage);
		TextView batteryAmps = (TextView)fragmentView.findViewById(R.id.txtBatteryCurrent);
		TextView batteryPower = (TextView)fragmentView.findViewById(R.id.txtBatteryPower);

		if ( array1Volts != null ) {
			// Setup solar detail
			array1Volts.setText(formatterWithDecimal.format(carData.getLastArray1Volts()));
			array2Volts.setText(formatterWithDecimal.format(carData.getLastArray2Volts()));
			array3Volts.setText(formatterWithDecimal.format(carData.getLastArray3Volts()));
			array1Amps.setText(formatterWithDecimal.format(carData.getLastArray1Amps()));
			array2Amps.setText(formatterWithDecimal.format(carData.getLastArray2Amps()));
			array3Amps.setText(formatterWithDecimal.format(carData.getLastArray3Amps()));
			array1Power.setText(formatterWithDecimal.format(carData.getLastArray1Power()));
			array2Power.setText(formatterWithDecimal.format(carData.getLastArray2Power()));
			array3Power.setText(formatterWithDecimal.format(carData.getLastArray3Power()));
			totalArrayPower.setText(formatterWithDecimal.format(carData.getLastArrayTotalPower()));

			// Setup controller detail
			controllerVolts.setText(formatterWithDecimal.format(carData.getLastBusVolts()));
			controllerAmps.setText(formatterWithDecimal.format(carData.getLastBusAmps()));
			controllerPower.setText(formatterWithDecimal.format(carData.getLastBusPower()*-1000));

			// Setup battery detail
			batteryVolts.setText(formatterWithDecimal.format(carData.getLastBatteryVolts()));
			batteryAmps.setText(formatterWithDecimal.format(carData.getLastBatteryAmps()));
			batteryPower.setText(formatterWithDecimal.format(carData.getLastArrayTotalPower()-(carData.getLastBusPower()*1000)));

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_power_detail,
				container, false);
		return rootView;
	}

	
}

