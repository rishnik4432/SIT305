package com.example.task61d;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.task61d.database.AppDatabase;
import com.example.task61d.database.HistoryEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class HistoryActivity extends AppCompatActivity {
    private RecyclerView rvHistory;
    private Button btnClear;
    private HistoryAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvHistory = findViewById(R.id.rvHistory);
        btnClear = findViewById(R.id.btnClearHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        db = AppDatabase.getInstance(this);
        loadHistory();

        btnClear.setOnClickListener(v -> {
            db.historyDao().deleteAll();
            loadHistory();
        });
    }

    private void loadHistory() {
        new Thread(() -> {
            List<HistoryEntity> list = db.historyDao().getAll();
            runOnUiThread(() -> {
                adapter = new HistoryAdapter(list);
                rvHistory.setAdapter(adapter);
            });
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // Inner Adapter class
    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
        private List<HistoryEntity> list;
        private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

        HistoryAdapter(List<HistoryEntity> list) { this.list = list; }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            HistoryEntity e = list.get(position);
            holder.tvScreenName.setText(e.screenName);
            holder.tvTimestamp.setText(sdf.format(new Date(e.timestamp)));
            holder.tvUserInput.setText("You: " + e.userInput);
            holder.tvAiResponse.setText("AI: " + e.aiResponse);
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvScreenName, tvTimestamp, tvUserInput, tvAiResponse;
            ViewHolder(View v) {
                super(v);
                tvScreenName = v.findViewById(R.id.tvScreenName);
                tvTimestamp = v.findViewById(R.id.tvTimestamp);
                tvUserInput = v.findViewById(R.id.tvUserInput);
                tvAiResponse = v.findViewById(R.id.tvAiResponse);
            }
        }
    }
}
