/*
 * Note about ESP
 * 
 * - D2 <=> GPIO4
 * - boud rate: 115200
 */
#define LED_PIN 2

void setup() {                
  pinMode(LED_PIN, OUTPUT); 
  Serial.begin(115200);     
  Serial.println("READY"); 
}

void loop() {
  int value = analogRead(34);
  Serial.println("Value: " + String(value));
  digitalWrite(LED_PIN, HIGH);
  Serial.println("ON");
  if(value<1000){
    delay(150);  
  }else if (value>1000 && value <2000){
     delay(300);  
  }else if (value>2000 && value <3000){
     delay(450);  
  }else if (value>3000 && value <400){
     delay(600);  
  }else {
    delay(1000);  
  }       
  digitalWrite(LED_PIN, LOW);
  Serial.println("OFF");   
 if(value<1000){
    delay(150);  
  }else if (value>1000 && value <2000){
     delay(300);  
  }else if (value>2000 && value <3000){
     delay(450);  
  }else if (value>3000 && value <400){
     delay(600);  
  }else {
    delay(1000);  
  }              
}
