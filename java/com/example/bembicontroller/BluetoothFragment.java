package com.example.bembicontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class BluetoothFragment extends Fragment {

    private ImageView imgStatus;
    private TextView tvStatus;
    private Button btnScan;
    private ListView deviceList;

    private BluetoothAdapter myBluetooth;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listDevices = new ArrayList<>();

    // UUID Standard pentru module Serial (HC-05, HC-06)
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public BluetoothFragment() {
        // Constructor gol
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Atenție: Asigură-te că numele fișierului tău XML este corect (fragment_bluetooth sau fragment_connection)
        return inflater.inflate(R.layout.fragment_bluetooth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgStatus = view.findViewById(R.id.imgBluetoothStatus);
        tvStatus = view.findViewById(R.id.tvStatus);
        btnScan = view.findViewById(R.id.btnScan);
        deviceList = view.findViewById(R.id.deviceList);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        // Verificăm dacă suntem deja conectați ca să actualizăm textul
        if (ConnectionManager.mmSocket != null && ConnectionManager.mmSocket.isConnected()) {
            tvStatus.setText("CONECTAT");
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_light));
            imgStatus.setColorFilter(getResources().getColor(android.R.color.holo_green_light));
        }

        if (myBluetooth == null) {
            Toast.makeText(getContext(), "Fără Bluetooth!", Toast.LENGTH_LONG).show();
        } else if (!myBluetooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // startActivity(turnBTon); // Opțional
            tvStatus.setText("Bluetooth Oprit");
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listDevices);
        deviceList.setAdapter(adapter);

        btnScan.setOnClickListener(v -> showPairedDevices());

        // --- AICI ESTE LOGICA DE CONECTARE PENTRU SAMSUNG S25 ---
        deviceList.setOnItemClickListener((parent, view1, position, id) -> {
            String info = ((TextView) view1).getText().toString();
            if(info.length() < 17) return;
            String address = info.substring(info.length() - 17);

            tvStatus.setText("Se conectează...");

            // Pornim conectarea pe un fir separat ca să nu blocăm ecranul
            new Thread(() -> {
                try {
                    // 1. Verificăm permisiunile
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    // 2. CRITIC PENTRU S25: Oprim scanarea înainte de conectare
                    if (myBluetooth.isDiscovering()) {
                        myBluetooth.cancelDiscovery();
                    }

                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    BluetoothSocket tmpSocket = null;
                    boolean connected = false;

                    // 3. Încercăm conectarea (Metoda Insecure este mai bună pentru HC-06)
                    try {
                        tmpSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                        tmpSocket.connect();
                        connected = true;
                    } catch (IOException e) {
                        // Dacă eșuează, încercăm metoda Securizată
                        try {
                            if(tmpSocket != null) tmpSocket.close();
                            tmpSocket = dispositivo.createRfcommSocketToServiceRecord(myUUID);
                            tmpSocket.connect();
                            connected = true;
                        } catch (IOException e2) {
                            connected = false;
                        }
                    }

                    if (connected) {
                        // Salvăm conexiunea în Manager
                        ConnectionManager.mmSocket = tmpSocket;

                        // Actualizăm ecranul
                        getActivity().runOnUiThread(() -> {
                            tvStatus.setText("Conectat la: " + dispositivo.getName());
                            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                            imgStatus.setColorFilter(getResources().getColor(android.R.color.holo_green_light));
                            Toast.makeText(getContext(), "CONECTAT! Mergi la Gamepad.", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        getActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Eșec conectare. Verifică dacă robotul e pornit.", Toast.LENGTH_LONG).show()
                        );
                    }

                } catch (Exception e) {
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Eroare: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }).start();
        });
    }

    private void showPairedDevices() {
        // Verificăm permisiunile înainte de a cere lista (Evită crash pe Android 15)
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 1);
            Toast.makeText(getContext(), "Acceptă permisiunea și apasă din nou!", Toast.LENGTH_LONG).show();
            return;
        }

        listDevices.clear();
        Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                listDevices.add(bt.getName() + "\n" + bt.getAddress());
            }
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Nu am găsit dispozitive. Împerechează robotul din setările telefonului întâi!", Toast.LENGTH_LONG).show();
        }
    }
}