#include <SoftwareSerial.h>
 
int Rx=2;
int Tx=3;
SoftwareSerial bluetooth(Rx, Tx);

String btRead;

void setup(){
  Serial.begin(115200);
  bluetooth.begin(9600);
}

void loop(){
  if (Serial.available()) {
    String serialRead = Serial.readString();
    if(serialRead == "L"){
      bluetooth.print("ledOn");}
    if(serialRead == "F"){
      bluetooth.print("ledOff");}
    if(serialRead == "M"){
      bluetooth.print("moterOn");}
   if(serialRead == "N")
     bluetooth.print("moterOff");
   //else if(serialRead == 'F')
   //  bluetooth.print("fan");
   //else
   //  bluetooth.print(serialRead);
  }
  
  if (bluetooth.available()) {
    btRead = bluetooth.readString();
    Serial.print(btRead);
  }


}
