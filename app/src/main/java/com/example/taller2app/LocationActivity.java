package com.example.taller2app;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private TextView txtLtd;
    private TextView txtLng;
    private TextView txtAlt;
    private TextView txtPdb;
    private Button   svLct;
    private LinearLayout auxLay;
    private JSONArray locations = new JSONArray();
    private FusedLocationProviderClient flpc;
    private LocationRequest lctRqst;
    private final double[] plzBlvr = new double[]{4.598000,-74.076000};
    private String crrLtd;
    private String crrLng;
    private String crrAlt;

    static LocationActivity instance;
    public static LocationActivity getInstance(){
        return instance;
    }

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
        svLct = findViewById(R.id.svLct);

        svLct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSaveCurrentLocation();
            }
        });

        instance = this;

    }

    private void showSaveCurrentLocation() {
        String location = crrLtd + " | " + crrLng + " | " + crrAlt;
        TextView newLocation = new TextView(auxLay.getContext());
        newLocation.setText(location);
        auxLay.addView(newLocation);
        JSONObject object = currentLocationToJSON();
        locations.put(object);
        Writer output = null;
        String filename = "locations.json";
        try {
            File file = new File(getBaseContext().getExternalFilesDir(null), filename);
            output = new BufferedWriter(new FileWriter(file));
            output.write(locations.toString());
            output.close();
            Toast.makeText(getApplicationContext(), "Localización guardada", Toast.LENGTH_LONG).show();
        } catch (Exception ex){
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private JSONObject currentLocationToJSON(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("latitude:",crrLtd);
            obj.put("longitude:",crrLng);
            obj.put("altitude",crrAlt);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    private void requestPermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(getApplicationContext(), "Todos los permisos fueron brindados!", Toast.LENGTH_SHORT).show();
                        updateLocation();
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

    private void updateLocation(){
        buildLocationRequest();
        flpc = LocationServices.getFusedLocationProviderClient(this);
        flpc.requestLocationUpdates(lctRqst,getPendingIntent());
    }

    private PendingIntent getPendingIntent(){
        Intent intent = new Intent(this,LocationService.class);
        intent.setAction(LocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest(){
        lctRqst = new LocationRequest();
        lctRqst.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lctRqst.setInterval(5000);
        lctRqst.setFastestInterval(3000);
        lctRqst.setSmallestDisplacement(10);
    }

    public void updateLocationData(final String ltd, final String lng, final String alt){
        LocationActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                crrAlt = alt;
                crrLng = lng;
                crrLtd = ltd;
                txtLng.setText(crrLng);
                txtLtd.setText(crrLtd);
                txtAlt.setText(crrAlt);
                txtPdb.setText(Double.toString(computeDistanceTo(Double.parseDouble(crrLtd),Double.parseDouble(crrLng),plzBlvr[0],plzBlvr[1])));
            }
        });
    }
}
