package com.example.dmiadmin.hackathonapp.geofence.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.dmiadmin.hackathonapp.R;
import com.example.dmiadmin.hackathonapp.geofence.GeofenceClientActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);
        String transition = mapTransition(event.getGeofenceTransition());

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.side_nav_bar)
                .setContentTitle("Geofence action")
                .setContentText(transition)
                .setTicker("Geofence action")
                .build();
        nm.notify(0, notification);

        Intent intentClient= new Intent(context, GeofenceClientActivity.class);
        intentClient.putExtra("eventTriggered",true);
        if(event.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER) {
            intentClient.putExtra("insideGeofence", true);
        }else{
            intentClient.putExtra("insideGeofence", false);
        }
        intentClient.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentClient);

    }

    private String mapTransition(int event) {
        switch (event) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "ENTER";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "EXIT";
            default:
                return "UNKNOWN";
        }
    }
}
