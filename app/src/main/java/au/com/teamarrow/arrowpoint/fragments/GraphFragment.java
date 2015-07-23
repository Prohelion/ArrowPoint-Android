package au.com.teamarrow.arrowpoint.fragments;

import com.androidplot.xy.XYPlot;
import com.example.arrowpoint.R;

import au.com.teamarrow.arrowpoint.utils.GraphHandler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

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
    private static int previous_primary_index;
    private static int previous_secondary_index;

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

        if (previous_primary_index != primary_Spinner.getSelectedItemPosition()
                || previous_secondary_index != secondary_Spinner.getSelectedItemPosition()){
            graph_handler_1.clearGraph();
            graph_handler_2.clearGraph();
        }

        //Update Graphs
        graph_handler_1.addData(graph1, carData, primary_Spinner.getSelectedItemPosition());
        graph_handler_2.addData(graph2, carData, secondary_Spinner.getSelectedItemPosition());

        previous_primary_index = primary_Spinner.getSelectedItemPosition();
        previous_secondary_index = secondary_Spinner.getSelectedItemPosition();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph_new,
                container, false);

        // New instances of GraphHandler for each graph
        graph_handler_1 = new GraphHandler();
        graph_handler_2 = new GraphHandler();

        //Setup Spinners
        secondary_Spinner = (Spinner) rootView.findViewById(R.id.spnSecondary);
        primary_Spinner = (Spinner) rootView.findViewById(R.id.spnPrimary);

        //Setup Graphs
        graph1 = (XYPlot) rootView.findViewById(R.id.graph1);
        graph2 = (XYPlot) rootView.findViewById(R.id.graph2);

        int text_color = getResources().getColor(R.color.text_color);
        int line_color = getResources().getColor(R.color.IntegralRed_color);

        graph_handler_1.setupGraph(graph1, "Primary Graph",text_color ,line_color);
        graph_handler_2.setupGraph(graph2, "Secondary Graph",text_color ,line_color);

        super.onCreate(savedInstanceState);

        return rootView;
    }

}

