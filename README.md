# Vehicul Robotic Bluetooth
[Apasă aici pentru a vedea un Video Demo cu mașina](https://youtube.com/linkul-tau-aici)

Acesta este un vehicul robotic autonom, controlat de la distanță printr-o aplicație de mobil. Scopul principal al acestui proiect este aplicarea practică a conceptelor de electronică, programarea microcontrolerelor și dezvoltarea de aplicații native pentru Android.

## Arhitectura și Tehnologii Utilizate:
* Hardware: Placă de dezvoltare Arduino Mega 2560, modul Bluetooth HC-06, senzor de lumină BH1750 (I2C) și driver de motoare L298N.
* Software (Aplicație): Dezvoltată în Android Studio folosind Java și XML.
* Software (Microcontroler): C++ (Arduino IDE) pentru interpretarea comenzilor și controlul motoarelor.
* Alimentare: Doi acumulatori Li-Ion Samsung (3.7V, 3500mAh) legați în serie (7.4V) pentru a oferi putere optimă motoarelor.

## Funcționalitățile Principale
* Sistem de control wireless: Transmiterea comenzilor de deplasare și direcție prin Bluetooth, direct din aplicația Android către placa Arduino.
* Aplicație Android modulară: Interfață structurată pe 4 pagini (Principală, Control, Conectare, Setări), cu trimitere dinamică a comenzilor la apăsarea și ridicarea degetului de pe butoane.
* Iluminare automată inteligentă: Senzorul BH1750 detectează scăderea luminozității sub 40 lux și aprinde automat LED-urile mașinii, fără intervenția utilizatorului.
* Organizare hardware eficientă: Utilizarea conectorilor Wago pentru un wire-management compact, înlocuind breadboard-ul clasic din cauza lipsei de spațiu pe șasiu.

## Integrarea Asistenței AI în fluxul de lucru
* Acest proiect este realizat cu ajutorul inteligenței artificiale (Gemini). Cu ajutorul acesteia am reușit să scriu mai eficient codul pentru Arduino și logica aplicației din Android Studio.
* Rolul meu: Am realizat asamblarea fizică a hardware-ului, am proiectat interfața aplicației, am testat comunicarea și am rezolvat problemele electrice.
* Rolul AI-ului: Asistentul a fost utilizat pentru generarea sintaxei Java/C++, funcțiilor de conectare Bluetooth și pentru asistență în depanare.

## Instrucțiuni pentru rularea locală:
* Deschideți fișierul .ino în Arduino IDE, conectați placa Arduino Mega 2560 prin USB și încărcați codul.
* Deschideți proiectul aplicației în Android Studio, compilați și instalați fișierul APK pe telefon.
* Împerecheați modulul HC-06 cu telefonul din setările Bluetooth.
* Deschideți aplicația, navigați la pagina de "Conectare", alegeți robotul (HC-06).

[Apasă aici pentru a vedea un Video Demo cu mașina](https://youtube.com/linkul-tau-aici)
