#include <SoftwareSerial.h>
 
int Rx=2;
int Tx=3;
SoftwareSerial bluetooth(Rx, Tx);
 
void setup(){
  Serial.begin(9600);
  bluetooth.begin(9600);
}
 
void loop(){
  if (bluetooth.available()) {
    Serial.write(bluetooth.read());
  }
  if (Serial.available()) {
    bluetooth.write(Serial.read());
  }
}
