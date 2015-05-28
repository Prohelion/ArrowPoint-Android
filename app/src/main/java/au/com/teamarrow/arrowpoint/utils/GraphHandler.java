package au.com.teamarrow.arrowpoint.utils;

        import android.graphics.Paint;
        import android.nfc.Tag;
        import android.util.Log;
        import au.com.teamarrow.arrowpoint.ArrowPoint;
        import au.com.teamarrow.arrowpoint.CustomOnItemSelectedListener;
        import au.com.teamarrow.arrowpoint.fragments.GraphFragment;
        import au.com.teamarrow.canbus.comms.DatagramReceiver;

        import android.app.Fragment;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.CheckBox;
        import android.widget.Toast;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.app.Activity;

        import com.androidplot.Plot;
        import com.androidplot.xy.SimpleXYSeries;
        import com.androidplot.xy.XYSeries;
        import com.androidplot.xy.*;
        import com.example.arrowpoint.R;

        import au.com.teamarrow.arrowpoint.fragments.GraphFragment;
        import au.com.teamarrow.canbus.comms.DatagramReceiver;
        import au.com.teamarrow.arrowpoint.fragments.PlaceholderFragment;
        import au.com.teamarrow.arrowpoint.ArrowPoint;
        import au.com.teamarrow.canbus.model.CarData;

        import java.util.Arrays;
        import java.util.Calendar;

/**
 * Created by Admin on 3/05/2015.
 */
public class GraphHandler {

    //Spinner Index
    static final int VEHICLE_SPEED = 0;
    static final int BUS_POWER = 1;
    static final int ARRAY_POWER = 2;
    static final int MOTOR_TEMP = 3;
    static final int MAX_CELL_TEMP = 4;
    static final int CONTROLLER_TEMP = 5;
    static final int MIN_CELL_VOLTAGE = 6;
    static final int MAX_CELL_VOLTAGE = 7;

    // Private Variables
    private static int MAX_X_Values = 500;
    private SimpleXYSeries series = null;
    private int currentItem = 0;


    public void setupGraph(XYPlot graph, String label){

        // Add series to graph
        series = new SimpleXYSeries(label);
        series.useImplicitXVals();
        graph.addSeries(series, new LineAndPointFormatter(Color.RED, null, null, null));


        // Layout And Styling
        graph.getLayoutManager().remove(graph.getLegendWidget());
        graph.getLayoutManager().remove(graph.getDomainLabelWidget());
        graph.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        graph.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
        graph.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        graph.getGraphWidget().getDomainLabelPaint().setColor(Color.TRANSPARENT);
        graph.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.BLACK);
        graph.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        graph.getGraphWidget().getDomainGridLinePaint().setColor(Color.BLACK);
        graph.getGraphWidget().getRangeGridLinePaint().setColor(Color.BLACK);
        graph.getGraphWidget().getRangeOriginLabelPaint().setColor(Color.BLACK);
        graph.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);
        graph.getGraphWidget().getRangeLabelPaint().setColor(Color.RED);
        graph.getRangeLabelWidget().getLabelPaint().setColor(Color.BLACK);
        graph.getTitleWidget().getLabelPaint().setColor(Color.BLACK);


        graph.setRangeBoundaries(0, 130, BoundaryMode.FIXED);
        graph.setDomainBoundaries(0, MAX_X_Values, BoundaryMode.FIXED);
        graph.setDomainStepValue(5);
        graph.setRangeStepValue(5);
        graph.setRangeLabel("Initial Title");
        graph.getRangeLabelWidget().pack();
        resetGraph(graph, "Vehicle Speed", "Velocity (Km/h)");

    }


    private void setScale(XYPlot graph, int minY, int maxY) {

        graph.setRangeBoundaries(minY, maxY, BoundaryMode.FIXED);

    }


    public void addData(XYPlot graph, CarData carData,int itemIndex) {

        double lastValue = 0;

        // get rid the oldest sample in history:
        if (series.size() > MAX_X_Values) {
            series.removeFirst();
        }

        // Select which data to add and change the axis scale if data source is changed
        if (itemIndex == VEHICLE_SPEED) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Vehicle Speed", "Velocity (Km/h)");
                setScale(graph, 0, 130);
            }
            lastValue = carData.getLastSpeed();

        } else if (itemIndex == BUS_POWER) {
            if (currentItem != itemIndex) {
               resetGraph(graph, "Bus Power", "Watts (W)");
               setScale(graph, -5, 5);
            }
            lastValue = carData.getLastBusPower();

        } else if (itemIndex == ARRAY_POWER) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Array Power", "Watts (W)");
                setScale(graph, -5, 5);
            }
            lastValue = carData.getLastArrayTotalPower() / 1000;

        } else if (itemIndex == MOTOR_TEMP) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Motor Temp", "Celsius (C)");
                setScale(graph, 0, 100);
            }
            lastValue = carData.getLastMotorTemp() / 10;

        } else if (itemIndex == MAX_CELL_TEMP) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Maximum Cell Temp", "Celsius (C)");
                setScale(graph, 0, 100);
            }
            lastValue = carData.getLastMaxCellTemp() / 10;

        } else if (itemIndex == CONTROLLER_TEMP) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Controller Temp", "Celsius (C)");
                setScale(graph, 0, 100);
            }
            lastValue = carData.getLastControllerTemp() / 10;

        } else if (itemIndex == MIN_CELL_VOLTAGE) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Minimum Cell Voltage", "Volts (V)");
                setScale(graph, 2, 4);
            }
            lastValue = carData.getLastMinimumCellV() / 1000;

        } else if (itemIndex == MAX_CELL_VOLTAGE) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Maximum Cell Voltage", "Volts (V)");
                setScale(graph, 2, 4);
            }
            lastValue = carData.getLastMaximumCellV() / 1000;
        }

        // add the latest data sample to corresponding series
        currentItem = itemIndex;
        series.addLast(null, lastValue);

        //Redraw Graph
            graph.redraw();

    }

    private void resetGraph(XYPlot graph, String label, String units) {
        //Clear all values from series
        int series_size = series.size();
        for(int i = 0; i < series_size; i ++){
            series.removeFirst();
        }

        graph.setTitle(label);
        graph.setRangeLabel(units);
    }

}
