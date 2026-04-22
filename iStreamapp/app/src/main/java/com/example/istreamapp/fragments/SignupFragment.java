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

public class SignupFragment extends Fragment {
    private EditText fullNameEdit, usernameEdit, passwordEdit, confirmEdit;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        db = AppDatabase.getInstance(getContext());

        fullNameEdit = view.findViewById(R.id.fullNameEditText);
        usernameEdit = view.findViewById(R.id.usernameEditText);
        passwordEdit = view.findViewById(R.id.passwordEditText);
        confirmEdit = view.findViewById(R.id.confirmPasswordEditText);
        Button signupButton = view.findViewById(R.id.signupButton);
        Button gotoLogin = view.findViewById(R.id.gotoLoginButton);

        signupButton.setOnClickListener(v -> signup());
        gotoLogin.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.loginFragment));

        return view;
    }

    private void signup() {
        String fullName = fullNameEdit.getText().toString().trim();
        String username = usernameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String confirm = confirmEdit.getText().toString().trim();

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirm)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            User existing = db.userDao().getUserByUsername(username);
            if (existing != null) {
                getActivity().runOnUiThread(() -> Toast.makeText(getContext(), "Username already exists", Toast.LENGTH_SHORT).show());
                return;
            }
            User newUser = new User(fullName, username, password);
            db.userDao().insert(newUser);
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Account created! Please login", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this).navigate(R.id.loginFragment);
            });
        }).start();
    }
}