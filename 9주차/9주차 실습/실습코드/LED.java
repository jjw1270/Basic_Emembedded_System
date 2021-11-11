package week9;
import java.util.List;

import org.ws4d.coap.core.enumerations.CoapMediaType;
import org.ws4d.coap.core.rest.BasicCoapResource;
import org.ws4d.coap.core.rest.CoapData;
import org.ws4d.coap.core.tools.Encoder;
import com.pi4j.io.gpio.*;

public class LED extends BasicCoapResource{
	private String state = "off";


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

}