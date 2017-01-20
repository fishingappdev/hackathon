package com.example.dmiadmin.hackathonapp.geofence.client.viewholder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmiadmin.hackathonapp.R;
import com.example.dmiadmin.hackathonapp.geofence.client.models.GeofenceClientModel;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
    }

    public void bindToPost(GeofenceClientModel post, View.OnClickListener starClickListener) {
        titleView.setText(post.getDeviceId());
        authorView.setText(post.getIMEI());
        if(post.isInsideGeofence()) {
            numStarsView.setText("Inside office");
            numStarsView.setTextColor(Color.GREEN);
        }else{
            numStarsView.setText("Outside office");
            numStarsView.setTextColor(Color.RED);
        }

        if(post.getDeviceId().equalsIgnoreCase("355306066443708")){
            authorView.setText("Swati Singh - EO167DMP");
        }else if(post.getDeviceId().equalsIgnoreCase("f5893162e133e4a9")){
            authorView.setText("Nitish Srivastav - EO220DMP");
        }else{
            authorView.setText("Unknown");
        }

//        bodyView.setText(post.body);

        starView.setOnClickListener(starClickListener);
    }
}
