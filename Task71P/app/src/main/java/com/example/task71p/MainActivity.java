package com.example.task71p;

import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private Spinner spinnerCategory;
    private List<Post> currentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        spinnerCategory = findViewById(R.id.spinner_category);
        FloatingActionButton fab = findViewById(R.id.fab_add);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup category filter spinner
        String[] categories = {"All", "Electronics", "Pets", "Wallets", "Keys", "Others"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                loadPosts(selected.equals("All") ? null : selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        fab.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddPostActivity.class));
        });
    }

    private void loadPosts(String categoryFilter) {
        currentList = dbHelper.getAllPosts(categoryFilter);
        if (adapter == null) {
            adapter = new PostAdapter(currentList, postId -> {
                dbHelper.deletePost(postId);
                // Reload with current filter
                String filter = spinnerCategory.getSelectedItem().toString();
                loadPosts(filter.equals("All") ? null : filter);
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(currentList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPosts(spinnerCategory.getSelectedItem().toString().equals("All") ? null : spinnerCategory.getSelectedItem().toString());
    }
}