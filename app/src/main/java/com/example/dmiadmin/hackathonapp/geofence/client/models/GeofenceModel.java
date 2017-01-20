package com.example.dmiadmin.hackathonapp.geofence.client.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Karan on 20/1/17.
 */
@IgnoreExtraProperties
public class GeofenceModel {
    double longitude;
    double latitude;
    double radius;

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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
