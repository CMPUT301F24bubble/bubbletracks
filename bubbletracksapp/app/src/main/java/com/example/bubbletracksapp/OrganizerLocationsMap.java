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

/**
 * Creates a map with the locations of the entrants of the given event.
 * @author Chester
 */
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

    /**
     * This function will run when the map is created and will populate it with
     * markers that show the geolocations of the entrants that joined.
     * @param googleMap The google map that will be populated with the Entrant's locations.
     * @author Chester
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        entrantList.add(new Entrant());

        double latitudeSum = 0;
        double longitudeSum = 0;
        for(Entrant entrant : entrantList) {
            latitudeSum += entrant.getGeolocation().latitude;
            longitudeSum += entrant.getGeolocation().longitude;
            createMarker(googleMap, entrant);
        }
        int n = entrantList.size();
        LatLng centerPoint = new LatLng(latitudeSum/n,longitudeSum/n);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 10));
    }

    /**
     * This method creates a marker in the given google map with the name of the
     * given Entrant as it's title and the specific address of the Entrant
     * as extra information.
     *
     * @param googleMap The Google Map where the marker will be created.
     * @param entrant The Entrant from which the name and the address will be retrieved.
     * @author Chester
     */
    public void createMarker(GoogleMap googleMap, Entrant entrant) {
        LatLng markerPoint = entrant.getGeolocation();
        String entrantName = entrant.getNameAsString();
        googleMap.addMarker(new MarkerOptions()
                .position(markerPoint)
                .title(entrantName)
                .snippet(String.format("Location (%f,%f)", markerPoint.latitude, markerPoint.longitude))
        );
    }
}
