package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.electoralstatsapp.MainActivity;
import com.example.electoralstatsapp.R;

public class WelcomeFragment extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
        // Hide the bottom navigation when this fragment is visible
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideBottomNav();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Ensure the bottom navigation is shown when leaving this fragment
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showBottomNav();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        Button commencerButton = view.findViewById(R.id.btn_commencer);

        commencerButton.setOnClickListener(v -> {
            // Navigate to LoginFragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new LoginFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
} 