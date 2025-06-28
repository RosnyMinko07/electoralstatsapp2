package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.DatePickerDialog;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.example.electoralstatsapp.sqlite.models.CentreDeVote;
import com.example.electoralstatsapp.sqlite.models.Circonscription;
import com.example.electoralstatsapp.sqlite.models.Election;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_saisie_resultats#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_saisie_resultats extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseHelper db;
    private Spinner electionSpinner, circonscriptionSpinner, communeSpinner;
    private EditText etNombreVoix, etPourcentage, etDateSaisie;
    private Button btnDateSaisie, btnVoirResultats;
    private Calendar calendarSaisie = Calendar.getInstance();

    public Fragment_saisie_resultats() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_saisie_resultats.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_saisie_resultats newInstance(String param1, String param2) {
        Fragment_saisie_resultats fragment = new Fragment_saisie_resultats();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saisie_resultats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String userRole = prefs.getString("user_role", "");
        if (!"opérateur".equalsIgnoreCase(userRole)) {
            view.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Accès réservé à l'opérateur.", Toast.LENGTH_LONG).show();
            return;
        }

        db = new DatabaseHelper(getContext());

        electionSpinner = view.findViewById(R.id.spinner_type_election);
        circonscriptionSpinner = view.findViewById(R.id.spinner_circonscription);
        communeSpinner = view.findViewById(R.id.spinner_commune);
        etNombreVoix = view.findViewById(R.id.et_nombre_voix);
        etPourcentage = view.findViewById(R.id.et_pourcentage);
        etDateSaisie = view.findViewById(R.id.et_date_saisie);
        btnDateSaisie = view.findViewById(R.id.btn_date_saisie);
        btnVoirResultats = view.findViewById(R.id.btn_voir_resultats);

        loadElectionSpinner();
        loadCirconscriptionSpinner();

        circonscriptionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Circonscription selectedCirconscription = (Circonscription) parent.getItemAtPosition(position);
                loadCommuneSpinner(selectedCirconscription.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                communeSpinner.setAdapter(null);
            }
        });

        // Initialiser la date de saisie à aujourd'hui
        updateDateSaisieField();
        btnDateSaisie.setOnClickListener(v -> showDatePicker());
        btnVoirResultats.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new Fragment_presentation_des_resultats())
                .addToBackStack(null)
                .commit();
        });
    }

    private void loadElectionSpinner() {
        List<Election> elections = db.getAllElections();
        ArrayAdapter<Election> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, elections);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        electionSpinner.setAdapter(adapter);
    }

    private void loadCirconscriptionSpinner() {
        List<Circonscription> circonscriptions = db.getAllCirconscriptions();
        ArrayAdapter<Circonscription> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, circonscriptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        circonscriptionSpinner.setAdapter(adapter);
    }

    private void loadCommuneSpinner(long circonscriptionId) {
        List<CentreDeVote> centres = db.getCentresByCirconscription(circonscriptionId);
        ArrayAdapter<CentreDeVote> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, centres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        communeSpinner.setAdapter(adapter);
    }

    private void showDatePicker() {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendarSaisie.set(Calendar.YEAR, year);
            calendarSaisie.set(Calendar.MONTH, month);
            calendarSaisie.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateSaisieField();
        },
        calendarSaisie.get(Calendar.YEAR),
        calendarSaisie.get(Calendar.MONTH),
        calendarSaisie.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateSaisieField() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etDateSaisie.setText(sdf.format(calendarSaisie.getTime()));
    }
}