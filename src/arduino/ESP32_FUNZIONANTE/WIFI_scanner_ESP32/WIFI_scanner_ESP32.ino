
#include "WiFi.h"

void setup() {
  Serial.begin(115200);

  // Imposto il mio esp in modalità stazione,e lo disconnetto da tutte le reti che prima aveva
  // salvate poi eseguo uno scanning nella parte del loop, per vedere:
  // - Quali reti riesce a rilevare
  // - Quanto è il relativo valore RSSI che rappresenta la forza del segnale
  // - tipologia di crittazione

  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(100);

  Serial.println("Setup done");
}

void loop() {
  Serial.println("scan start");

  // Scanner che mi ritorna il numero e le reti che posso raggiungere.
  int n = WiFi.scanNetworks();
  Serial.println("scan done");
  if (n == 0) {
      Serial.println("no networks found");
  } else {
    Serial.print(n);
    Serial.println(" networks found");
    for (int i = 0; i < n; ++i) {
      // RSSI
      Serial.print(i + 1);
      Serial.print(": ");
      Serial.print(WiFi.SSID(i));
      Serial.print(" (");
      Serial.print(WiFi.RSSI(i));
      Serial.print(")");
      Serial.println((WiFi.encryptionType(i) == WIFI_AUTH_OPEN)?" ":"*");
      delay(10);
    }
  }
  Serial.println("");
  delay(5000);
}