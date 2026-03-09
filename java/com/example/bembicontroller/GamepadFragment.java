package com.example.bembicontroller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

public class GamepadFragment extends Fragment {

    Button btnFata, btnSpate, btnStanga, btnDreapta, btnX;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Asigură-te că numele fișierului XML e corect (fragment_gamepad)
        View view = inflater.inflate(R.layout.fragment_gamepad, container, false);

        // 1. Găsim butoanele din XML
        btnFata = view.findViewById(R.id.btn_fata);
        btnSpate = view.findViewById(R.id.btn_spate);
        btnStanga = view.findViewById(R.id.btn_stanga);
        btnDreapta = view.findViewById(R.id.btn_dreapta);
        btnX = view.findViewById(R.id.btn_x);

        // 2. Configurăm butoanele de mișcare (apasă = mergi, ridică = stop)
        setupButton(btnFata, "F");
        setupButton(btnSpate, "B");
        setupButton(btnStanga, "L");
        setupButton(btnDreapta, "R");

        // 3. Butonul X (Reverse Mode) - doar click simplu
        btnX.setOnClickListener(v -> ConnectionManager.sendData("X"));

        return view;
    }

    // Funcție care trimite comanda cât timp ții apăsat și STOP ("S") când ridici
    @SuppressLint("ClickableViewAccessibility")
    private void setupButton(Button btn, final String comandaMiscare) {
        btn.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Când APASĂM -> Trimitem litera de mișcare folosind funcția ta
                ConnectionManager.sendData(comandaMiscare);
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                // Când RIDICĂM degetul -> Trimitem "S" pentru a opri motoarele
                // (Arduino intră pe cazul default)
                ConnectionManager.sendData("S");
                return true;
            }
            return false;
        });
    }
}