package au.com.teamarrow.arrowpoint.fragments;

import com.example.arrowpoint.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import au.com.teamarrow.canbus.model.CarData;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class GraphFragment extends UpdateablePlaceholderFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int GRAPH_WIDTH = 50;

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

            LineChart chart = (LineChart) fragmentView.findViewById(R.id.chart);

            LineData lineData = chart.getData();
            if (lineData == null) lineData = new LineData();

            ArrayList<LineDataSet> dataSets = (ArrayList)lineData.getDataSets();
            if ( dataSets == null ) {
                dataSets = new ArrayList<LineDataSet>();
            }

            LineDataSet speedData = null;
            if (dataSets.size() > 0) speedData = dataSets.get(0);
            if (speedData == null) {
                speedData = new LineDataSet(null,"Speeds");
                lineData.addDataSet(speedData);
            }

            speedData.addEntry(new Entry((float)graphLastXValue,(int)graphLastXValue));

            chart.setData(lineData);
            chart.invalidate(); // refresh

            /*

            graphLastXValue += 1d;

            CheckBox velocity = (CheckBox) fragmentView.findViewById(R.id.chkSpeed);
            CheckBox busPower = (CheckBox) fragmentView.findViewById(R.id.chkBusPower);
            CheckBox arrayPower = (CheckBox) fragmentView.findViewById(R.id.chkArrayPower);
            CheckBox motorPower = (CheckBox) fragmentView.findViewById(R.id.chkMotorPower);
            CheckBox motorTemp = (CheckBox) fragmentView.findViewById(R.id.chkMotorTemp);

            try {
                if (velocity.isChecked()) {
                    seriesSpeed.appendData(new DataPoint(graphLastXValue, carData.getLastSpeed()), true, GRAPH_WIDTH * 2 );
                }

                if (busPower.isChecked()) {
                    seriesBusPower.appendData(new DataPoint(graphLastXValue, carData.getLastBusPower()), true, GRAPH_WIDTH * 2);
                }

                if (arrayPower.isChecked()) {
                    seriesArrayPower.appendData(new DataPoint(graphLastXValue, carData.getLastArrayTotalPower()), true, GRAPH_WIDTH * 2);
                }

                if (motorPower.isChecked()) {
                    seriesMotorPower.appendData(new DataPoint(graphLastXValue, carData.getLastMotorPowerSetpoint()), true, GRAPH_WIDTH  * 2);
                }

                if (motorTemp.isChecked()) {
                    seriesMotorTemp.appendData(new DataPoint(graphLastXValue, carData.getLastMotorTemp()), true, GRAPH_WIDTH - 10);
                }
            } catch (Exception ex) {
            } */
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_graph,
                container, false);



        //LineChart chart = (LineChart) findViewById(R.id.chart);

        /*

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        // Bus Power
        seriesBusPower = new LineGraphSeries<DataPoint>();
        graph.addSeries(seriesBusPower);
        seriesBusPower.setColor(Color.BLUE);

        // Speed
        seriesSpeed = new LineGraphSeries<DataPoint>();
        //graph.getSecondScale().addSeries(seriesSpeed);
        graph.addSeries(seriesSpeed);
        seriesSpeed.setColor(Color.RED);

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
        //graph.getSecondScale().addSeries(seriesMotorTemp);
        graph.addSeries(seriesMotorTemp);
        seriesMotorTemp.setColor(Color.GREEN);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(10);
        graph.getViewport().setMaxX(GRAPH_WIDTH);
        graph.getSecondScale().setMinY(0);
        graph.getSecondScale().setMaxY(120);
        graph.getViewport().setScrollable(true);

        graph.setTitle("Graphed Information");
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        graphLastXValue = 1d;
*/
        return rootView;
    }


}


