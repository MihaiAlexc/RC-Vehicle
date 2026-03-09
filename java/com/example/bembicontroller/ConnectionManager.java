package com.example.bembicontroller;

import android.bluetooth.BluetoothSocket;
import java.io.IOException;

public class ConnectionManager {
    // Aici ținem "firul" conectat la robot. E static ca să fie accesibil de oriunde.
    public static BluetoothSocket mmSocket = null;

    // Funcție simplă să trimitem mesaje (F, B, L, R, etc.)
    public static void sendData(String msg) {
        if (mmSocket != null && mmSocket.isConnected()) {
            try {
                mmSocket.getOutputStream().write(msg.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}