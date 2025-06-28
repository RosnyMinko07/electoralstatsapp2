package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.electoralstatsapp.R;

public class Fragment_circonscription extends Fragment {

    public Fragment_circonscription() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_circonscription, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup container = view.findViewById(R.id.circonscription_container);
        View.OnClickListener listener = v -> navigateTo(new Fragment_liste_bureau());

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            child.setOnClickListener(listener);
        }
    }

    private void navigateTo(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
