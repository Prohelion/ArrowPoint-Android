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
        mSeries = new LineGraphSeries<DataPoint>();
        graph.addSeries(mSeries);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(600);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(20);
        graph.setTitle("Power(kw)");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Power(kw)");
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);


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


