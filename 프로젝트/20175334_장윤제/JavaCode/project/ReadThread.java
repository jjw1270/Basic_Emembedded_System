package project;

import jssc.SerialPort;

public class ReadThread extends Thread{
	
	boolean dataReceiveOk = false;
	boolean dataReceiveComplete = false;
	String receiveData = "";
	public static String receivePRData = "";
	public static String receiveTempData = "";
	public static String receiveHumData = "";
	
	SerialPort serial;
	ReadThread(SerialPort serial){
		this.serial = serial;
	}
	
	public void run() {
		try {
			while(true) {
				byte[] read = serial.readBytes();
				if(read != null && read.length > 0) {
					receiveData = new String(read);
					if(receiveData.length()==12) {  //값이 정상적으로 들어올 때
						//System.out.println(receiveData);
						if(receiveData.indexOf("PR")!=-1) {  //조도센서값
							int firstIndex = receiveData.indexOf("P");
							receivePRData = receiveData.substring(firstIndex,firstIndex+4);     //PR00
						}
						if(receiveData.indexOf("Hm")!=-1) { //습도값
							int firstIndex = receiveData.indexOf("H");
							receiveHumData = receiveData.substring(firstIndex,firstIndex+4); //Hm00
						}
						if(receiveData.indexOf("Tp")!=-1) {  //온도값
							int firstIndex = receiveData.indexOf("T");
							receiveTempData = receiveData.substring(firstIndex);     //Tp00
						}
					}
					this.receiveData = "";
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}