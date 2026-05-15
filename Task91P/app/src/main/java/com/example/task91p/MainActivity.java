package com.example.task91p;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private Spinner spinnerCategory;
    private List<Post> currentList;
    private FusedLocationProviderClient fusedLocationClient;
    private EditText etRadius;
    private Button btnRadiusSearch;
    private static final int LOCATION_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        spinnerCategory = findViewById(R.id.spinner_category);
        FloatingActionButton fab = findViewById(R.id.fab_add);
        Button btnShowMap = findViewById(R.id.btn_show_map);
        etRadius = findViewById(R.id.et_radius);
        btnRadiusSearch = findViewById(R.id.btn_radius_search);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup category filter
        String[] categories = {"All", "Electronics", "Pets", "Wallets", "Keys", "Others"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);
        spinnerCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                String selected = categories[position];
                loadPosts(selected.equals("All") ? null : selected);
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddPostActivity.class)));
        btnShowMap.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapsActivity.class)));

        btnRadiusSearch.setOnClickListener(v -> {
            String radiusStr = etRadius.getText().toString().trim();
            if (radiusStr.isEmpty()) {
                Toast.makeText(this, "Enter radius in km", Toast.LENGTH_SHORT).show();
                return;
            }
            double radiusKm = Double.parseDouble(radiusStr);
            searchWithinRadius(radiusKm);
        });
    }

    private void loadPosts(String categoryFilter) {
        currentList = dbHelper.getAllPosts(categoryFilter);
        if (adapter == null) {
            adapter = new PostAdapter(currentList, postId -> {
                dbHelper.deletePost(postId);
                loadPosts(spinnerCategory.getSelectedItem().toString().equals("All") ? null : spinnerCategory.getSelectedItem().toString());
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(currentList);
        }
    }

    private void searchWithinRadius(double radiusKm) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            double userLat, userLon;
            if (location == null) {
                userLat = -37.8476;
                userLon = 145.1149;
                Toast.makeText(this, "Using default location (Deakin University)", Toast.LENGTH_SHORT).show();
            } else {
                userLat = location.getLatitude();
                userLon = location.getLongitude();
            }
            performRadiusSearch(userLat, userLon, radiusKm);
        });
    }

    private void performRadiusSearch(double userLat, double userLon, double radiusKm) {
        List<Post> allPosts = dbHelper.getAllPosts(null);
        List<Post> filtered = new ArrayList<>();
        for (Post post : allPosts) {
            // Skip posts with no valid coordinates
            if (post.getLatitude() == 0 && post.getLongitude() == 0) {
                Log.d("RadiusSearch", "Post " + post.getTitle() + " has no coordinates, skipping");
                continue;
            }
            float[] results = new float[1];
            Location.distanceBetween(userLat, userLon, post.getLatitude(), post.getLongitude(), results);
            float distanceKm = results[0] / 1000;
            if (distanceKm <= radiusKm) {
                filtered.add(post);
            }
        }
        if (adapter == null) {
            adapter = new PostAdapter(filtered, postId -> {
                dbHelper.deletePost(postId);
                loadPosts(spinnerCategory.getSelectedItem().toString().equals("All") ? null : spinnerCategory.getSelectedItem().toString());
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(filtered);
        }
        if (filtered.isEmpty()) {
            Toast.makeText(this, "No posts within " + radiusKm + " km", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Found " + filtered.size() + " posts within " + radiusKm + " km", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPosts(spinnerCategory.getSelectedItem().toString().equals("All") ? null : spinnerCategory.getSelectedItem().toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String radiusStr = etRadius.getText().toString().trim();
            if (!radiusStr.isEmpty()) {
                searchWithinRadius(Double.parseDouble(radiusStr));
            }
        } else {
            Toast.makeText(this, "Location permission needed for radius search", Toast.LENGTH_SHORT).show();
        }
    }
}