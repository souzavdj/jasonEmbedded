#include <Javino.h>
Javino j;

void setup() {
  // initialize digital pin LED_BUILTIN as an output.
  Serial.begin(9600);
  pinMode(LED_BUILTIN, OUTPUT);
}

// the loop function runs over and over again forever
void loop() {
  if (j.availablemsg()) {
    String response = j.getmsg();

    if (response == "ligar") {
      digitalWrite(LED_BUILTIN, HIGH);      
    } else if (response == "desligar") {
      digitalWrite(LED_BUILTIN, LOW);
    }
  }
}

