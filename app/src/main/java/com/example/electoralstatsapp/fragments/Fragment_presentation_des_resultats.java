package com.example.electoralstatsapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.electoralstatsapp.R;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.Typeface;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.example.electoralstatsapp.sqlite.models.Candidat;
import java.util.List;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_presentation_des_resultats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_presentation_des_resultats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_presentation_des_resultats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_presentation_des_resultats.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_presentation_des_resultats newInstance(String param1, String param2) {
        Fragment_presentation_des_resultats fragment = new Fragment_presentation_des_resultats();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_presentation_des_resultats, container, false);

        LinearLayout candidatsContainer = view.findViewById(R.id.candidats_container);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        List<Candidat> candidats = dbHelper.getAllCandidats();

        // Affichage du candidat en tête (plus grand pourcentage réel)
        Candidat enTete = null;
        float maxPct = -1f;
        for (Candidat c : candidats) {
            float pct = dbHelper.getPourcentageForCandidat(c.getId());
            if (pct > maxPct) {
                maxPct = pct;
                enTete = c;
            }
        }
        if (enTete != null) {
            View topCard = createCandidatCard(inflater, enTete, container, dbHelper);
            TextView titre = new TextView(getContext());
            titre.setText("Candidat en tête :");
            titre.setTextSize(20);
            titre.setTypeface(null, android.graphics.Typeface.BOLD);
            candidatsContainer.addView(titre, 0);
            candidatsContainer.addView(topCard, 1);
        } else {
            Toast.makeText(getContext(), "Aucun résultat saisi.", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < candidats.size(); i += 2) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setPadding(0, 0, 0, 24);

            row.addView(createCandidatCard(inflater, candidats.get(i), container, dbHelper));
            if (i + 1 < candidats.size()) {
                row.addView(createCandidatCard(inflater, candidats.get(i + 1), container, dbHelper));
            }
            candidatsContainer.addView(row);
        }
        return view;
    }

    private View createCandidatCard(LayoutInflater inflater, Candidat candidat, ViewGroup container, DatabaseHelper dbHelper) {
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        params.setMargins(8, 0, 8, 0);
        card.setLayoutParams(params);
        card.setGravity(Gravity.CENTER_HORIZONTAL);
        card.setBackgroundResource(R.drawable.circle_card);
        card.setPadding(12, 12, 12, 12);

        ImageView img = new ImageView(getContext());
        int resId = getResources().getIdentifier(candidat.getPhoto(), "drawable", getContext().getPackageName());
        img.setImageResource(resId != 0 ? resId : R.drawable.profilhomme);
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(128, 128);
        img.setLayoutParams(imgParams);
        img.setBackgroundResource(R.drawable.circle_bg);
        card.addView(img);

        TextView nom = new TextView(getContext());
        nom.setText(candidat.getPrenom() + " " + candidat.getNom());
        nom.setTypeface(null, Typeface.BOLD);
        nom.setGravity(Gravity.CENTER_HORIZONTAL);
        card.addView(nom);

        TextView pourcentage = new TextView(getContext());
        float pct = dbHelper.getPourcentageForCandidat(candidat.getId());
        pourcentage.setText("Pourcentage: " + String.format("%.2f", pct) + "%");
        pourcentage.setGravity(Gravity.CENTER_HORIZONTAL);
        card.addView(pourcentage);

        return card;
    }
}