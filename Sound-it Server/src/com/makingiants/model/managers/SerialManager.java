package com.makingiants.model.managers;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class SerialManager {
	
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	private final static String PORT_NAME = "/dev/tty.usbserial-A900epgF";
	
	private OutputStream output;
	private SerialPort serialPort;
	
	public SerialManager() throws Exception {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		
		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			
			if (PORT_NAME.equals(currPortId.getName())) {
				portId = currPortId;
				break;
			}
		}
		
		if (portId != null) {
			
			try {
				// open serial port, and use class name for the appName.
				serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
				
				// set port parameters
				serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
				        SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				
				// open the streams
				output = serialPort.getOutputStream();
				
			} catch (Exception e) {
				System.err.println("SerialManager Exception: " + e.getMessage());
			}
			
		} else {
			throw new Exception("No Connection found");
		}
	}
	
	public void send(int value) throws IOException {
		output.write(String.valueOf(value).getBytes());
		output.flush();
		
	}
	
	public void send(String value) throws IOException {
		output.write((value + "\n").getBytes());
		output.flush();
		
	}
	
	public void close() throws IOException {
		output.close();
	}
}
