package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.LocationsMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class OrganizerLocationsMap extends AppCompatActivity implements OnMapReadyCallback {
    private LocationsMapBinding binding;
    private Context context;
    private Event event;
    private EntrantDB entrantDB;
    private ArrayList<Entrant> entrantList = new ArrayList<>();
    private  MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LocationsMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        Intent in =  getIntent();
        try {
            event = in.getParcelableExtra("event");
        } catch (Exception e) {
            Log.d("OrganizerLocationsMap", "event extra was not passed correctly");
            throw new RuntimeException(e);
        }

        mapView = binding.entrantsMap;

        entrantDB = new EntrantDB();
        entrantDB.getEntrantList(event.getWaitList()).thenAccept(entrants -> {
            if(entrants != null){
                entrantList = entrants;
                Log.d("setMap", "WaitList loaded");
            } else {
                Log.d("setMap", "No entrants in waitlist");
            }
            mapView.getMapAsync(this);
            mapView.onCreate(savedInstanceState);
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        entrantList.add(new Entrant());

        double latitudeSum = 0;
        double longitudeSum = 0;
        for(Entrant entrant : entrantList) {
            latitudeSum += entrant.getLocationPoint().latitude;
            longitudeSum += entrant.getLocationPoint().longitude;
            createMarker(googleMap, entrant);
        }
        int n = entrantList.size();
        LatLng centerPoint = new LatLng(latitudeSum/n,longitudeSum/n);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 10));
    }

    public void createMarker(GoogleMap googleMap, Entrant entrant) {
        LatLng markerPoint = entrant.getLocationPoint();
        String entrantName = entrant.getNameAsString();
        String address = entrant.getAddress();
        googleMap.addMarker(new MarkerOptions()
                .position(markerPoint)
                .title(entrantName)
                .snippet(address)
        );
    }
}
