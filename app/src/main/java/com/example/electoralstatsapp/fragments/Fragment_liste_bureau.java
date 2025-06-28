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

import com.example.electoralstatsapp.R;

public class Fragment_liste_bureau extends Fragment {

    public Fragment_liste_bureau() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_liste_bureau, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroup container = view.findViewById(R.id.liste_bureau_container);
        View.OnClickListener listener = v -> navigateTo(new ResultEntryFragment());

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof ViewGroup) {
                ViewGroup bureauLayout = (ViewGroup) child;
                // The "Afficher" button is the first button in the layout
                for (int j = 0; j < bureauLayout.getChildCount(); j++) {
                    View innerChild = bureauLayout.getChildAt(j);
                    if (innerChild instanceof Button && "Afficher".equals(((Button) innerChild).getText().toString())) {
                        innerChild.setOnClickListener(listener);
                        break; // Found the button, move to the next bureau item
                    }
                }
            }
        }
    }

    private void navigateTo(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
