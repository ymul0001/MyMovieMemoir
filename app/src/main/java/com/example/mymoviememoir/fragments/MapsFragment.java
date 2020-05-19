package com.example.mymoviememoir.fragments;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap gMaps;
    private MapView mapView;
    private double longitude;
    private double latitude;
    NetworkConnection networkConnection = null;

    public MapsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maps_fragment, container, false);
        networkConnection = new NetworkConnection();
        GetPersonLocationTask getPersonLocationTask = new GetPersonLocationTask();
        getPersonLocationTask.execute();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        LatLng userLocation = new LatLng(latitude, longitude);
        gMaps = googleMap;
        gMaps.addMarker(new MarkerOptions().position(userLocation).title("My location"));
        float zoomLevel = (float) 10.0;
        gMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,
                zoomLevel));
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this.getContext());
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            Log.d("String", String.valueOf(address.get(1).getLatitude()) + String.valueOf(address.get(1).getLongitude()));
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude() * 1E6, location.getLongitude()* 1E6);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }

    private class GetPersonLocationTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            JSONObject personObj = null;
            String address = "";
            SharedPreferences sf = getContext().getSharedPreferences("dashboardPreferences", Context.MODE_PRIVATE);
            int personId = sf.getInt("personId", 0);
            Log.d("string", "we have " + personId);
            String personResult = networkConnection.getPersonById(personId);
            Log.d("string", personResult);
            try {
                personObj = new JSONObject(personResult);
                address = personObj.getString("personAddress");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LatLng coordinates = getLocationFromAddress(address);
            String latitude  = String.valueOf(coordinates.latitude);
            String longitude = String.valueOf(coordinates.longitude);
            return latitude + "," + longitude;
        }

        @Override
        protected void onPostExecute(String coordinates) {
            latitude = Double.parseDouble(coordinates.split(",")[0]);
            longitude = Double.parseDouble(coordinates.split(",")[1]);
        }
    }
}
