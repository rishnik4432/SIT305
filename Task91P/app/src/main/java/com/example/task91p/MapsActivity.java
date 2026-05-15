package com.example.task91p;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        dbHelper = new DatabaseHelper(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Post> allPosts = dbHelper.getAllPosts(null);
        boolean anyMarker = false;
        LatLng lastValidLocation = null;
        for (Post post : allPosts) {
            if (post.getLatitude() != 0 || post.getLongitude()!= 0) {
                LatLng location = new LatLng(post.getLatitude(), post.getLongitude());
                String snippet = post.getType() + "-" + post.getCategory() + "\n" + post.getDescription();
                mMap.addMarker(new MarkerOptions().position(location).title(post.getTitle()).snippet(snippet));
                anyMarker = true;
                lastValidLocation = location;
                Log.d("MapsActivity", "Added marker for: " + post.getTitle() + " at " + post.getLatitude() + "," + post.getLongitude());
            } else {
                Log.d("MapsActivity", "Post " + post.getTitle() + " has no coordinates, not showing on map");
            }
        }
        if (anyMarker && lastValidLocation != null) {
            // Move camera to the first valid marker
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastValidLocation, 12));
        } else {
            // Default location if no markers (Deakin University)
            LatLng deakin = new LatLng(-37.8476, 145.1149);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deakin, 12));
            Toast.makeText(this, "No posts with location data to show", Toast.LENGTH_SHORT).show();
        }
    }
}
