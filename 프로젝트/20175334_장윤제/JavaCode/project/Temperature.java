package project;

import java.util.List;

import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.rest.BasicCoapResource;
import org.ws4d.coap.core.rest.CoapData;
import org.ws4d.coap.core.tools.Encoder;

public class Temperature extends BasicCoapResource{

	private String value = "0";
	
	private Temperature(String path, String value, CoapMediaType mediaType) {
		super(path, value, mediaType);
	}

	public Temperature() {
		this("/temperature", "0", CoapMediaType.text_plain);
	}

	@Override
	public synchronized CoapData get(List<String> query, List<CoapMediaType> mediaTypesAccepted) {
		return get(mediaTypesAccepted);
	}
	
	@Override
	public synchronized CoapData get(List<CoapMediaType> mediaTypesAccepted) {
		this.value = ReadThread.receiveTempData;
		return new CoapData(Encoder.StringToByte(this.value), CoapMediaType.text_plain);
	}
	
	
	public synchronized void optional_changed() {
		//온도값 읽어오기
		String temp = ReadThread.receiveTempData;
		//이전에 확인했던 온도와 같은지
		//만약 온도가 달라졌으면, observe 응답 전송
		if (temp.equals(this.value)) {
			System.out.println("Temp has not changed.");
		}
		else {
			System.out.println("Temp  : " + temp.substring(2) + "℃");
			this.changed(temp);
			this.value = temp;
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
		return "Raspberry pi 4 Temperature";
	}
}
