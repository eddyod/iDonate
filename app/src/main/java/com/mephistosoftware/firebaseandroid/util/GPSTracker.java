package com.mephistosoftware.firebaseandroid.util;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.mephistosoftware.firebaseandroid.model.MapModel;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * Created by eddyod on 11/24/16.
 */

public class GPSTracker extends Service implements LocationListener {

    private final Context context;

    Location location;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    double latitude;
    double longitude;

    private static final long MinDistanceChangeUpdate = 10;
    private static final long MinTimeUpdate = 1000 * 60 * 1;
    private static final String TAG = "GPSTracker";

    protected LocationManager locationManager;
    private MapModel mapModel;

    public GPSTracker(Context context){
        this.context = context;
        getLocation();
    }

    public boolean canGetLocation(){
        return this.canGetLocation;
    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude (){
        if(location != null){
            longitude = location.getLongitude();
        }
        return  longitude;
    }

    public void showSettingsAlert(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS is Settings! ");
        alertDialog.setMessage("GPS is not enable, Wanna enable?");

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }



    public Location getLocation(){
        try{
            //locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isNetworkEnabled && !isGPSEnabled){
                Log.e("GPSTracker", "!isNetworkEnabled && !isGPSEnabled");
                Log.e("GPSTracker", "is network location enabled: " + isNetworkEnabled );
                Log.e("GPSTracker", "is GPS location enabled: " + isGPSEnabled);
            }else{
                this.canGetLocation = true;

                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MinTimeUpdate, MinDistanceChangeUpdate, this);

                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            mapModel = this.createMap(latitude, longitude);
                            setMapModel(mapModel);
                        }
                    }
                }


                if(isGPSEnabled){
                    if(location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MinTimeUpdate, MinDistanceChangeUpdate, this);

                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                setMapModel(mapModel);
                            }
                        }
                    }

                }
            }

        }catch(Exception e){
            Log.e("GPSTracker", "error getting location", e);
            e.printStackTrace();
        }

        return location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {

    }


    @Override
    public void onProviderDisabled(String provider) {

    }


    private MapModel createMap(Double latitude, Double longitude) {
        // state map
        mapModel = null;
        Map<String, String> states = new HashMap<String, String>();
        states.put("Alabama","AL");
        states.put("Alaska","AK");
        states.put("Alberta","AB");
        states.put("American Samoa","AS");
        states.put("Arizona","AZ");
        states.put("Arkansas","AR");
        states.put("Armed Forces (AE)","AE");
        states.put("Armed Forces Americas","AA");
        states.put("Armed Forces Pacific","AP");
        states.put("British Columbia","BC");
        states.put("California","CA");
        states.put("Colorado","CO");
        states.put("Connecticut","CT");
        states.put("Delaware","DE");
        states.put("District Of Columbia","DC");
        states.put("Florida","FL");
        states.put("Georgia","GA");
        states.put("Guam","GU");
        states.put("Hawaii","HI");
        states.put("Idaho","ID");
        states.put("Illinois","IL");
        states.put("Indiana","IN");
        states.put("Iowa","IA");
        states.put("Kansas","KS");
        states.put("Kentucky","KY");
        states.put("Louisiana","LA");
        states.put("Maine","ME");
        states.put("Manitoba","MB");
        states.put("Maryland","MD");
        states.put("Massachusetts","MA");
        states.put("Michigan","MI");
        states.put("Minnesota","MN");
        states.put("Mississippi","MS");
        states.put("Missouri","MO");
        states.put("Montana","MT");
        states.put("Nebraska","NE");
        states.put("Nevada","NV");
        states.put("New Brunswick","NB");
        states.put("New Hampshire","NH");
        states.put("New Jersey","NJ");
        states.put("New Mexico","NM");
        states.put("New York","NY");
        states.put("Newfoundland","NF");
        states.put("North Carolina","NC");
        states.put("North Dakota","ND");
        states.put("Northwest Territories","NT");
        states.put("Nova Scotia","NS");
        states.put("Nunavut","NU");
        states.put("Ohio","OH");
        states.put("Oklahoma","OK");
        states.put("Ontario","ON");
        states.put("Oregon","OR");
        states.put("Pennsylvania","PA");
        states.put("Prince Edward Island","PE");
        states.put("Puerto Rico","PR");
        states.put("Quebec","PQ");
        states.put("Rhode Island","RI");
        states.put("Saskatchewan","SK");
        states.put("South Carolina","SC");
        states.put("South Dakota","SD");
        states.put("Tennessee","TN");
        states.put("Texas","TX");
        states.put("Utah","UT");
        states.put("Vermont","VT");
        states.put("Virgin Islands","VI");
        states.put("Virginia","VA");
        states.put("Washington","WA");
        states.put("West Virginia","WV");
        states.put("Wisconsin","WI");
        states.put("Wyoming","WY");
        states.put("Yukon Territory","YT");

        StringBuilder strReturnedAddress = new StringBuilder("");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String state = "NA";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                mapModel = new MapModel();
                Address returnedAddress = addresses.get(0);
                String address = returnedAddress.getAddressLine(0);
                String city = returnedAddress.getLocality();
                String fullState = returnedAddress.getAdminArea();
                if (fullState != null && fullState.length() > 1) {
                    state = states.get(fullState.trim());
                }
                String postalCode = returnedAddress.getPostalCode();
                mapModel.setAddress(address);
                mapModel.setCity(city);
                mapModel.setState(state);
                mapModel.setPostalCode(postalCode);
                mapModel.setLatitude(latitude);
                mapModel.setLongitude(longitude);
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                }
                mapModel.setLocation(strReturnedAddress.toString());
                Log.d(TAG, "" + strReturnedAddress.toString());
            } else {
                Log.d(TAG, "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Cannot get Address!\t" + e.getMessage());
        }
        return mapModel;
    }


    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }


}
