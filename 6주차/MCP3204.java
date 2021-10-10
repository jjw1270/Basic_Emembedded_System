package week7;

import java.io.IOException;
import com.pi4j.io.spi.*;


public class MCP3204 {
	public static SpiDevice spi = null;
	public MCP3204() {
		try {
			//SPI 객체 선언
			spi = SpiFactory.getInstance(SpiChannel.CS0, SpiDevice.DEFAULT_SPI_SPEED, SpiDevice.DEFAULT_SPI_MODE);
		}catch (Exception e) {
			System.out.println("Fail to create a SPI instance");
		}		
	}
	
	public static String byteToBinaryString(byte n) {
		// byte의 binary 값을 String으로 반환
	    StringBuilder sb = new StringBuilder("00000000");
	    for (int bit = 0; bit < 8; bit++) {
	        if (((n >> bit) & 1) > 0) {
	            sb.setCharAt(7 - bit, '1');
	        }
	    }
	    return sb.toString();
	}
	
	public int readMCP3204(int adcChannel) throws IOException {

	}

	public static void main(String[] args) {
		MCP3204 obj = new MCP3204();
		while(true) {
			try {
				int value = obj.readMCP3204(0);
				System.out.println(value);
				Thread.sleep(1000);
			}catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}
