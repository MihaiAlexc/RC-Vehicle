#include <Wire.h>

int IN1 = 4, IN2 = 5, ENA = 3;
int IN3 = 8, IN4 = 9, ENB = 10;
bool reverseMode = false;

#define BH1750_ADDR 0x23
#define LED_PIN A1
#define LIGHT_THRESHOLD 40

void setupBH1750() {
  Wire.beginTransmission(BH1750_ADDR);
  Wire.write(0x10);
  Wire.endTransmission();
}

float readBH1750() {
  Wire.requestFrom(BH1750_ADDR, 2);
  if (Wire.available() == 2) {
    uint16_t val = Wire.read();
    val <<= 8;
    val |= Wire.read();
    return val / 1.2;
  }
  return -1;
}

void setup() {
  pinMode(IN1, OUTPUT); pinMode(IN2, OUTPUT); pinMode(ENA, OUTPUT);
  pinMode(IN3, OUTPUT); pinMode(IN4, OUTPUT); pinMode(ENB, OUTPUT);
  
  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);
  
  Serial.begin(9600);
  Serial1.begin(9600);
  
  Wire.begin();
  setupBH1750();
  
  Serial.println("Robot gata pentru comenzi Bluetooth F/B/L/R/X");
}

void loop() {
  float lux = readBH1750();
  if (lux >= 0) {
    Serial.print("Lumina: ");
    Serial.print(lux);
    Serial.println(" lx");
    
    if (lux < LIGHT_THRESHOLD) {
      digitalWrite(LED_PIN, HIGH);
    } else {
      digitalWrite(LED_PIN, LOW);
    }
  }

  if (Serial1.available()) {
    char cmd = Serial1.read();
    Serial.print("Comanda primita: ");
    Serial.println(cmd);
    
    if (cmd == 'X') {
      reverseMode = !reverseMode;
      Serial.print("Reverse mode: ");
      Serial.println(reverseMode ? "ON" : "OFF");
      return;
    }
    
    if (!reverseMode) {
      switch (cmd) {
        case 'F':
          digitalWrite(IN1, HIGH); digitalWrite(IN2, LOW); analogWrite(ENA, 200);
          digitalWrite(IN3, LOW); digitalWrite(IN4, LOW); analogWrite(ENB, 0);
          break;
        case 'B':
          digitalWrite(IN1, LOW); digitalWrite(IN2, HIGH); analogWrite(ENA, 200);
          digitalWrite(IN3, LOW); digitalWrite(IN4, LOW); analogWrite(ENB, 0);
          break;
        case 'L':
          digitalWrite(IN1, HIGH); digitalWrite(IN2, LOW); analogWrite(ENA, 0);
          digitalWrite(IN3, LOW); digitalWrite(IN4, HIGH); analogWrite(ENB, 255);
          break;
        case 'R':
          digitalWrite(IN1, HIGH); digitalWrite(IN2, LOW); analogWrite(ENA, 0);
          digitalWrite(IN3, HIGH); digitalWrite(IN4, LOW); analogWrite(ENB, 255);
          break;
        default:
          digitalWrite(IN1, LOW); digitalWrite(IN2, LOW); analogWrite(ENA, 0);
          digitalWrite(IN3, LOW); digitalWrite(IN4, LOW); analogWrite(ENB, 0);
          break;
      }
    } else {
      switch (cmd) {
        case 'F':
          digitalWrite(IN1, HIGH); digitalWrite(IN2, LOW); analogWrite(ENA, 255);
          digitalWrite(IN3, LOW); digitalWrite(IN4, LOW); analogWrite(ENB, 0);
          break;
        case 'B':
          digitalWrite(IN1, LOW); digitalWrite(IN2, HIGH); analogWrite(ENA, 200);
          digitalWrite(IN3, LOW); digitalWrite(IN4, LOW); analogWrite(ENB, 0);
          break;
        case 'L':
          digitalWrite(IN1, LOW); digitalWrite(IN2, HIGH); analogWrite(ENA, 255);
          digitalWrite(IN3, LOW); digitalWrite(IN4, HIGH); analogWrite(ENB, 255);
          break;
        case 'R':
          digitalWrite(IN1, LOW); digitalWrite(IN2, HIGH); analogWrite(ENA, 255);
          digitalWrite(IN3, HIGH); digitalWrite(IN4, LOW); analogWrite(ENB, 255);
          break;
        default:
          digitalWrite(IN1, LOW); digitalWrite(IN2, LOW); analogWrite(ENA, 0);
          digitalWrite(IN3, LOW); digitalWrite(IN4, LOW); analogWrite(ENB, 0);
          break;
      }
    }
  }
  
  delay(200);
}