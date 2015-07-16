package au.com.teamarrow.arrowpoint.utils;

        import android.graphics.Color;

        import com.androidplot.xy.SimpleXYSeries;
        import com.androidplot.xy.*;
        import com.example.arrowpoint.R;

        import au.com.teamarrow.canbus.model.CarData;

/**
 * Created by Admin on 3/05/2015.
 */
public class GraphHandler{

    //Spinner Index
    static final int VEHICLE_SPEED = 0;
    static final int BUS_POWER = 1;
    static final int ARRAY_POWER = 2;
    static final int MOTOR_TEMP = 3;
    static final int MAX_CELL_TEMP = 4;
    static final int CONTROLLER_TEMP = 5;
    static final int MIN_CELL_VOLTAGE = 6;
    static final int MAX_CELL_VOLTAGE = 7;
    static final int BATTERY_VOLTAGE = 8;
    static final int BATTERY_AMPS = 9;

    // Private Variables
    private static int MAX_X_Values = 500;
    private SimpleXYSeries series = null;
    private int currentItem = 0;


    public void setupGraph(XYPlot graph, String label,int text_color, int line_color){

        // Add series to graph
        series = new SimpleXYSeries(label);
        series.useImplicitXVals();
        graph.addSeries(series, new LineAndPointFormatter(line_color, null, null, null));

         int color = R.color.text_color;
        // Layout And Styling
        graph.getLayoutManager().remove(graph.getLegendWidget());
        graph.getLayoutManager().remove(graph.getDomainLabelWidget());
        graph.getGraphWidget().getBackgroundPaint().setColor(Color.TRANSPARENT);
        graph.setBorderStyle(XYPlot.BorderStyle.NONE, null, null);
        graph.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        graph.getGraphWidget().getDomainLabelPaint().setColor(Color.TRANSPARENT);
        graph.getGraphWidget().getDomainOriginLabelPaint().setColor(text_color);
        graph.getGraphWidget().getDomainOriginLinePaint().setColor(text_color);
        graph.getGraphWidget().getDomainGridLinePaint().setColor(text_color);
        graph.getGraphWidget().getRangeGridLinePaint().setColor(text_color);
        graph.getGraphWidget().getRangeOriginLabelPaint().setColor(text_color);
        graph.getGraphWidget().getRangeOriginLinePaint().setColor(text_color);
        graph.getGraphWidget().getRangeLabelPaint().setColor(line_color);
        graph.getRangeLabelWidget().getLabelPaint().setColor(text_color);
        graph.getTitleWidget().getLabelPaint().setColor(text_color);



        graph.setRangeBoundaries(0, 120, BoundaryMode.FIXED);
        graph.setDomainBoundaries(0, MAX_X_Values, BoundaryMode.FIXED);
        graph.setDomainStepValue(5);
        graph.setRangeStepValue(13);
        graph.setRangeLabel("Initial Title");
        graph.getRangeLabelWidget().pack();
        resetGraph(graph, "Vehicle Speed", "Velocity (Km/h)");

    }


    private void setScale(XYPlot graph, int minY, int maxY, int stepVal) {

        graph.setRangeBoundaries(minY, maxY, BoundaryMode.FIXED);
        graph.setRangeStepValue(stepVal);

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
                setScale(graph, 0, 120, 13);
            }
            lastValue = carData.getLastSpeed();

        } else if (itemIndex == BUS_POWER) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Bus Power", "Watts (W)");
                setScale(graph, -5, 5, 11);
            }
            lastValue = carData.getLastBusPower();

        } else if (itemIndex == ARRAY_POWER) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Array Power", "Watts (W)");
                setScale(graph, 0, 800, 9);
            }
            lastValue = carData.getLastArrayTotalPower();

        } else if (itemIndex == MOTOR_TEMP) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Motor Temp", "Celsius (C)");
                setScale(graph, 0, 100, 11);
            }
            lastValue = carData.getLastMotorTemp() / 10;

        } else if (itemIndex == MAX_CELL_TEMP) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Maximum Cell Temp", "Celsius (C)");
                setScale(graph, 0, 100, 11);
            }
            lastValue = carData.getLastMaxCellTemp() / 10;

        } else if (itemIndex == CONTROLLER_TEMP) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Controller Temp", "Celsius (C)");
                setScale(graph, 0, 100, 11);
            }
            lastValue = carData.getLastControllerTemp() / 10;

        } else if (itemIndex == MIN_CELL_VOLTAGE) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Minimum Cell Voltage", "Volts (V)");
                setScale(graph, 0, 5, 6);
            }
            lastValue = carData.getLastMinimumCellV() / 1000;

        } else if (itemIndex == MAX_CELL_VOLTAGE) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Maximum Cell Voltage", "Volts (V)");
                setScale(graph, 0, 5, 6);
            }
            lastValue = carData.getLastMaximumCellV() / 1000;
        }else if (itemIndex == BATTERY_VOLTAGE) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Battery Voltage", "Volts (V)");
                setScale(graph, 0, 200, 5);
            }
            lastValue = carData.getLastBatteryVolts();
        }else if (itemIndex == BATTERY_AMPS) {
            if (currentItem != itemIndex) {
                resetGraph(graph, "Battery Current", "Amps (A)");
                setScale(graph, -60, 70, 14);
            }
            lastValue = carData.getLastBatteryAmps();
        }

        // add the latest data sample to corresponding series
        currentItem = itemIndex;
        series.addLast(null, lastValue);

        //Redraw Graph
        graph.redraw();

    }


    public void clearGraph(){
        int series_size = series.size();
        for(int i = 0; i < series_size; i ++){
            series.removeFirst();
        }
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
