package com.example.bembicontroller;

import android.content.Context;
import android.content.SharedPreferences;
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

public class SettingsFragment extends Fragment {

    // Variabile pentru câmpurile de text
    private EditText etFwd, etBack, etLeft, etRight, etStop;
    private Button btnSave;

    // Numele fișierului de preferințe (caietul de notițe)
    private static final String PREFS_NAME = "BembiPrefs";

    public SettingsFragment() {
        // Constructor gol
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Legăm variabilele de design
        etFwd = view.findViewById(R.id.etCmdForward);
        etBack = view.findViewById(R.id.etCmdBack);
        etLeft = view.findViewById(R.id.etCmdLeft);
        etRight = view.findViewById(R.id.etCmdRight);
        etStop = view.findViewById(R.id.etCmdStop);
        btnSave = view.findViewById(R.id.btnSaveSettings);

        // 2. Încărcăm setările existente (sau punem valorile default dacă e prima dată)
        loadSettings();

        // 3. Setăm butonul de salvare
        btnSave.setOnClickListener(v -> saveSettings());
    }

    // Funcție care citește datele salvate și le pune în căsuțe
    private void loadSettings() {
        SharedPreferences settings = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Al doilea parametru este valoarea default ("F", "B", etc.) dacă nu s-a salvat nimic înainte
        etFwd.setText(settings.getString("CMD_FWD", "F"));
        etBack.setText(settings.getString("CMD_BACK", "B"));
        etLeft.setText(settings.getString("CMD_LEFT", "L"));
        etRight.setText(settings.getString("CMD_RIGHT", "R"));
        etStop.setText(settings.getString("CMD_STOP", "S"));
    }

    // Funcție care salvează ce ai scris în căsuțe
    private void saveSettings() {
        SharedPreferences settings = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Luăm textul din fiecare căsuță și îl salvăm
        editor.putString("CMD_FWD", etFwd.getText().toString());
        editor.putString("CMD_BACK", etBack.getText().toString());
        editor.putString("CMD_LEFT", etLeft.getText().toString());
        editor.putString("CMD_RIGHT", etRight.getText().toString());
        editor.putString("CMD_STOP", etStop.getText().toString());

        // Aplicăm salvarea
        editor.apply();

        Toast.makeText(getContext(), "Setări salvate cu succes!", Toast.LENGTH_SHORT).show();
    }
}