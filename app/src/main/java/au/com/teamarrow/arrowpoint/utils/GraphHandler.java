package au.com.teamarrow.arrowpoint.utils;

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

        import com.jjoe64.graphview.GraphView;
        import com.jjoe64.graphview.series.DataPoint;
        import com.jjoe64.graphview.series.LineGraphSeries;

        import au.com.teamarrow.arrowpoint.fragments.GraphFragment;
        import au.com.teamarrow.canbus.comms.DatagramReceiver;
        import au.com.teamarrow.arrowpoint.fragments.PlaceholderFragment;
        import au.com.teamarrow.arrowpoint.ArrowPoint;
        import au.com.teamarrow.canbus.model.CarData;

        import java.util.Calendar;

/**
 * Created by Admin on 3/05/2015.
 */
public class GraphHandler {

    int primaryCurrentItem = -1;
    int secondaryCurrentItem = -1;
    double lastPrimaryValue = 0;
    double lastSecondaryValue = 0;

    static final boolean APPEND = false;

    // Primary Spinner
    static final int VEHICLE_SPEED = 0;
    static final int BUS_POWER = 1;
    static final int ARRAY_POWER = 2;
    static final int MOTOR_TEMP = 3;
    static final int MAX_CELL_TEMP = 4;
    static final int CONTROLLER_TEMP = 5;
    static final int MIN_CELL_VOLTAGE = 6;
    static final int MAX_CELL_VOLTAGE = 7;


    private void setScale(GraphView graphViewId,boolean primaryAxis, int minY, int maxY) {

        if (primaryAxis) {
            graphViewId.getViewport().setMinY(minY);
            graphViewId.getViewport().setMaxY(maxY);
        } else {
            graphViewId.getSecondScale().setMinY(minY);
            graphViewId.getSecondScale().setMaxY(maxY);
        }

    }


    public void addData(GraphView graphViewId,CarData carData,double xValue,int itemIndex, boolean primaryAxis) {

        double lastValue = 0;

        LineGraphSeries<DataPoint> m_Series;

        if (primaryAxis)
            m_Series = (LineGraphSeries) graphViewId.getSeries().get(GraphFragment.GRAPH_1);
        else
            m_Series = (LineGraphSeries) graphViewId.getSecondScale().getSeries().get(GraphFragment.GRAPH_1);


        if (itemIndex == VEHICLE_SPEED) {
            if (primaryCurrentItem != itemIndex) {
                setScale(graphViewId, primaryAxis, 0, 130);
                resetGraph(m_Series,primaryAxis);
            }
            lastValue = carData.getLastSpeed();

        } else if (itemIndex == BUS_POWER) {
            if (primaryCurrentItem != itemIndex) {
                setScale(graphViewId, primaryAxis, 0, 50);
                resetGraph(m_Series,primaryAxis);
            }
            lastValue = carData.getLastBusPower();

        } else if (itemIndex == ARRAY_POWER) {
            if (primaryCurrentItem != itemIndex) {
                setScale(graphViewId, primaryAxis, -5, 5);
                resetGraph(m_Series,primaryAxis);
            }
            lastValue = carData.getLastArrayTotalPower() / 1000;

        } else if (itemIndex == MOTOR_TEMP) {
            if (primaryCurrentItem != itemIndex) {
                setScale(graphViewId, primaryAxis, 0, 100);
                resetGraph(m_Series,primaryAxis);
            }
            lastValue = carData.getLastMotorTemp();

        } else if (itemIndex == MAX_CELL_TEMP) {
            if (primaryCurrentItem != itemIndex) {
                setScale(graphViewId, primaryAxis, 0, 100);
                resetGraph(m_Series,primaryAxis);
            }
            lastValue = carData.getLastMaxCellTemp();

        } else if (itemIndex == CONTROLLER_TEMP) {
            if (primaryCurrentItem != itemIndex) {
                setScale(graphViewId, primaryAxis, 0, 100);
                resetGraph(m_Series,primaryAxis);
            }
            lastValue = carData.getLastControllerTemp();

        } else if (itemIndex == MIN_CELL_VOLTAGE) {
            if (primaryCurrentItem != itemIndex) {
                setScale(graphViewId, primaryAxis, 2, 4);
                resetGraph(m_Series,primaryAxis);
            }
            lastValue = carData.getLastMinimumCellV() / 1000;

        } else if (itemIndex == MAX_CELL_VOLTAGE) {
            if (primaryCurrentItem != itemIndex) {
                setScale(graphViewId, primaryAxis, 2, 4);
                resetGraph(m_Series,primaryAxis);
            }
            lastValue = carData.getLastMaximumCellV() / 1000;
        }

        if (primaryAxis) {
            primaryCurrentItem = itemIndex;
            lastPrimaryValue = lastValue;
            m_Series.setColor(Color.BLUE);
            m_Series.appendData(new DataPoint(xValue, lastPrimaryValue), APPEND, 120);
        } else {
            secondaryCurrentItem = itemIndex;
            lastSecondaryValue = lastValue;
            m_Series.setColor(Color.RED);
            m_Series.appendData(new DataPoint(xValue, lastSecondaryValue), APPEND, 120);
        }
    }

    private void resetGraph(LineGraphSeries<DataPoint> m_Series, boolean primaryAxis ) {

        if (primaryAxis) {
            m_Series.resetData(new DataPoint[]{new DataPoint(0, lastPrimaryValue)});
        } else {
            m_Series.resetData(new DataPoint[]{new DataPoint(0, lastSecondaryValue)});
        }

    }

    public void resetAllGraph(GraphView graphViewId)
    {

        resetGraph((LineGraphSeries) graphViewId.getSeries().get(GraphFragment.GRAPH_1),true);
        resetGraph((LineGraphSeries) graphViewId.getSeries().get(GraphFragment.GRAPH_2),false);

    }

}
