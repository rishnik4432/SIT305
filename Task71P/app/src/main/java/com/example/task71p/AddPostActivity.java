package com.example.task71p;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

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
    private byte[] selectedImageBytes = null;

    private final int[] testDrawables = {
            R.drawable.airpods_pro_3_blue,
            R.drawable.germ_shep_collar,
            R.drawable.louis_vuitton_wallet,
            R.drawable.pandora_wedding_ring,
            R.drawable.porsche_car_keys
    };

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        // resize to avoid memory issues
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                        ivPreview.setImageBitmap(scaled);
                        selectedImageBytes = getBytesFromBitmap(scaled);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                    }
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

        btnUpload.setOnClickListener(v -> showTestImageDialog());
        btnSave.setOnClickListener(v -> savePost());
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

        String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        Post post = new Post(title, desc, category, type, selectedImageBytes, currentDateTime);
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

    private void showTestImageDialog() {
        String[] imageNames = {"airpods", "collar", "wallet", "ring", "keys"}; // same order as drawables array
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a test image");

        // Use a custom adapter to show small previews (optional but nice)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, imageNames);
        builder.setAdapter(adapter, (dialog, which) -> {
            // which is the index in testDrawables array
            int drawableId = testDrawables[which];
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
            // Resize to avoid memory issues
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            ivPreview.setImageBitmap(scaled);
            selectedImageBytes = getBytesFromBitmap(scaled);
            Toast.makeText(this, "Image selected: " + imageNames[which], Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}