package com.example.geomonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.io.Serializable;
import java.util.Date;

public class GeofenceBroadcastReceiver extends BroadcastReceiver implements Serializable {

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e("GeofenceBroadcastReceiver", errorMessage);
            return;
        }

        if (geofencingEvent.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Log.d("mylocation", "Entering Geofence Area");
            MapsActivity.myLocation.setAction("ENTER");
        }
        if (geofencingEvent.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d("mylocation", "Exiting Geofence Area");
            MapsActivity.myLocation.setAction("EXIT");
        }

        MyLocation.getCurrentLocation(context, MapsActivity.myLocation);
        Log.d("coordinates", MapsActivity.myLocation.getAction()+": "+MapsActivity.myLocation.getLat()+", "+MapsActivity.myLocation.getLon());
        Date date = new Date();
        MapsActivity.myLocation.setTimestamp(date.getTime());

        try {
            MapsActivity.myLocation.persist(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}