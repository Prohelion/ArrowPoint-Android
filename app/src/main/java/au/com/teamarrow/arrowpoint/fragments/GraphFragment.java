package au.com.teamarrow.arrowpoint.fragments;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.arrowpoint.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import au.com.teamarrow.arrowpoint.CustomOnItemSelectedListener;
import au.com.teamarrow.arrowpoint.utils.GraphHandler;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Arrays;

import au.com.teamarrow.canbus.model.CarData;



/**
 * A placeholder fragment containing a simple view.
 */
public class GraphFragment extends UpdateablePlaceholderFragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static Spinner primary_Spinner;
    private static Spinner secondary_Spinner;

    private static GraphHandler graph_handler_1;
    private static GraphHandler graph_handler_2;

    private static XYPlot graph1;
    private static XYPlot graph2;


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
    public void Update(View fragmentView, CarData carData) {

        //Update Graphs
        graph_handler_1.addData(graph1, carData, primary_Spinner.getSelectedItemPosition());
        graph_handler_2.addData(graph2, carData, secondary_Spinner.getSelectedItemPosition());

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph,
                container, false);

        // New instances of GraphHandler for each graph
        graph_handler_1 = new GraphHandler();
        graph_handler_2 = new GraphHandler();

        //Setup Spinners
        secondary_Spinner = (Spinner) rootView.findViewById(R.id.secondarySpinner);
        primary_Spinner = (Spinner) rootView.findViewById(R.id.primarySpinner);

        //Setup Graphs
        graph1 = (XYPlot) rootView.findViewById(R.id.graph1);
        graph2 = (XYPlot) rootView.findViewById(R.id.graph2);
        graph_handler_1.setupGraph(graph1, "Primary Graph");
        graph_handler_2.setupGraph(graph2, "Secondary Graph");

        super.onCreate(savedInstanceState);

        return rootView;
    }

}

