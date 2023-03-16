#include <WiFi.h>
#include <HTTPClient.h>

// Replace with your network credentials (STATION)
const char* ssid = "TIM-30946425"; //inserisci ssis casa
const char* password = "CasaCiarafoni00."; //relativa psw
char* address = "http://192.168.1.7:8080";   //https://b036-79-26-139-8.eu.ngrok.io/


void initWiFi() {
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
  Serial.print("Connecting to WiFi ..");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(1000);
  }
  Serial.println(WiFi.localIP());
}

void setup() {
  Serial.begin(115200);
  initWiFi();
  Serial.print("RRSI: ");
  Serial.println(WiFi.RSSI());

}
int sendData(String address, float value, String place){  
   HTTPClient http;    
   http.begin(address + "/api/data");      
   http.addHeader("Content-Type", "text/plain");     
   String msg = 
    String("{ \"value\": ") + String(value) + 
    ", \"place\": \"" + place +"\" }";
   int retCode = http.POST(msg);   
   http.end();  
      
   // String payload = http.getString();  
    //Serial.println(payload);      
   return retCode;
}


void loop() {
if (WiFi.status()== WL_CONNECTED){   

   /* read sensor */
   float value = (float) analogRead(A0) / 4092.0;
   
   /* send data */
   Serial.print("sending "+String(value)+"...");    
   int code = sendData(address, value, "home");

   Serial.println();
   /* log result */
   if (code == HTTP_CODE_OK){
     Serial.println("ok");   
   } else {
     Serial.println("error: " + String(code));
   }
 } else { 
   Serial.println("Error in WiFi connection");   
 }
 
 delay(5000);  
 


}