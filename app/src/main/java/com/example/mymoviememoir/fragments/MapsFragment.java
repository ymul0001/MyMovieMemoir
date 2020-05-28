package com.example.mymoviememoir.fragments;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mymoviememoir.R;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMaps;
    private MapView mapView;
    private double userLongitude;
    private double userLatitude;
    private ArrayList<Double> cinemaLatitude = new ArrayList<>();
    private ArrayList<Double> cinemaLongitude = new ArrayList<>();
    private ArrayList<String> cinemaNames;
    private MarkerOptions mo = new MarkerOptions();
    NetworkConnection networkConnection = null;

    public MapsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_fragment, container, false);
        networkConnection = new NetworkConnection();
        cinemaNames = new ArrayList<>();
        GetCinemaLocationTask getCinemaLocationTask = new GetCinemaLocationTask();
        GetPersonLocationTask getPersonLocationTask = new GetPersonLocationTask();
        getPersonLocationTask.execute();
        getCinemaLocationTask.execute();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.app_map);
        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    //triggering all map views after map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMaps = googleMap;
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this.getContext());
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 =  new LatLng(location.getLatitude(), location.getLongitude());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    //method for getting person's location from the database as address, and will convert it to coordinates
    private class GetPersonLocationTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            JSONObject personObj = null;
            String address = "";
            String state = "";
            int postcode = 0;
            SharedPreferences sf = getContext().getSharedPreferences("dashboardPreferences", MODE_PRIVATE);
            int personId = sf.getInt("personId", 0);
            String personResult = networkConnection.getPersonById(personId);
            try {
                personObj = new JSONObject(personResult);
                address = personObj.getString("personAddress");
                state = personObj.getString("personState");
                postcode = personObj.getInt("personPostcode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LatLng coordinates = getLocationFromAddress(address + state + postcode);

            userLatitude = coordinates.latitude;
            userLongitude = coordinates.longitude;
            Log.d("string", userLatitude + "," + userLongitude);
            return "person location has been retrieved";
        }

        @Override
        protected void onPostExecute(String message) {
            LatLng userLocation = new LatLng(userLatitude, userLongitude);
            if (userLocation != null){
                Log.d("string", userLocation.toString());
                mo.position(userLocation).title("My location!");
                gMaps.addMarker(mo);
                float zoomLevel = (float) 15.0;
                gMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,
                        zoomLevel));
                Toast.makeText(getContext(), message,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    //method for getting cinema's location from the database as address, and will convert it to coordinates
    private class GetCinemaLocationTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void...params){
            String result = networkConnection.getAllCinemas();
            Log.d("string",result);
            JSONArray cinemaArray = null;
            try {
                cinemaArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < cinemaArray.length(); i++){
                JSONObject cinemaObj = null;
                try {
                    cinemaObj = cinemaArray.getJSONObject(i);
                    String fullAddress = cinemaObj.getString("cinemaAddress");
                    LatLng coordinates = getLocationFromAddress(fullAddress);
                    cinemaLatitude.add(coordinates.latitude);
                    cinemaLongitude.add(coordinates.longitude);
                    cinemaNames.add(cinemaObj.getString("cinemaName"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return "All cinema location have been retrieved";
        }

        @Override
        protected void onPostExecute(String message){
            ArrayList<LatLng> cinemaCoordinates = new ArrayList<>();
            for(int i = 0; i < cinemaLatitude.size(); i++)
            {
                cinemaCoordinates.add(new LatLng(cinemaLatitude.get(i), cinemaLongitude.get(i)));
            };
            for(LatLng coor: cinemaCoordinates){
                mo.position(coor).title(cinemaNames.get(cinemaCoordinates.indexOf(coor))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                gMaps.addMarker(mo);
            }
            Toast.makeText(getContext(), message,
                    Toast.LENGTH_LONG).show();
        }
    }

}
