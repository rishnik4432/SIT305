package com.example.task81c;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList = new ArrayList<>();

    private String username;
    private OkHttpClient client = new OkHttpClient();
    private final String BACKEND_URL = "http://10.0.2.2:3000/chat";

    private ChatDatabase db;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        username = getIntent().getStringExtra("USERNAME");
        if (username == null) username = "Guest";

        db = ChatDatabase.getInstance(this);

        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatAdapter(messageList);
        recyclerView.setAdapter(adapter);

        // Load chat history from Room
        loadChatHistory();

        buttonSend.setOnClickListener(v -> {
            String msg = editTextMessage.getText().toString().trim();
            if (msg.isEmpty()) {
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show();
                return;

            }
            // Add user message to UI and database
            ChatMessage userMsg = new ChatMessage(username, msg, true, System.currentTimeMillis());
            addMessageToUI(userMsg);
            saveMessageToDB(userMsg);

            editTextMessage.setText("");
            // Send to backend
            sendToBackend(msg);
        });
    }
    private void loadChatHistory() {
        executor.execute(() -> {
            List<ChatMessage> history = db.chatDao().getMessagesForUser(username);

            mainHandler.post(() -> {
                messageList.clear();
                messageList.addAll(history);
                adapter.updateList(messageList);
                if (messageList.size() > 0) {
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            });
        });
    }
    private void saveMessageToDB(ChatMessage message) {
        executor.execute(() -> db.chatDao().insert(message));
    }
    private void addMessageToUI(ChatMessage message) {
        adapter.addMessage(message);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }
    private void sendToBackend(String userMessage) {
        JSONObject json = new JSONObject();
        try {
            json.put("message", userMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(BACKEND_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mainHandler.post(() -> {
                        Toast.makeText(ChatActivity.this, "Network error: check backend", Toast.LENGTH_LONG).show();
                        //Show a bot error message
                        ChatMessage errorMsg = new ChatMessage(username, "Sorry, I couldn't reach the server.", false, System.currentTimeMillis());
                        saveMessageToDB(errorMsg);
                        addMessageToUI(errorMsg);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        String botReply = jsonResponse.getString("reply");
                        // Save bot reply
                        ChatMessage botMsg = new ChatMessage(username, botReply, false, System.currentTimeMillis());
                        saveMessageToDB(botMsg);
                        mainHandler.post(() -> addMessageToUI(botMsg));
                    } catch (Exception e) {
                        e.printStackTrace();
                        mainHandler.post(() -> Toast.makeText(ChatActivity.this, "Invalid response from server", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    mainHandler.post(() -> {
                        Toast.makeText(ChatActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                        //Fallback message
                        ChatMessage errorMsg = new ChatMessage(username, "The server returned an error.", false, System.currentTimeMillis());
                        saveMessageToDB(errorMsg);
                        addMessageToUI(errorMsg);
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}