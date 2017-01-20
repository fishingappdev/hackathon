package com.example.dmiadmin.hackathonapp.geofence.utils;

import rx.Subscription;

public final class UnsubscribeIfPresent {
    private UnsubscribeIfPresent() {//no instance
    }

    public static void unsubscribe(Subscription subscription) {
        if (subscription != null) subscription.unsubscribe();
    }
}
