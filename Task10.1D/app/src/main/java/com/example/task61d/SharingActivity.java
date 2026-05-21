package com.example.task61d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.File;
import java.io.FileOutputStream;
public class SharingActivity extends AppCompatActivity{
    private TextView tvProfileInfo;
    private ImageView ivQrCode;
    private Button btnShare;
    private SharedPreferences prefs;
    private Bitmap qrBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvProfileInfo = findViewById(R.id.tvProfileInfo);
        ivQrCode = findViewById(R.id.ivQrCode);
        btnShare = findViewById(R.id.btnShare);

        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "Student");
        String interests = prefs.getString("interests", "Math, Science");
        String history = prefs.getString("history", "Algebra");

        String profileText = "Name: " + name + "\nInterests: " + interests + "\nStudied: " + history;
        tvProfileInfo.setText(profileText);

        // Generate QR code from profile data
        String qrData = "LearningAssistantApp://profile?name=" + name + "&interests=" + interests + "&history=" + history;
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder encoder = new BarcodeEncoder();
            qrBitmap = encoder.createBitmap(matrix);
            ivQrCode.setImageBitmap(qrBitmap);
        } catch (Exception e) {
            ivQrCode.setImageResource(android.R.drawable.ic_dialog_alert);
            Toast.makeText(this, "QR failed", Toast.LENGTH_SHORT).show();
        }

        btnShare.setOnClickListener(v -> shareQrCode());
    }

    private void shareQrCode() {
        if (qrBitmap == null) return;
        // Save bitmap to cache
        File cachePath = new File(getCacheDir(), "qr_code.png");
        try (FileOutputStream stream = new FileOutputStream(cachePath)) {
            qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Uri uri = androidx.core.content.FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", cachePath);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
        } catch (Exception e) {
            Toast.makeText(this, "Share failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
