package com.example.dmiadmin.hackathonapp.geofence;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmiadmin.hackathonapp.R;
import com.example.dmiadmin.hackathonapp.geofence.client.models.GeofenceClientModel;
import com.example.dmiadmin.hackathonapp.geofence.client.models.GeofenceModel;
import com.example.dmiadmin.hackathonapp.geofence.utils.DisplayTextOnViewAction;
import com.example.dmiadmin.hackathonapp.geofence.utils.GeofenceBroadcastReceiver;
import com.example.dmiadmin.hackathonapp.geofence.utils.LocationToStringFunc;
import com.example.dmiadmin.hackathonapp.geofence.utils.UnsubscribeIfPresent;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;


public class GeofenceClientActivity extends BaseActivity {
    private static final String TAG = "GeofenceActivity";

    private ReactiveLocationProvider reactiveLocationProvider;
    private TextView lastKnownLocationView;
    private TextView infoText;
    private Subscription lastKnownLocationSubscription;
    private DatabaseReference mUserGeofenceReference;

    boolean eventTriggered = false;
    boolean insideGeofence= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactiveLocationProvider = new ReactiveLocationProvider(this);
        setContentView(R.layout.client_geofence_layout);
        initViews();
    }

    private void initViews() {
        lastKnownLocationView = (TextView) findViewById(R.id.last_known_location_view);
        infoText = (TextView) findViewById(R.id.infoText);
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addGeofence();
                getGeofenceData();
            }
        });
//        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clearGeofence();
//            }
//        });
        eventTriggered = getIntent().getBooleanExtra("eventTriggered", false);
        insideGeofence = getIntent().getBooleanExtra("insideGeofence", false);
        if(eventTriggered){
            postGeoFenceData(0,0,insideGeofence);
        }
    }

    @Override
    protected void onLocationPermissionGranted() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "Please provide location permission", Toast.LENGTH_SHORT).show();
            ;
            return;
        }
        lastKnownLocationSubscription = reactiveLocationProvider
                .getLastKnownLocation()
                .map(new LocationToStringFunc())
                .subscribe(new DisplayTextOnViewAction(lastKnownLocationView));
    }

    @Override
    protected void onStop() {
        super.onStop();
        UnsubscribeIfPresent.unsubscribe(lastKnownLocationSubscription);
    }

//    private void clearGeofence() {
//        reactiveLocationProvider.removeGeofences(createNotificationBroadcastPendingIntent()).subscribe(new Action1<Status>() {
//            @Override
//            public void call(Status status) {
//                toast("Geofences removed");
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable throwable) {
//                toast("Error removing geofences");
//                Log.d(TAG, "Error removing geofences", throwable);
//            }
//        });
//    }

    private void toast(String text) {
        Toast.makeText(GeofenceClientActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private PendingIntent createNotificationBroadcastPendingIntent() {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, GeofenceBroadcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addGeofence(GeofenceModel geofenceModel) {
        final GeofencingRequest geofencingRequest = createGeofencingRequest(geofenceModel);
        if (geofencingRequest == null) return;

        final PendingIntent pendingIntent = createNotificationBroadcastPendingIntent();
        reactiveLocationProvider
                .removeGeofences(pendingIntent)
                .flatMap(new Func1<Status, Observable<Status>>() {
                    @Override
                    public Observable<Status> call(Status pendingIntentRemoveGeofenceResult) {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return null;
                        }
                        return reactiveLocationProvider.addGeofences(pendingIntent, geofencingRequest);
                    }
                })
                .subscribe(new Action1<Status>() {
                    @Override
                    public void call(Status addGeofenceResult) {
                        toast("Geofence added, success: " + addGeofenceResult.isSuccess());

                        infoText.setText("Success");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        toast("Error adding geofence.");
                        Log.d(TAG, "Error adding geofence.", throwable);

                        infoText.setText("Error");
                    }
                });
    }

    private GeofencingRequest createGeofencingRequest(GeofenceModel geofenceModel) {
        try {
            Geofence geofence = new Geofence.Builder()
                    .setRequestId("GEOFENCE")
                    .setCircularRegion(geofenceModel.getLatitude(), geofenceModel.getLongitude(), new Double(geofenceModel.getRadius()).intValue())
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            return new GeofencingRequest.Builder().addGeofence(geofence).build();
        } catch (Exception ex) {
            toast("Error parsing input.");
            return null;
        }
    }


    private void getGeofenceData() {
        infoText.setText("Getting geofence from server");
        FirebaseApp.initializeApp(GeofenceClientActivity.this);
        FirebaseDatabase.getInstance().getReference().child("geofence")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        infoText.setText("SettingUp geofence");
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            GeofenceModel geofenceModel = postSnapshot.getValue(GeofenceModel.class);
                            addGeofence(geofenceModel);
                            break;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        infoText.setText("Server Error");
                    }
                });
    }

    private void postGeoFenceData(double longitude, double latitude, boolean isInsideGeofence) {

        FirebaseApp.initializeApp(GeofenceClientActivity.this);
        mUserGeofenceReference = FirebaseDatabase.getInstance().getReference("userdata");
        String android_id = Settings.Secure.getString(GeofenceClientActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = telephonyManager.getDeviceId();
        if (!TextUtils.isEmpty(deviceId)) {
            final GeofenceClientModel geofenceClientModel = new GeofenceClientModel();
            geofenceClientModel.setLatitude(latitude);
            geofenceClientModel.setLongitude(longitude);
            geofenceClientModel.setIMEI(deviceId);
            geofenceClientModel.setDeviceId(android_id);
            geofenceClientModel.setInsideGeofence(isInsideGeofence);

//            DatabaseReference listOfObjects = mUserGeofenceReference.orderByChild("deviceId").equalTo(deviceId).getRef();
//            listOfObjects.removeValue(new DatabaseReference.CompletionListener() {
//                @Override
//                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    // Push the comment, it will appear in the list
                    mUserGeofenceReference.push().setValue(geofenceClientModel, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            Toast.makeText(GeofenceClientActivity.this, "Saved User Data",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
//                }
//            });

        }
    }
}
