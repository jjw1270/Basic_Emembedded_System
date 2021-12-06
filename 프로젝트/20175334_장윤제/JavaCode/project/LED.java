package project;
import java.util.List;

import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.rest.BasicCoapResource;
import org.ws4d.coap.core.rest.CoapData;
import org.ws4d.coap.core.tools.Encoder;

import jssc.SerialPort;

public class LED extends BasicCoapResource{
	private String state = "off";
	public static SerialPort serialPort = new SerialPort("/dev/ttyACM0");
	//WriteThread sd = new WriteThread(serialPort);
	

	private LED(String path, String value, CoapMediaType mediaType) {
		super(path, value, mediaType);
	}

	public LED() {
		this("/led", "off", CoapMediaType.text_plain);
	}

	@Override
	public synchronized CoapData get(List<String> query, List<CoapMediaType> mediaTypesAccepted) {
		return get(mediaTypesAccepted);
	}
	
	@Override
	public synchronized CoapData get(List<CoapMediaType> mediaTypesAccepted) {
		return new CoapData(Encoder.StringToByte(this.state), CoapMediaType.text_plain);
	}

	@Override
	public synchronized boolean setValue(byte[] value) {
		this.state = Encoder.ByteToString(value);
		
		if(this.state.equals("on")) {
			WriteThread.sendData = "L";
		}
		else if (this.state.equals("off")) {
			WriteThread.sendData = "F";
		}

		return true;
	}
	
	@Override
	public synchronized boolean post(byte[] data, CoapMediaType type) {
		return this.setValue(data);
	}

	@Override
	public synchronized boolean put(byte[] data, CoapMediaType type) {
		return this.setValue(data);
	}

	@Override
	public synchronized String getResourceType() {
		return "Raspberry pi 4 LED";
	}

//	public static boolean isLEDisOn() {
//		return LEDisOn;
//	}
//
//	public static void setLEDisOn(boolean lEDisOn) {
//		LEDisOn = lEDisOn;
//	}
	
	

}