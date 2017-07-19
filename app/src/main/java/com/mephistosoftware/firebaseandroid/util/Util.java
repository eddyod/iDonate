package com.mephistosoftware.firebaseandroid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Util {

    public static final String URL_STORAGE_REFERENCE = "gs://chat-f309c.appspot.com";
    public static final String FOLDER_STORAGE_IMG = "images";

    public static void initToast(Context c, String message){
        Toast.makeText(c,message,Toast.LENGTH_SHORT).show();
    }

    public  static boolean verifyConnection(Context context) {
        boolean connected;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connected = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return connected;
    }

    public static String local(String latitudeFinal,String longitudeFinal){
        return "https://maps.googleapis.com/maps/api/staticmap?center="+latitudeFinal+","+longitudeFinal+"&zoom=18&size=280x280&markers=color:red|"+latitudeFinal+","+longitudeFinal;
    }


    /**
     * Verify the user is loggged in
     */
    public static boolean isLoggedIn(FirebaseAuth auth){
        boolean loggedIn = false;
        FirebaseAuth mFirebaseAuth = auth.getInstance();
        if (mFirebaseAuth.getCurrentUser() != null){
            loggedIn = true;
        }

        return loggedIn;
    }


}
