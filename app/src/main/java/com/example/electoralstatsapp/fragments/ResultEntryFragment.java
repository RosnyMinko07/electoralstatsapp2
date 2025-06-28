package com.example.electoralstatsapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electoralstatsapp.R;
import com.example.electoralstatsapp.sqlite.DatabaseHelper;
import com.example.electoralstatsapp.sqlite.models.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ResultEntryFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private Spinner spinnerElection, spinnerCandidat, spinnerCirconscription, spinnerCentre, spinnerBureau;
    private EditText etVoix;
    private Button btnSave;

    private List<Election> electionList;
    private List<Candidat> candidatList;
    private List<Circonscription> circonscriptionList;
    private List<CentreDeVote> centreList;
    private List<BureauDeVote> bureauList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(getContext());
        
        spinnerElection = view.findViewById(R.id.spinner_election);
        spinnerCandidat = view.findViewById(R.id.spinner_candidat);
        spinnerCirconscription = view.findViewById(R.id.spinner_circonscription);
        spinnerCentre = view.findViewById(R.id.spinner_centre_vote);
        spinnerBureau = view.findViewById(R.id.spinner_bureau_vote);
        etVoix = view.findViewById(R.id.et_nombre_voix);
        btnSave = view.findViewById(R.id.btn_save_result);

        loadElectionSpinner();
        loadCandidatSpinner();
        loadCirconscriptionSpinner();
        
        spinnerCirconscription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Circonscription selected = circonscriptionList.get(position);
                loadCentreSpinner(selected.getId());
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerCentre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (centreList != null && !centreList.isEmpty()) {
                    CentreDeVote selected = centreList.get(position);
                    loadBureauSpinner(selected.getId());
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnSave.setOnClickListener(v -> saveResult());
    }

    private void loadElectionSpinner() {
        electionList = dbHelper.getAllElections();
        List<String> electionNames = electionList.stream().map(Election::getType).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, electionNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerElection.setAdapter(adapter);
    }

    private void loadCandidatSpinner() {
        candidatList = dbHelper.getAllCandidats();
        List<String> candidatNames = candidatList.stream().map(c -> c.getPrenom() + " " + c.getNom()).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, candidatNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCandidat.setAdapter(adapter);
    }
    
    private void loadCirconscriptionSpinner() {
        circonscriptionList = dbHelper.getAllCirconscriptions();
        List<String> names = circonscriptionList.stream().map(Circonscription::getNom).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCirconscription.setAdapter(adapter);
    }

    private void loadCentreSpinner(long circonscriptionId) {
        centreList = dbHelper.getCentresByCirconscription(circonscriptionId);
        List<String> names = centreList.stream().map(CentreDeVote::getNom).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCentre.setAdapter(adapter);
    }

    private void loadBureauSpinner(long centreId) {
        bureauList = dbHelper.getBureauxByCentre(centreId);
        List<String> names = bureauList.stream().map(BureauDeVote::getNumero).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBureau.setAdapter(adapter);
    }

    private void saveResult() {
        try {
            long electionId = electionList.get(spinnerElection.getSelectedItemPosition()).getId();
            long candidatId = candidatList.get(spinnerCandidat.getSelectedItemPosition()).getId();
            long bureauId = bureauList.get(spinnerBureau.getSelectedItemPosition()).getId();
            int nbVoix = Integer.parseInt(etVoix.getText().toString());

            Resultat resultat = new Resultat();
            resultat.setElectionId(electionId);
            resultat.setCandidatId(candidatId);
            resultat.setBureauVoteId(bureauId);
            resultat.setNbVoix(nbVoix);
            resultat.setDateSaisie(new Date());
            
            long resultId = dbHelper.addResultat(resultat);
            if(resultId != -1) {
                Toast.makeText(getContext(), "Résultat enregistré avec succès !", Toast.LENGTH_SHORT).show();
                // Optionnel: Vider les champs ou naviguer ailleurs
            } else {
                Toast.makeText(getContext(), "Erreur lors de l'enregistrement.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Veuillez remplir tous les champs correctement.", Toast.LENGTH_SHORT).show();
        }
    }
}