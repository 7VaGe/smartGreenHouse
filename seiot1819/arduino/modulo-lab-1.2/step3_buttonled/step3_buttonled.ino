#define BUTTON_PIN 4  
#define LED_PIN 7   

void setup() {
  pinMode(LED_PIN, OUTPUT);      
  pinMode(BUTTON_PIN, INPUT);     
  Serial.begin(9600);
}

void loop() {
  int buttonState = digitalRead(BUTTON_PIN);
  if (buttonState == HIGH) {     
    digitalWrite(LED_PIN, HIGH);  
    Serial.println("Pressed");
  } else {
    digitalWrite(LED_PIN, LOW); 
  }
  // delay(20);
}
