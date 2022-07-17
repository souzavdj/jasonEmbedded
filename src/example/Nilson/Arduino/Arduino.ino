#include <Javino.h>

#define LED    13

Javino j; 

void setup() {
  Serial.begin(9600);
  pinMode(LED, OUTPUT);
}

void loop(){
 if(j.availablemsg()){
    String msg = j.getmsg();
    if(msg == "getPercepts") {
      j.sendmsg(getPercepts());
    } else if(msg == "turnOnLight") {
      digitalWrite(LED, HIGH);
    } else if(msg == "turnOffLight") {
      digitalWrite(LED, LOW);
    }
 }
}

String getPercepts() {
  return "luminosity(36);proximity(22)";
}