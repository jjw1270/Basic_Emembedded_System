package project;

import java.util.List;

import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.rest.BasicCoapResource;
import org.ws4d.coap.core.rest.CoapData;
import org.ws4d.coap.core.tools.Encoder;

public class Humidity extends BasicCoapResource{

	private String value = "0";
	
	private Humidity(String path, String value, CoapMediaType mediaType) {
		super(path, value, mediaType);
	}

	public Humidity() {
		this("/humidity", "0", CoapMediaType.text_plain);
	}

	@Override
	public synchronized CoapData get(List<String> query, List<CoapMediaType> mediaTypesAccepted) {
		return get(mediaTypesAccepted);
	}
	
	@Override
	public synchronized CoapData get(List<CoapMediaType> mediaTypesAccepted) {
		this.value = ReadThread.receiveHumData;
		return new CoapData(Encoder.StringToByte(this.value), CoapMediaType.text_plain);
	}
	
	
	public synchronized void optional_changed() {
		//습도값 읽어오기
		String hum = ReadThread.receiveHumData;
		//이전에 확인했던 습도와 같은지
		//만약 습도가 달라졌으면, observe 응답 전송
		if (hum.equals(this.value)) {
			System.out.println("Humi has not changed.");
		}
		else {
			System.out.println("Humi  : " + hum.substring(2) + "%");
			this.changed(hum);
			this.value = hum;
		}
	}
	

	
	@Override
	public synchronized boolean setValue(byte[] value) {
		this.value = Encoder.ByteToString(value);
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
		return "Raspberry pi 4 Humidity";
	}
}
