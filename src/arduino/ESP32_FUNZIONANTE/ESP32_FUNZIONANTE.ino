#include <HTTPClient.h>
#include <WiFi.h>


/* wifi network name */
const char* ssidName = "TIM-30946425"; //aggiunto const
/* WPA2 PSK password */
 const char* pwd = "CasaCiarafoni00."; //aggiunto const
/* service IP address */ 
char* address = "http://192.168.1.11:8080"; //controllare e cambiare in base a che dice la console di eclipse.
const int potPin =34;
void setup() {
  Serial.begin(115200);                                
  WiFi.begin(ssidName, pwd);
  WiFi.mode(WIFI_STA);
  Serial.print("Connecting...");
  while (WiFi.status() != WL_CONNECTED) {  
    delay(500);
    Serial.print(".");
  } 
  Serial.print("Connected: \n local IP: ");
  Serial.println(WiFi.localIP());
  // put your setup code here, to run once:

}
int sendData(String address, float value, String place){  
   HTTPClient http;       
   http.begin(address + "/api/data");      
   http.addHeader("Content-Type", "application/json");
       
   String msg = 
    String("{ \"value\": ") + String(value) + 
    ", \"place\": \"" + place +"\" }";
   int retCode = http.POST(msg);   
   http.end();  
      
   // String payload = http.getString();  
   // Serial.println(payload);      
   return retCode;
}

void loop() {
  if (WiFi.status()== WL_CONNECTED){   
  
   /* read sensor */
   float value = (float) analogRead(potPin) / 4095.0;
   
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
