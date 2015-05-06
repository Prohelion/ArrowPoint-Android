package au.com.teamarrow.arrowpoint;

import com.example.arrowpoint.R;


import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;
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
import android.widget.TextView;
import android.widget.Toast;


import au.com.teamarrow.arrowpoint.fragments.UpdateableFragment;
import au.com.teamarrow.canbus.comms.DatagramReceiver;
import au.com.teamarrow.canbus.model.CarData;

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

    CarData carData;
	
	int defaultTextColour = 0;
	int currentTab = 0;


	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		handler = new Handler();
        carData = new CarData();
		
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

		super.onCreate(savedInstanceState);
		handler.post(sendData);

    }


    public CarData getCarData() {
        return carData;
    }


	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(sendData);
	}


	private final Runnable sendData = new Runnable(){
		public void run(){
			try {
				//prepare and send the data here..

				UpdateableFragment activeFragment = (UpdateableFragment)mSectionsPagerAdapter.getCurrentFragment();
				if (activeFragment != null) activeFragment.Update(getWindow().getDecorView(),carData);
				handler.postDelayed(this, 1000);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	};



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
            myDatagramReceiver = new DatagramReceiver(carData,simulateMode);
            myDatagramReceiver.start();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		currentTab = tab.getPosition();
		mViewPager.setCurrentItem(currentTab);


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
        myDatagramReceiver = new DatagramReceiver(carData,simulateMode);
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


    public void onVelocityClicked(View view){
        Toast.makeText(ArrowPoint.this, "Velocity Added", Toast.LENGTH_SHORT).show();
    }





}
