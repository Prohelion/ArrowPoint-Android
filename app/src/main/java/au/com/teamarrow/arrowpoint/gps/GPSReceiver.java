package au.com.teamarrow.arrowpoint.gps;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import au.com.teamarrow.canbus.model.CarData;

public class GPSReceiver {

    private CarData carData;

    public GPSReceiver(Context context, CarData carData) {
        this.carData = carData;
        LocalBroadcastManager.getInstance(context).registerReceiver(
                mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("Status");
            Bundle b = intent.getBundleExtra("Location");
            Location lastKnownLoc = (Location) b.getParcelable("Location");
            if (lastKnownLoc != null) {
                carData.setLatitude(lastKnownLoc.getLatitude());
                carData.setLongitude(lastKnownLoc.getLongitude());
                Log.d("receiver", "Got message: " + String.valueOf(lastKnownLoc.getLatitude()));
            }
        }
    };

}
