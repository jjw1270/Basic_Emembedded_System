package project;

import jssc.SerialPort;

public class WriteThread extends Thread{
	
	public static String sendData = "";
	String tempData = "";
	boolean isSend = false;
	
	SerialPort serial;
	WriteThread(SerialPort serial){
		this.serial = serial;
	}
	
	public void run() {
			
			try {
				while(true) {
					if(sendData!="") {
						serial.writeString(sendData);
						sendData="";
					}
				}
			}
			catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	}
}