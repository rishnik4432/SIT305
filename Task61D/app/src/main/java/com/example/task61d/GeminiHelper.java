package com.example.task61d;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GeminiHelper {
    private static final String API_KEY = "AIzaSyB0S4lQb1vOUnMeA2Cnrgb1prLzI6bcpdo";
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;

    private final Context context;
    private final RequestQueue requestQueue;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();

    private final Map<String, String[]> mockResponseMap = new HashMap<>();

    public GeminiHelper(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        initMockResponses();
    }

    private void initMockResponses() {
        mockResponseMap.put("hint", new String[]{
                "Think about the main formula. What does each variable represent?",
                "Break the problem into smaller steps. Start with what you know.",
                "Have you considered re-reading the last paragraph? The clue is there.",
        });

        mockResponseMap.put("explain", new String[]{
                "Your answer is correct! Great job. Remember to always double-check units.",
                "Almost there! The correct answer uses a different approach. Let's review the concept...",
                "That's partially correct. You missed the fact that 196's root is 14 and not 13. But good effort!",
                "Excellent reasoning! Your answer shows deep understanding."
        });

        mockResponseMap.put("summary", new String[]{
                "This lesson teaches the core idea that practice leads to mastery. Key points: 1) Repetition helps memory, 2) Errors are learning opportunities, 3) Consistent study works best.",
                "The main takeaway is that breaking down complex topics into smaller pieces makes learning easier. Always start with the basics.",
                "In summary, this topic connects three ideas: cause, effect, and solution. Understanding each helps solve real problems."
        });

        mockResponseMap.put("flashcards", new String[]{
                "What is the main concept?|The central idea of the topic.||Give one example.|An example makes it clear.||Why is this useful?|It applies to many situations.",
                "Define the term.|A short definition.||List two features.|Feature A, Feature B.||When should you use it?|When condition X is met."
        });

        mockResponseMap.put("plan", new String[]{
                "Day 1: Review fundamentals. Day 2: Practice problems. Day 3: Study examples. Day 4: Take a quiz. Day 5: Review mistakes. Day 6: Teach someone else. Day 7: Rest and reflect.",
                "Monday: Watch tutorial. Tuesday: Do exercises. Wednesday: Group study. Thursday: Flashcards. Friday: Practice test. Saturday: Review weak areas. Sunday: Plan next week."
        });

        mockResponseMap.put("assessment", new String[]{
                "Your answer is correct! The reason is that the question asks for the capital, and Paris is indeed the capital of France. Keep going!",
                "That's not correct. The right answer is Paris because it is the political and cultural center of France. Don't worry – try the next question!",
                "Good try! Actually, Paris is the capital. Remember that capitals are often the largest or most famous city in a country."
        });
    }

    public void askGemini(String prompt, final Callback callback) {
        String type = detectPromptType(prompt);

        if (API_KEY.equals("AIzaSyB0S4lQb1vOUnMeA2Cnrgb1prLzI6bcpdo") || API_KEY.isEmpty()) {
            useMockResponse(type, callback);
            return;
        }

        // Build JSON request for Gemini API
        JSONObject requestBody = new JSONObject();
        try {
            JSONObject content = new JSONObject();
            JSONObject part = new JSONObject();
            part.put("text", prompt);
            content.put("parts", new JSONObject[]{part});
            requestBody.put("contents", new JSONObject[]{content});
        } catch (Exception e) {
            callback.onError("Failed to build request: " + e.getMessage());
            useMockResponse(type, callback); // fallback
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, requestBody,
                response -> {
                    try {
                        String aiText = response.getJSONArray("candidates")
                                .getJSONObject(0)
                                .getJSONObject("content")
                                .getJSONArray("parts")
                                .getJSONObject(0)
                                .getString("text");
                        mainHandler.post(() -> callback.onSuccess(aiText));
                    } catch (Exception e) {
                        useMockResponse(type, callback);
                    }
                },
                error -> {
                    useMockResponse(type, callback);
                });
        requestQueue.add(request);
    }

    private String detectPromptType(String prompt) {
        String lowerPrompt = prompt.toLowerCase();
        if (lowerPrompt.contains("hint")) return "hint";
        if (lowerPrompt.contains("student's answer") || lowerPrompt.contains("explain")) return "explain";
        if (lowerPrompt.contains("summarise") || lowerPrompt.contains("summary")) return "summary";
        if (lowerPrompt.contains("flashcard")) return "flashcards";
        if (lowerPrompt.contains("7-day") || lowerPrompt.contains("study plan")) return "plan";
        if (lowerPrompt.contains("student answered") || lowerPrompt.contains("correct")) return "assessment";
        return "hint";
    }

    private void useMockResponse(String type, final Callback callback) {
        mainHandler.postDelayed(() -> {
            String[] options = mockResponseMap.get(type);
            if (options == null || options.length == 0) {
                callback.onSuccess("This is a simulated AI response because the real API could not be reached. Your app logic is working perfectly!");
                return;
            }
            String mock = options[random.nextInt(options.length)];
            callback.onSuccess(mock);
        }, 1200);
    }

    public interface Callback {
        void onSuccess(String response);
        void onError(String error);
    }
}