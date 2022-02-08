package com.example.geomonitor;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class MyLocation {
    private double lon;
    private double lat;
    private String action;
    private long timestamp;
    private DbHelper helper;


    public MyLocation(double lon, double lat, String action, long timestamp) {
        this.lon = lon;
        this.lat = lat;
        this.action = action;
        this.timestamp = timestamp;
    }

    public MyLocation(){}

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }

    public void setLon(double lon) { this.lon = lon; }

    public String getAction() { return action; }

    public void setAction(String action) { this.action = action; }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public long persist(Context context) throws Exception {
        helper = new DbHelper(context);
        ContentValues values = new ContentValues();
        values.put(DbHelper.FIELD_1, this.lat);
        values.put(DbHelper.FIELD_2, this.lon);
        values.put(DbHelper.FIELD_3, this.action);
        values.put(DbHelper.FIELD_4, this.timestamp);
        SQLiteDatabase db = helper.getWritableDatabase();
        long res = db.insert(DbHelper.TABLE_NAME, null, values);
        db.close();
        if (res == -1) {
            throw new Exception("Insertion failed!");
        }
        Log.d("DBpersist", "location persisted successfully");
        return  res;
    }

    public static ArrayList<MyLocation> getAllEntries(Context context) {
        DbHelper helper = new DbHelper(context);
        ArrayList<MyLocation> locations = new ArrayList<>();
        Cursor cursor = helper.selectAll();
        if(cursor.moveToFirst()) {
            do{
                MyLocation location = new MyLocation(cursor.getDouble(0), cursor.getDouble(1), cursor.getString(1), cursor.getLong(1));
                locations.add(location);
            }while(cursor.moveToNext());
        }
        return locations;
    }

    public static void getCurrentLocation(Context context, MyLocation myLocation) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                myLocation.setLon(lastKnownLocationGPS.getLongitude());
                myLocation.setLat(lastKnownLocationGPS.getLatitude());
            }
        }
    }
}
