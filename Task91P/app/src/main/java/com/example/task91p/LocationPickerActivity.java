package com.example.task91p;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;

public class LocationPickerActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private double selectedLat = 0, selectedLng = 0;
    private String selectedAddress = "";
    private static final int LOCATION_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_picker);

        // Initialize Places SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (place.getLatLng() != null) {
                    selectedLat = place.getLatLng().latitude;
                    selectedLng = place.getLatLng().longitude;
                    selectedAddress = place.getName();
                    Log.d("LocationPicker", "Place selected: " + selectedAddress + " (" + selectedLat + "," + selectedLng + ")");
                    Toast.makeText(LocationPickerActivity.this, "Location set: " + selectedAddress, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LocationPickerActivity.this, "Place has no coordinates", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(LocationPickerActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Button btnCurrent = findViewById(R.id.btn_current_location);
        Button btnConfirm = findViewById(R.id.btn_confirm_location);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        btnCurrent.setOnClickListener(v -> getCurrentLocation());
        btnConfirm.setOnClickListener(v -> {
            if (selectedLat != 0 || selectedLng != 0) {
                Intent result = new Intent();
                result.putExtra("lat", selectedLat);
                result.putExtra("lng", selectedLng);
                result.putExtra("address", selectedAddress);
                setResult(RESULT_OK, result);
                finish();
            } else {
                Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                selectedLat = location.getLatitude();
                selectedLng = location.getLongitude();
                selectedAddress = "Current Location";
                Toast.makeText(this, "Current location set: " + selectedLat + "," + selectedLng, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Could not get location. Try selecting a place manually.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
