package au.com.teamarrow.arrowpoint;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.example.arrowpoint.R;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import android.app.Activity;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import au.com.teamarrow.arrowpoint.fragments.GraphFragment;
import au.com.teamarrow.canbus.comms.DatagramReceiver;
import au.com.teamarrow.arrowpoint.fragments.PlaceholderFragment;

public class ArrowPoint extends Activity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v13.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	boolean simulateMode = false;
	public DatagramReceiver myDatagramReceiver = null;

	Handler handler;
	
	int defaultTextColour = 0;


	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		handler = new Handler();
		
		setContentView(R.layout.activity_arrow_point);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(),this);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.arrow_point, menu);
        menu.getItem(1);


		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.debug_mode) {
			simulateMode = !simulateMode;
            myDatagramReceiver = new DatagramReceiver(updateValues,handler,simulateMode);
            myDatagramReceiver.start();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	
    protected void onResume() {
    	super.onResume();
        myDatagramReceiver = new DatagramReceiver(updateValues,handler,simulateMode);
        myDatagramReceiver.start();
       
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    protected void onPause() {
    	super.onPause();
        myDatagramReceiver.kill();
        
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }	
	
	
    
    private void  setColourStatus(TextView textView, boolean status) {
    	
    	// Run this once and once only to get the origional colour
    	if (defaultTextColour == 0) defaultTextColour = textView.getCurrentTextColor();
    	
    	if ( status ) {
    		textView.setTypeface(Typeface.DEFAULT_BOLD);
    		textView.setTextColor(Color.RED);
    	} else {
    		textView.setTypeface(Typeface.DEFAULT);
    		textView.setTextColor(defaultTextColour);
    	}
    	
    }
    
	
	
    private Runnable updateValues = new Runnable() {

        private double graphLastXValue = 5d;
        private final Handler mHandler = new Handler();
        private LineGraphSeries<DataPoint> mSeries;

        public void run() {
        	
        	NumberFormat formatterWithDecimal = new DecimalFormat("#0.00");

            mSeries = new LineGraphSeries<DataPoint>();
        	        
            if (myDatagramReceiver == null) return;

	            
	            // **************** Dashboard Detail ***************	            

	            TextView speedTxt=(TextView)findViewById(R.id.Speed);
	            //Button speedTarget=(Button)findViewById(R.id.btnTargetSpeed);	            
	            TextView powerTxt=(TextView)findViewById(R.id.Power);
	            ProgressBar batteryBar=(ProgressBar)findViewById(R.id.Battery);
	            TextView batteryTxt=(TextView)findViewById(R.id.BatteryText);
	            ProgressBar setPointBar=(ProgressBar)findViewById(R.id.pbSetpoint);
	            TextView setPointBarTxt=(TextView)findViewById(R.id.pbSetpointText);
	            TextView motorTempTxt=(TextView)findViewById(R.id.txtMotorTemp);
	            TextView minCellVTxt=(TextView)findViewById(R.id.txtMinCellV);
	            TextView lastLockedSOCTxt = (TextView)findViewById(R.id.txtLastLockedSOC);
	            
	            if ( speedTxt != null) {	            
		            // Now setup all the values for the dashboard	            
		            speedTxt.setText(Integer.toString(myDatagramReceiver.getLastSpeed()));
		            
		          /*  if (myDatagramReceiver.getCruiseTargetSpeed() == 0 )
		            	speedTarget.setText("Target: Not Set");
		            else
		            	speedTarget.setText("Target: " + myDatagramReceiver.getCruiseTargetSpeed() + " kph");	  	            
		            */	            	           
		            powerTxt.setText(formatterWithDecimal.format(myDatagramReceiver.getLastBusPower()));
		            
		            batteryBar.setProgress((int)myDatagramReceiver.getLastSOC());
		            batteryTxt.setText(myDatagramReceiver.getLastSOC() + "%");
	            		            
		            setPointBar.setProgress(myDatagramReceiver.getLastMotorPowerSetpoint());
		            setPointBarTxt.setText(myDatagramReceiver.getLastMotorPowerSetpoint() + "%");
		            
		            motorTempTxt.setText(formatterWithDecimal.format(myDatagramReceiver.getLastMotorTemp()) + "c");
		            minCellVTxt.setText(formatterWithDecimal.format((double)myDatagramReceiver.getLastMinimumCellV()/1000) + "v");
		            
		            lastLockedSOCTxt.setText("Last Locked SOC:" + formatterWithDecimal.format(myDatagramReceiver.getLastLockedSOC()));


	            }
		            
	            // **************** Power Detail ***************

	            // Solar Detail Data
	            TextView array1Volts = (TextView)findViewById(R.id.txtArray1Voltage);
	            TextView array2Volts = (TextView)findViewById(R.id.txtArray2Voltage);
	            TextView array3Volts = (TextView)findViewById(R.id.txtArray3Voltage);
	            TextView array1Amps = (TextView)findViewById(R.id.txtArray1Current);
	            TextView array2Amps = (TextView)findViewById(R.id.txtArray2Current);
	            TextView array3Amps = (TextView)findViewById(R.id.txtArray3Current);
	            TextView array1Power = (TextView)findViewById(R.id.txtArray1Power);
	            TextView array2Power = (TextView)findViewById(R.id.txtArray2Power);
	            TextView array3Power = (TextView)findViewById(R.id.txtArray3Power);
	            TextView totalArrayPower = (TextView)findViewById(R.id.txtTotalArrayPower);
	            TextView controllerVolts = (TextView)findViewById(R.id.txtControllerVoltage);
	            TextView controllerAmps = (TextView)findViewById(R.id.txtControllerCurrent);
	            TextView controllerPower = (TextView)findViewById(R.id.txtControllerPower);
	            TextView batteryVolts = (TextView)findViewById(R.id.txtBatteryVoltage);
	            TextView batteryAmps = (TextView)findViewById(R.id.txtBatteryCurrent);
	            TextView batteryPower = (TextView)findViewById(R.id.txtBatteryPower);

	            if ( array1Volts != null ) {
		            // Setup solar detail
		            array1Volts.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArray1Volts()));
		            array2Volts.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArray2Volts()));
		            array3Volts.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArray3Volts()));
		            array1Amps.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArray1Amps()));
		            array2Amps.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArray2Amps()));
		            array3Amps.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArray3Amps()));
		            array1Power.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArray1Power()));
		            array2Power.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArray2Power()));
		            array3Power.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArray3Power()));
		            totalArrayPower.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArrayTotalPower()));
		            
		            // Setup controller detail
		            controllerVolts.setText(formatterWithDecimal.format(myDatagramReceiver.getLastBusVolts()));
		            controllerAmps.setText(formatterWithDecimal.format(myDatagramReceiver.getLastBusAmps()));
		            controllerPower.setText(formatterWithDecimal.format(myDatagramReceiver.getLastBusPower()*-1000));
		            
		            // Setup battery detail
		            batteryVolts.setText(formatterWithDecimal.format(myDatagramReceiver.getLastBatteryVolts()));
		            batteryAmps.setText(formatterWithDecimal.format(myDatagramReceiver.getLastBatteryAmps()));
		            batteryPower.setText(formatterWithDecimal.format(myDatagramReceiver.getLastArrayTotalPower()-(myDatagramReceiver.getLastBusPower()*1000)));

	            }
	            
	            
	            // **************** Driver Detail ***************

	            // Driver Detail Data
	            	            	            
	            TextView state = (TextView)findViewById(R.id.lblState);	            	            
	            TextView regen = (TextView)findViewById(R.id.lblRegen);
	            TextView brakes = (TextView)findViewById(R.id.lblBrakes);
	            TextView horn = (TextView)findViewById(R.id.lblHorn);
	            TextView motorTempDetail = (TextView)findViewById(R.id.txtMotorTempDetail);
	            TextView controllerTemp  = (TextView)findViewById(R.id.txtControllerTemp);
	            TextView minBattery  = (TextView)findViewById(R.id.txtMinBattery);
	            TextView maxBattery  = (TextView)findViewById(R.id.txtMaxBattery);
	            TextView maxBatteryTemp  = (TextView)findViewById(R.id.txtMaxBatteryTemp);
	            TextView twelveVolt  = (TextView)findViewById(R.id.txt12VoltDetail);

                GraphView graph = (GraphView)findViewById(R.id.graph);

                if (graph!=null) {

                    mSeries = (LineGraphSeries)graph.getSeries().get(0);
                    graphLastXValue += 1d;
                    mSeries.appendData(new DataPoint(graphLastXValue, myDatagramReceiver.getLastBusPower()) , true, 600);
                    //mHandler.postDelayed(this, 200);

                }


	            if ( state != null) {	            	            	
	            	
	            	if ( myDatagramReceiver.isIdle()) state.setText("Idle");
	            	if ( myDatagramReceiver.isReverse()) state.setText("Reverse");
	            	if ( myDatagramReceiver.isNeutral()) state.setText("Neutral");
	            	if ( myDatagramReceiver.isDrive()) state.setText("Drive");
	            		            	            	
	            	setColourStatus(state,myDatagramReceiver.isIdle() || 
	            			myDatagramReceiver.isReverse() ||
	            			myDatagramReceiver.isNeutral() ||
	            			myDatagramReceiver.isDrive());	 
	            	
	            }
	            
	            
	            if ( regen != null ) {
	            	setColourStatus(regen,myDatagramReceiver.isRegen());
	            	setColourStatus(brakes,myDatagramReceiver.isBrakes());
	            		            	
	            }
	            
	            if (motorTempDetail != null) {
	            	motorTempDetail.setText(formatterWithDecimal.format(myDatagramReceiver.getLastMotorTemp()) + "c");
		            controllerTemp.setText(formatterWithDecimal.format(myDatagramReceiver.getLastControllerTemp()) + "c");
		            minBattery.setText(formatterWithDecimal.format((double)myDatagramReceiver.getLastMinimumCellV()/1000) + "v");
		            maxBattery.setText(formatterWithDecimal.format((double)myDatagramReceiver.getLastMaximumCellV()/1000) + "v");
		            maxBatteryTemp.setText(formatterWithDecimal.format((double)myDatagramReceiver.getLastMaxCellTemp()/10) + "c");
		            twelveVolt.setText(formatterWithDecimal.format((double)myDatagramReceiver.getLastTwelveVBusVolts()/1000) + "v");
		            	
	            }

	            
        }

	
    };
	

	
}
