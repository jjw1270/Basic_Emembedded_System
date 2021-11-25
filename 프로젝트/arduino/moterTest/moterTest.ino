int moterIn1 = 4;
int moterIn2 = 5;

void setup() {
  // put your setup code here, to run once:
  pinMode(moterIn1, OUTPUT);
  pinMode(moterIn2, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  digitalWrite(moterIn1, HIGH);
  digitalWrite(moterIn2, LOW);
  delay(4000);
  digitalWrite(moterIn1, LOW);
  digitalWrite(moterIn2, LOW);
  delay(2000);
  
}
