package com.example.taller2app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private TextView txtLtd;
    private TextView txtLng;
    private TextView txtAlt;
    private TextView txtPdb;
    private LinearLayout auxLay;
    private List<String> locations;
    private FusedLocationProviderClient flpc;
    private final double[] plzBlvr = new double[]{4.598000,-74.076000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        requestPermission();

        txtAlt = findViewById(R.id.txtAlt);
        txtLng = findViewById(R.id.txtLng);
        txtLtd = findViewById(R.id.txtLtd);
        txtPdb = findViewById(R.id.txtPbd);
        auxLay = findViewById(R.id.auxScrlLyt);

        fillLocationInfo();

    }

    private void requestPermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(getApplicationContext(), "Todos los permisos fueron brindados!", Toast.LENGTH_SHORT).show();
                        fillLocationInfo();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext(), "Funcionalidad Limitada!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        Toast.makeText(getApplicationContext(), "Se necesita para acceder a ubicación!", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private double computeDistanceTo(double lat1, double lon1, double lat2, double lon2){
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return dist;
        }
    }

    private void fillLocationInfo(){
        flpc = LocationServices.getFusedLocationProviderClient(this);
        flpc.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    txtLng.setText(Double.toString(location.getLongitude()));
                    txtLtd.setText(Double.toString(location.getLatitude()));
                    txtAlt.setText(Double.toString(location.getAltitude()));
                    txtPdb.setText(Double.toString(computeDistanceTo(location.getLatitude(),location.getLongitude(),plzBlvr[0],plzBlvr[1])));
                }
            }
        });
    }
}
