package com.example.electoralstatsapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.electoralstatsapp.MainActivity;
import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;

import java.util.Map;

public class LoginFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView goToSignupTextView;
    private RadioGroup roleRadioGroup;
    private DatabaseHelper db;

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNav();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNav();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        db = new DatabaseHelper(getContext());
        emailEditText = view.findViewById(R.id.et_login_email);
        passwordEditText = view.findViewById(R.id.et_login_password);
        loginButton = view.findViewById(R.id.btn_login);
        goToSignupTextView = view.findViewById(R.id.tv_go_to_signup);
        roleRadioGroup = view.findViewById(R.id.rg_roles);
        final View progressBar = view.findViewById(R.id.progress_login_loading);

        loginButton.setOnClickListener(v -> {
            if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String role = "";

            int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
            if (selectedRoleId != -1) {
                RadioButton selectedRadioButton = view.findViewById(selectedRoleId);
                role = selectedRadioButton.getText().toString();
            }

            Log.d("LoginAttempt", "Email: " + email + " | Pass: " + password + " | Role: " + role);

            if (email.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs et sélectionner un rôle", Toast.LENGTH_SHORT).show();
                if (progressBar != null) progressBar.setVisibility(View.GONE);
                return;
            }

            Map<String, String> userDetails = db.checkUser(email, password);

            if (userDetails != null) {
                Log.d("LoginAttempt", "User found in DB. Role: " + userDetails.get(DatabaseHelper.KEY_USER_ROLE));
            } else {
                Log.d("LoginAttempt", "User NOT found in DB for this email/password.");
            }

            if (userDetails != null && userDetails.get(DatabaseHelper.KEY_USER_ROLE).equalsIgnoreCase(role)) {
                Log.d("LoginAttempt", "SUCCESS: Roles match.");
                Toast.makeText(getContext(), "Connexion réussie", Toast.LENGTH_SHORT).show();

                // Store user info in SharedPreferences
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("user_id", Integer.parseInt(userDetails.get(DatabaseHelper.KEY_ID)));
                editor.putString("user_role", userDetails.get(DatabaseHelper.KEY_USER_ROLE));
                String fullName = userDetails.get(DatabaseHelper.KEY_USER_FULL_NAME);
                if (fullName == null) fullName = "Utilisateur";
                editor.putString("user_name", fullName);
                editor.putBoolean("is_logged_in", true);
                editor.apply();

                // Navigate to the results fragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new Fragment_resultats_des_elections());
                transaction.commit();
                if (progressBar != null) progressBar.setVisibility(View.GONE);

            } else {
                Log.d("LoginAttempt", "FAILURE: userDetails is null or roles do not match.");
                Toast.makeText(getContext(), "Email, mot de passe ou rôle incorrect", Toast.LENGTH_SHORT).show();
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
        });

        goToSignupTextView.setOnClickListener(v -> {
            // Navigate to SignupFragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new SignupFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
} 