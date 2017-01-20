package com.example.dmiadmin.hackathonapp.utilhack;

/**
 * Created by Swati on 1/20/2017.
 */

public class Utils {

    public static String readDeviceInfo() {
        String s = "Debug-infos:";
        s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        s += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        s += "\n Device: " + android.os.Build.DEVICE;
        s += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";
        return s;
    }
}
