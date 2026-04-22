package com.example.istreamapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.istreamapp.R;
import com.example.istreamapp.data.database.AppDatabase;
import com.example.istreamapp.data.database.User;
import com.example.istreamapp.utils.SessionManager;

public class LoginFragment extends Fragment {
    private EditText usernameEdit, passwordEdit;
    private AppDatabase db;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        db = AppDatabase.getInstance(getContext());
        sessionManager = new SessionManager(getContext());

        usernameEdit = view.findViewById(R.id.usernameEditText);
        passwordEdit = view.findViewById(R.id.passwordEditText);
        Button loginButton = view.findViewById(R.id.loginButton);
        Button gotoSignup = view.findViewById(R.id.gotoSignupButton);

        loginButton.setOnClickListener(v -> login());
        gotoSignup.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.signupFragment));

        return view;
    }

    private void login() {
        String username = usernameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User user = db.userDao().login(username, password);
            if (user != null) {
                sessionManager.saveUserId(user.getUserId());
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                    NavHostFragment.findNavController(this).navigate(R.id.homeFragment);
                });
            } else {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Invalid credentials", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}