package au.com.teamarrow.arrowpoint.fragments;

import com.example.arrowpoint.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import au.com.teamarrow.arrowpoint.ArrowPoint;
import au.com.teamarrow.canbus.comms.DatagramReceiver;
import au.com.teamarrow.canbus.model.CarData;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A placeholder fragment containing a simple view.
 */
public class GraphFragment extends UpdateableFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private final Handler mHandler = new Handler();
    private Runnable mTimer;

    LineGraphSeries<DataPoint> seriesSpeed;
    LineGraphSeries<DataPoint> seriesBusPower;
    LineGraphSeries<DataPoint> seriesArrayPower;
    LineGraphSeries<DataPoint> seriesMotorPower;
    LineGraphSeries<DataPoint> seriesMotorTemp;

    private double graphLastXValue;

    View rootView;

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

    public GraphFragment() {
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void Update(View fragmentView, CarData carData) {

        if (fragmentView != null){

            graphLastXValue += 1d;

            CheckBox velocity = (CheckBox) fragmentView.findViewById(R.id.chkSpeed);
            CheckBox busPower = (CheckBox) fragmentView.findViewById(R.id.chkBusPower);
            CheckBox arrayPower = (CheckBox) fragmentView.findViewById(R.id.chkArrayPower);
            CheckBox motorPower = (CheckBox) fragmentView.findViewById(R.id.chkMotorPower);
            CheckBox motorTemp = (CheckBox) fragmentView.findViewById(R.id.chkMotorTemp);

            try {
                if (velocity.isChecked()) {
                    seriesSpeed.appendData(new DataPoint(graphLastXValue, carData.getLastSpeed()), true, 49);
                }

                if (busPower.isChecked()) {
                    seriesBusPower.appendData(new DataPoint(graphLastXValue, carData.getLastBusPower()), true, 49);
                }

                if (arrayPower.isChecked()) {
                    seriesArrayPower.appendData(new DataPoint(graphLastXValue, carData.getLastArrayTotalPower()), true, 50);
                }

                if (motorPower.isChecked()) {
                    seriesMotorPower.appendData(new DataPoint(graphLastXValue, carData.getLastMotorPowerSetpoint()), true, 50);
                }

                if (motorTemp.isChecked()) {
                    seriesMotorTemp.appendData(new DataPoint(graphLastXValue, carData.getLastMotorTemp()), true, 50);
                }
            } catch (Exception ex) {
            }
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_graph,
                container, false);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        // Speed
        seriesSpeed = new LineGraphSeries<DataPoint>();
        graph.getSecondScale().addSeries(seriesSpeed);
        seriesSpeed.setColor(Color.RED);

        // Bus Power
        seriesBusPower = new LineGraphSeries<DataPoint>();
        graph.addSeries(seriesBusPower);
        seriesBusPower.setColor(Color.BLUE);

        // Array Power
        seriesArrayPower = new LineGraphSeries<DataPoint>();
        graph.addSeries(seriesArrayPower);
        seriesArrayPower.setColor(Color.BLACK);

        // Motor Power
        seriesMotorPower = new LineGraphSeries<DataPoint>();
        graph.addSeries(seriesMotorPower);
        seriesMotorPower.setColor(Color.YELLOW);

        // Motor Temp
        seriesMotorTemp = new LineGraphSeries<DataPoint>();
        graph.getSecondScale().addSeries(seriesMotorTemp);
        seriesMotorTemp.setColor(Color.GREEN);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(50);
        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(120);
        graph.getViewport().setScrollable(true);

        graph.setTitle("Graphed Information");
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        graphLastXValue = 1d;

        // Doing this to prompt it to render
        seriesBusPower.appendData(new DataPoint(0, 0), true, 50);

        return rootView;
    }


}


