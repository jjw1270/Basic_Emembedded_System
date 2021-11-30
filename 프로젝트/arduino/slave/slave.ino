#include <SoftwareSerial.h>
#include <dht.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <ThreeWire.h>
#include <RtcDS1302.h>

//블루투스 모듈
const int Rx=2;
const int Tx=3;
SoftwareSerial bluetooth(Rx, Tx);
String btRead;
//조도센서
const int photoR = A2;
//DHT11온습도 센서
dht DHT;
#define DHT11 A1
int hum;
int temp;
//I2C LCD모듈
LiquidCrystal_I2C lcd(0x27, 16, 2);
//RTC모듈
ThreeWire myWire(8,7,9);
RtcDS1302<ThreeWire> Rtc(myWire);
unsigned long RTCpreMillis = 0;
//모터
const int moterIn1 = 4;
const int moterIn2 = 5;
bool ismoterOn = false;
//LED
const int led = 10;
bool isledOn = false;

unsigned long sensorPreMillis = 0;

void setup(){
  bluetooth.begin(9600);//블루투스 초기화
  //lcd 초기화
  lcd.begin();
  lcd.noBacklight();
  lcd.setCursor(0,0);

  Rtc.Begin();
  //RTC모듈 쓰기금지 확인 후 해제
  if (Rtc.GetIsWriteProtected())
    Rtc.SetIsWriteProtected(false);
  //RTC모듈 구동
  if (!Rtc.GetIsRunning())
    Rtc.SetIsRunning(true);
  //컴파일시 PC 시간으로 클래스 생성
  RtcDateTime nowTime = RtcDateTime(__DATE__,__TIME__);
  Rtc.SetDateTime(nowTime);

  pinMode(moterIn1, OUTPUT);
  pinMode(moterIn2, OUTPUT);
  pinMode(led, OUTPUT);
}

void LCDclear(){
  lcd.clear();
  lcd.backlight();
  lcd.setCursor(0,0);
}

void loop(){
  unsigned long currentMillis = millis();
  //1초간격으로 시간 출력
  if(currentMillis - RTCpreMillis >= 1000){
    lcd.noBacklight();
    lcdprintDateTime(Rtc.GetDateTime());
  }
  
  int photoRVal = map(analogRead(photoR), 250, 1023, 100, 0);
  
  DHT.read11(DHT11);
  hum = int(DHT.humidity);
  temp = int(DHT.temperature);
  
  //3초마다 센서값 전송
  if(currentMillis-sensorPreMillis > 5000){
    sensorPreMillis = currentMillis;
    //조도센서 동작
    String prv = String(photoRVal);
    if(prv.length()<2)
      prv = "0"+prv;
    bluetooth.print("PR");
    bluetooth.print(prv);
    //DHT11센서 동작
    String hv = String(hum);
    if(hv.length()<2)
      hv = "0"+hv;
    String tv = String(temp);
    if(tv.length()<2)
      tv = "0"+tv;
    bluetooth.print("Hm");
    bluetooth.print(hv);
    bluetooth.print("Tp");
    bluetooth.print(tv);
    bluetooth.flush();
  }
  //값 받기 
  if (bluetooth.available()) {
    btRead = bluetooth.readString();

    //LED 동작
    if(btRead == "ledOn"){
      if (!isledOn){  //꺼져있으면 동작
        LCDclear();
        digitalWrite(led, HIGH);
        lcd.print("LED ON");
        delay(3000);
        isledOn = true;
      }
    }
    if(btRead == "ledOff"){
      if(isledOn){   //켜져있으면 동작
        LCDclear();        
        digitalWrite(led, LOW);
        lcd.print("LED OFF");
        delay(3000);
        isledOn = false;
      }
    }
    //팬 동작
    if(btRead == "moterOn"){
      if(!ismoterOn){  //꺼져있으면 동작
        LCDclear();
        digitalWrite(moterIn1, HIGH);
        digitalWrite(moterIn2, LOW);     
        lcd.print("FAN ON");
        delay(3000);
        ismoterOn = true;
      }
    }
    if(btRead == "moterOff"){
      if(ismoterOn){  //켜져있으면 동작
        LCDclear();
        digitalWrite(moterIn1, LOW);
        digitalWrite(moterIn2, LOW);
        lcd.print("FAN OFF");
        delay(3000);
        ismoterOn = false;
      }
    }
  }
}


#define countof(a) (sizeof(a) / sizeof(a[0]))
void lcdprintDateTime(const RtcDateTime& dt){
  char dateString[20] = {0, };
  char timeString[20] = {0, };

  snprintf_P(dateString,
             countof(dateString),
             PSTR("%04u-%02u-%02u"),
             dt.Year(),
             dt.Month(),
             dt.Day());
  snprintf_P(timeString,
             countof(timeString),
             PSTR("%02u:%02u:%02u"),
             dt.Hour(),
             dt.Minute(),
             dt.Second());

  lcd.setCursor(0, 0);
  lcd.print(dateString);
  lcd.setCursor(0, 1);
  lcd.print(timeString);
}
