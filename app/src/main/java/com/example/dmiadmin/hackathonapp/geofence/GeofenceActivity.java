package com.example.dmiadmin.hackathonapp.geofence;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmiadmin.hackathonapp.R;
import com.example.dmiadmin.hackathonapp.geofence.client.MainClientActivity;
import com.example.dmiadmin.hackathonapp.geofence.client.models.Comment;
import com.example.dmiadmin.hackathonapp.geofence.client.models.GeofenceModel;
import com.example.dmiadmin.hackathonapp.geofence.client.models.User;
import com.example.dmiadmin.hackathonapp.geofence.utils.UnsubscribeIfPresent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class GeofenceActivity extends BaseActivity {
    private static final String TAG = "GeofenceActivity";

    private EditText latitudeInput;
    private EditText longitudeInput;
    private EditText radiusInput;
    private TextView lastKnownLocationView;
    private DatabaseReference mGeofenceReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);
        initViews();
    }

    private void initViews() {
        lastKnownLocationView = (TextView) findViewById(R.id.last_known_location_view);
        latitudeInput = (EditText) findViewById(R.id.latitude_input);
        longitudeInput = (EditText) findViewById(R.id.longitude_input);
        radiusInput = (EditText) findViewById(R.id.radius_input);
        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGeofencingRequest();
            }
        });
        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearGeofence();
            }
        });
//        FirebaseDatabase.setAndroidContext(this);
//        FirebaseApp.initializeApp(getApplicationContext());
        setupFirebase();
    }

    @Override
    protected void onLocationPermissionGranted() {
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void clearGeofence() {
    }

    private void toast(String text) {
        Toast.makeText(GeofenceActivity.this, text, Toast.LENGTH_SHORT).show();
    }



    private void createGeofencingRequest() {
        try {
            double longitude = Double.parseDouble(longitudeInput.getText().toString());
            double latitude = Double.parseDouble(latitudeInput.getText().toString());
            float radius = Float.parseFloat(radiusInput.getText().toString());

            postGeoFenceData(longitude, latitude, radius);

//            Geofence geofence = new Geofence.Builder()
//                    .setRequestId("GEOFENCE")
//                    .setCircularRegion(latitude, longitude, radius)
//                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
//                    .build();
        } catch (NumberFormatException ex) {
            toast("Error parsing input.");
        }
    }



    private void postGeoFenceData(double longitude, double latitude, float radius) {

        final GeofenceModel geofenceModel = new GeofenceModel();
        geofenceModel.setLatitude(latitude);
        geofenceModel.setLongitude(longitude);
        geofenceModel.setRadius(radius);

        mGeofenceReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


        // Push the comment, it will appear in the list
        mGeofenceReference.push().setValue(geofenceModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                Toast.makeText(GeofenceActivity.this, "Saved",
                        Toast.LENGTH_SHORT).show();
            }
        });


            }
        });

//        FirebaseDatabase.getInstance().getReference().child("geofence")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // Get user information
//                        GeofenceModel geofenceModel = dataSnapshot.getValue(GeofenceModel.class);
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
    }

    public void setupFirebase(){

//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("softprodigy010@gmail.com", "mohali123")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
//                        hideProgressDialog();

                        if (task.isSuccessful()) {
//                            onAuthSuccess(task.getResult().getUser());

                            Toast.makeText(GeofenceActivity.this, "Sign In success",
                                    Toast.LENGTH_SHORT).show();

                            FirebaseApp.initializeApp(GeofenceActivity.this);
                            mGeofenceReference = FirebaseDatabase.getInstance().getReference("geofence");
//                                    .child("geofence");
                        } else {
                            Toast.makeText(GeofenceActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
