package com.example.task61d;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PurchaseActivity extends AppCompatActivity {
    private Button btnPurchase;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnPurchase = findViewById(R.id.btnPurchase);
        tvStatus = findViewById(R.id.tvPurchaseStatus);

        btnPurchase.setText("Buy Now ($4.99)");
        btnPurchase.setEnabled(true);
        tvStatus.setText("Ready to purchase.");

        btnPurchase.setOnClickListener(v -> {
            btnPurchase.setEnabled(false);
            btnPurchase.setText("Processing...");
            tvStatus.setText("Processing payment...");

            new Handler().postDelayed(() -> {
                tvStatus.setText("Purchase successful! Premium features unlocked.");
                Toast.makeText(PurchaseActivity.this, "Thank you for your purchase!", Toast.LENGTH_LONG).show();
                getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().putBoolean("isPremium", true).apply();
                btnPurchase.setText("Buy Now!");
                btnPurchase.setEnabled(true);
            }, 5000);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
