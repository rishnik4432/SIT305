package com.example.task91p;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class AddPostActivity extends AppCompatActivity {
    private EditText etTitle, etDescription;
    private Spinner spinnerType, spinnerCategory;
    private ImageView ivPreview;
    private Button btnUpload, btnSave;
    private TextView tvSelectedLocation;
    private byte[] selectedImageBytes = null;
    private double selectedLat = 0, selectedLng = 0;
    private String selectedAddress = "";

    private final int[] testDrawables = {R.drawable.louis_vuitton_wallet, R.drawable.pandora_wedding_ring, R.drawable.airpods_pro_3_blue, R.drawable.porsche_car_keys};

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                        ivPreview.setImageBitmap(scaled);
                        selectedImageBytes = getBytesFromBitmap(scaled);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> locationPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedLat = result.getData().getDoubleExtra("lat", 0);
                    selectedLng = result.getData().getDoubleExtra("lng", 0);
                    selectedAddress = result.getData().getStringExtra("address");
                    tvSelectedLocation.setText(selectedAddress);
                    Log.d("AddPost", "Location received: " + selectedLat + ", " + selectedLng + " - " + selectedAddress);
                    Toast.makeText(this, "Location set: " + selectedLat + "," + selectedLng, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No location selected", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        spinnerType = findViewById(R.id.spinner_type);
        spinnerCategory = findViewById(R.id.spinner_category);
        ivPreview = findViewById(R.id.iv_preview);
        btnUpload = findViewById(R.id.btn_upload_image);
        btnSave = findViewById(R.id.btn_save);
        tvSelectedLocation = findViewById(R.id.tv_selected_location);

        tvSelectedLocation.setOnClickListener(v -> {
            Intent intent = new Intent(AddPostActivity.this, LocationPickerActivity.class);
            locationPickerLauncher.launch(intent);
        });

        btnUpload.setOnClickListener(v -> showImageSourceDialog());
        btnSave.setOnClickListener(v -> savePost());
    }

    private void showImageSourceDialog() {
        String[] options = {"Choose from test images", "Open gallery"};
        new AlertDialog.Builder(this)
                .setTitle("Select image source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) showTestImageDialog();
                    else openGallery();
                })
                .show();
    }

    private void showTestImageDialog() {
        String[] names = {"louis vuitton wallet", "pandora wedding ring", "airpods pro 3", "porsche car keys"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a test image");
        builder.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names), (dialog, which) -> {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), testDrawables[which]);
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            ivPreview.setImageBitmap(scaled);
            selectedImageBytes = getBytesFromBitmap(scaled);
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void savePost() {
        String title = etTitle.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Please fill title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImageBytes == null) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(selectedAddress)) {
            Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        Post post = new Post(title, desc, category, type, selectedImageBytes, currentDateTime, selectedLat, selectedLng);
        DatabaseHelper db = new DatabaseHelper(this);
        boolean inserted = db.addPost(post);
        if (inserted) {
            Toast.makeText(this, "Post added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error, please try again", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }
}
