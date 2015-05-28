package au.com.teamarrow.arrowpoint.fragments;

import com.example.arrowpoint.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import au.com.teamarrow.arrowpoint.utils.TextHelper;
import au.com.teamarrow.canbus.model.CarData;

public class DashboardFragment extends UpdateablePlaceholderFragment {

	private static final String ARG_SECTION_NUMBER = "section_number";


	public DashboardFragment() {
	}

    private void handleDriverView(View view, CarData carData) {
        NumberFormat formatterWithDecimal = new DecimalFormat("#0.00");

        /**
         * Text view Declarations
         */
        // ----- Quick Stats -----
        TextView speedTxt = (TextView) view.findViewById(R.id.currentSpeedValue);
        TextView powerTxt = (TextView) view.findViewById(R.id.netPowerPositionValue);
        ProgressBar batteryBar = (ProgressBar) view.findViewById(R.id.batteryPercentageBar);
        TextView batteryTxt = (TextView) view.findViewById(R.id.batteryValue);
        TextView currentSetPointTxt = (TextView) view.findViewById(R.id.currentSetPointValue);
        TextView currentDriveModeTxt = (TextView) view.findViewById(R.id.currentDriveModeValue);
        TextView powerDrawTxt = (TextView) view.findViewById(R.id.powerDrawValue);
        //Button speedTargetBtn =(Button)findViewById(R.id.btnTargetSpeed);

        // ------ Solar Power -----
        TextView minCellVTxt = (TextView) view.findViewById(R.id.lowestCellVoltageValue);
        TextView totalPowerFromArrayTxt = (TextView) view.findViewById(R.id.totalPowerValue);
        TextView lowestCellVoltageTxt = (TextView) view.findViewById(R.id.lowestCellVoltageValue);
        TextView array1PowerTxt = (TextView) view.findViewById(R.id.array1PowerValue);
        TextView array2PowerTxt = (TextView) view.findViewById(R.id.array2PowerValue);
        TextView array3PowerTxt = (TextView) view.findViewById(R.id.array3PowerValue);

        // ----- Temperature -----
        TextView motorTempTxt = (TextView) view.findViewById(R.id.motorTempValue);
        TextView batteryHighestTempTxt = (TextView) view.findViewById(R.id.batteryHighestTempValue);
        TextView arrayTempTxt = (TextView) view.findViewById(R.id.arrayTempValue);

        /**
         * Setting Values
         */
        if (speedTxt != null) {

            // Quick Stats
            currentSetPointTxt.setText(Integer.toString(carData.getLastMotorPowerSetpoint()));
            speedTxt.setText(Integer.toString(carData.getLastSpeed()));
            powerTxt.setText(formatterWithDecimal.format(carData.getLastBusPower()));
            batteryBar.setProgress((int) carData.getLastSOC());
            batteryTxt.setText(String.valueOf(carData.getLastSOC()));
            currentDriveModeTxt.setText(carData.getDriveMode());
            powerDrawTxt.setText(formatterWithDecimal.format(carData.getLastBusPower()));

            // Solar Power
            minCellVTxt.setText(formatterWithDecimal.format((double) carData.getLastMinimumCellV() / 1000));
            totalPowerFromArrayTxt.setText(formatterWithDecimal.format((double)carData.getLastArrayTotalPower()));
            lowestCellVoltageTxt.setText(formatterWithDecimal.format((double) (carData.getLastMinimumCellV()/ 1000)));
            array1PowerTxt.setText(formatterWithDecimal.format((carData.getLastArray1Power())));
            array2PowerTxt.setText(formatterWithDecimal.format((carData.getLastArray2Power())));
            array3PowerTxt.setText(formatterWithDecimal.format((carData.getLastArray3Power())));

            // Temperature
            motorTempTxt.setText(formatterWithDecimal.format(carData.getLastMotorTemp()));
            batteryHighestTempTxt.setText(formatterWithDecimal.format((carData.getLastMaxCellTemp() / 10))); // clarify
            arrayTempTxt.setText(formatterWithDecimal.format((carData.getLastControllerTemp())));

        }
    }
	@Override
	public void Update(View fragmentView, CarData carData) {

        handleDriverView(fragmentView, carData);
		// **************** Dashboard Detail ***************

		TextHelper textHelper = new TextHelper(fragmentView);

		textHelper.setText(R.id.Speed, carData.getLastSpeed());
		textHelper.setText(R.id.txtMotorTemp, carData.getLastMotorTemp(),"#0.0' c'");
		textHelper.setText(R.id.txtMinCellV, (double)carData.getLastMinimumCellV()/1000,"#0.0' v'");
		textHelper.setText(R.id.txtLastLockedSOC, carData.getLastLockedSOC());
		textHelper.setText(R.id.Power, carData.getLastBusPower());
		textHelper.setProgressBar(R.id.Battery, R.id.BatteryText, (int) carData.getLastSOC());
		textHelper.setProgressBar(R.id.pbSetpoint, R.id.pbSetpointText, carData.getLastMotorPowerSetpoint());

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