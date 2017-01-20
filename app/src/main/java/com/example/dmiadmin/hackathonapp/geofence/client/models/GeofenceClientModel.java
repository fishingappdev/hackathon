package com.example.dmiadmin.hackathonapp.geofence.client.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Karan on 20/1/17.
 */
@IgnoreExtraProperties
public class GeofenceClientModel {
    double longitude;
    double latitude;
    boolean insideGeofence;
    String deviceId;
    String IMEI;


    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isInsideGeofence() {
        return insideGeofence;
    }

    public void setInsideGeofence(boolean insideGeofence) {
        this.insideGeofence = insideGeofence;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }
}
