package au.com.teamarrow.arrowpoint.fragments;

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
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private LineGraphSeries<DataPoint> mSeries;
    private double graphLastXValue = 5d;

    private Spinner primary_Spinner;
    private Spinner secondary_Spinner;

    public static final int GRAPH_1 = 0;
    public static final int GRAPH_2 = 1;

    public GraphHandler graph_handler_1 = new GraphHandler();


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

        GraphView graph = (GraphView)fragmentView.findViewById(R.id.graph);

        if (graph!=null) {

            //All checks on graph happen her
            primary_Spinner = (Spinner) fragmentView.findViewById(R.id.primarySpinner);
            secondary_Spinner = (Spinner) fragmentView.findViewById(R.id.secondarySpinner);

            graph_handler_1.addData(graph, carData, graphLastXValue, primary_Spinner.getSelectedItemPosition(),true);
            graph_handler_1.addData(graph, carData, graphLastXValue, secondary_Spinner.getSelectedItemPosition(), false);

            graphLastXValue += 1d;
            if (graphLastXValue == 120d) {
                graphLastXValue = 0;
                graph_handler_1.resetAllGraph(graph);
            }

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph,
                container, false);



        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        // Primary Scale
        graph.addSeries(new LineGraphSeries<DataPoint>());

        // Secondary Scale
        graph.getSecondScale().addSeries(new LineGraphSeries<DataPoint>());

        // Secondary Scale
        graph.getSecondScale().addSeries(new LineGraphSeries<DataPoint>());


        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(120);

        //graph.getViewport().setScrollable(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getSecondScale().setMaxY(100);
        graph.getSecondScale().setMinY(0);


        graph.getGridLabelRenderer().setVerticalAxisTitle("");
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("1 Minute Scale");


        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);

        // Doing this to prompt it to render
        //mSeries = (LineGraphSeries) graph.getSeries().get(GraphFragment.GRAPH_1);
        //mSeries.appendData(new DataPoint(0, 0), false, 60);



        // Modify Primary Spinner to a dialog box display
        setSpinnerDialogBox(rootView);

        //Add listeners to each spinner
        addListenerOnSpinnerItemSelection(rootView);

        return rootView;



    }


    // public void addItemsOnSpinner(){
    //    Spinner primary_Spinner = (Spinner) findViewById(R.id.primarySpinner);
    //    List<String> list = new ArrayList<String>();
    //    list.add("Speed");
    //    list.add("Bus Power");
    //    list.add("Array Power");
///
    //    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    //             android.R.layout.simple_spinner_item,list);
    //     dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //      primary_Spinner.setAdapter(dataAdapter);
    // }



    public void addListenerOnSpinnerItemSelection(View view){
        //Primary data display option menu

        Spinner primary_Spinner = (Spinner) view.findViewById(R.id.primarySpinner);
        primary_Spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        //Secondary data display option menu
        Spinner secondary_Spinner = (Spinner) view.findViewById(R.id.secondarySpinner);
        secondary_Spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

/*
    public void addListenerOnButton(View view) {
        Spinner primary_Spinner = (Spinner) view.findViewById(R.id.primarySpinner);
        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view){
                Spinner primary_Spinner = (Spinner) view.findViewById(R.id.primarySpinner);
                Toast.makeText(activity,
                        "OnClickListener : " +
                                "\nPrimary Spinner : " + String.valueOf(primary_Spinner.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
*/

    public void setSpinnerDialogBox(View view){
        //Set primary dialog box


        //Set secondary dialog box
        Spinner secondary_Spinner = (Spinner) view.findViewById(R.id.secondarySpinner);
        ArrayAdapter<String> secondary_dataAdapter = (ArrayAdapter<String>) secondary_Spinner.getAdapter();
        secondary_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondary_Spinner.setAdapter(secondary_dataAdapter);
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


