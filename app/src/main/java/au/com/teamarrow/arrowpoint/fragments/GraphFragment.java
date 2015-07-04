package au.com.teamarrow.arrowpoint.fragments;

import com.example.arrowpoint.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import au.com.teamarrow.canbus.comms.DatagramReceiver;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class GraphFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private LineGraphSeries<DataPoint> mSeries;
    private double graphLastXValue = 5d;

    public static final int SPEED = 0;
    public static final int BUS_POWER = 1;
    public static final int ARRAY_POWER = 2;
    public static final int MOTOR_POWER = 3;
    public static final int MOTOR_TEMP = 4;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph,
                container, false);

        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        // Speed
        graph.getSecondScale().addSeries(new LineGraphSeries<DataPoint>());

        // Bus Power
        graph.addSeries(new LineGraphSeries<DataPoint>());

        // Array Power
        graph.addSeries(new LineGraphSeries<DataPoint>());

        // Motor Power
        graph.addSeries(new LineGraphSeries<DataPoint>());

        // Motor Temp
        graph.getSecondScale().addSeries(new LineGraphSeries<DataPoint>());

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(600);
        graph.getViewport().setScrollable(true);

        graph.setTitle("Power(kw)");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Power(kw)");
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        // Doing this to prompt it to render
        mSeries = (LineGraphSeries) graph.getSeries().get(GraphFragment.SPEED);
        mSeries.appendData(new DataPoint(0, 0), true, 600);

        return rootView;
    }

    public void onResume() {
        super.onResume();
        /* mTimer = new Runnable() {
            @Override
            public void run() {
               // graphLastXValue += 1d;
               // mSeries.appendData(new DataPoint(graphLastXValue, 10+(5*Math.sin(graphLastXValue))), true, 40);
               // mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(mTimer, 1000); */
    }


}


