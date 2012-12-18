package com.makingiants.model.managers;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

public class MessageManager {
	
	// ---------------------------------------------
	// Attributes
	// ---------------------------------------------
	
	private final SoundPlayer player;
	private SerialManager serialManager;
	
	private static MessageManager instance;
	
	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------
	
	private MessageManager() {
		player = new SoundPlayer();
		
		try {
			serialManager = new SerialManager();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static MessageManager getInstance() {
		if (instance == null) {
			instance = new MessageManager();
		}
		return instance;
		
	}
	
	// ---------------------------------------------
	// Message managment methods
	// ---------------------------------------------
	int level = 1;

	public void manage(final String message) {
		if (message.charAt(0) == 'v') {
			
			final String[] split = message.split(" ");
			final int accX = Integer.valueOf(split[1]);
			int accY = Integer.valueOf(split[2]);//0-128
			int accZ = Integer.valueOf(split[3]);//0-128
			final int channel = Integer.valueOf(split[4]);//0-16
			
			
			// TODO: get when should I left pressed or not from message
			final boolean pressed = false;//Boolean.valueOf(split[4]);
			
			try {
				int led;
				
				if(accZ < -7){
					if(level < 2){
						level++;
					}
				}
				else if(accZ > 7){
					if(level > 0){
						level--;
					}
				}
				
				if (accY > 1) {
					if (accX > 3) {
						led = 0;
					} else if (accX < -3) {
						led = 2;
					} else {
						led = 1;
					}
					
				} else if (accY < -1) {
					if (accX > 3) {
						led = 6;
					} else if (accX < -3) {
						led = 8;
					} else {
						led = 7;
					}
				} else {
					if (accX > 3) {
						led = 3;
					} else if (accX < -3) {
						led = 5;
					} else {
						led = 4;
					}
				}
				
				serialManager.send(level+""+led);
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if (accY < 0) {
					accY *= -1;
				}
				player.play(accX, channel, SoundPlayer.MAX_VOLUME - (accY * 10), pressed);
			} catch (final InvalidMidiDataException e) {
				e.printStackTrace();
			}
			
		} else {
			
			final String[] split = message.split(" ");
			final int instrument = Integer.valueOf(split[1]);
			final int channel = Integer.valueOf(split[2]);//0-16
			
			try {
				player.setInstrument(instrument, channel);
			} catch (final InvalidMidiDataException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	// ---------------------------------------------
	// Accessor Methods
	// ---------------------------------------------
	
	public final SoundPlayer getSoundPlayer() {
		return player;
	}
	
}
