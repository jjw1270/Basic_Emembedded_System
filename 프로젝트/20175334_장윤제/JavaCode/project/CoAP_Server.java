package project;

import org.ws4d.coap.core.rest.CoapResourceServer;


import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/*
public class Main {
	public static void main(String[] args) 
	{
		try {
			(new Serial()).connect("COM5");
		}
		catch(Exception e) {e.printStackTrace();}
	}
}*/

public class CoAP_Server {
	private static CoAP_Server coapServer;
	private CoapResourceServer resourceServer;
	public static SerialPort serialPort = new SerialPort("/dev/ttyACM0");
	
	public static void main(String[] args) throws Exception{
		coapServer = new CoAP_Server();
		coapServer.start();
	}

	public void start() throws SerialPortException {
		System.out.println("===Run Test Server ===");

		String[] portNames = SerialPortList.getPortNames();
		for(int i = 0; i<portNames.length;i++) {
			System.out.println(portNames[i]);
		}
		
		serialPort.openPort();
		serialPort.setParams(SerialPort.BAUDRATE_115200,
				SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1,
				SerialPort.PARITY_NONE);
		
		// create server
		if (this.resourceServer != null)	this.resourceServer.stop();
		this.resourceServer = new CoapResourceServer();

		// initialize resource
		LED LED = new LED();
		PhotoResistor PhotoResistor = new PhotoResistor();
		Humidity Humidity = new Humidity();
		Temperature Temperature = new Temperature();
		Motor Motor = new Motor();
		
		// add resource to server
		this.resourceServer.createResource(LED);
		this.resourceServer.createResource(PhotoResistor);
		this.resourceServer.createResource(Humidity);
		this.resourceServer.createResource(Temperature);
		this.resourceServer.createResource(Motor);
		PhotoResistor.registerServerListener(resourceServer);
		Humidity.registerServerListener(resourceServer);
		Temperature.registerServerListener(resourceServer);
		
		// run the server
		try {
			this.resourceServer.start();
			new ReadThread(serialPort).start();
			new WriteThread(serialPort).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		while(true) {
			try {
				Thread.sleep(5000);
				PhotoResistor.changed();
				System.out.println("Illum : " + ReadThread.receivePRData.substring(2) + "%");
				Humidity.optional_changed();
				Temperature.optional_changed();
				System.out.println("---------------------------------");
			}
			catch(Exception e) {
			}
		}
	}
}

