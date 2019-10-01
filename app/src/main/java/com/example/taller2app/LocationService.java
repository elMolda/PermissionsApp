package com.example.taller2app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;

public class LocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.example.taller2app.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action =  intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    Location location = result.getLastLocation();
                    try {
                        LocationActivity.getInstance().updateLocationData(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()),Double.toString(location.getAltitude()));
                    }catch (Exception ex) {
                        Toast.makeText(context, Double.toString(location.getLatitude()), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }
}
