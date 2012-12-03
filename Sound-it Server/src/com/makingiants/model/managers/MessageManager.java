/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.makingiants.model.managers;

import javax.sound.midi.InvalidMidiDataException;

/**
 *
 * @author danielgomezrico
 */
public class MessageManager {
	
	// ---------------------------------------------
	// Attributes
	// ---------------------------------------------
	
	private final SoundPlayer player;
	
	private static MessageManager instance;
	
	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------
	
	private MessageManager() {
		player = new SoundPlayer();
		
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
	
	public void manage(final String message) {
		if (message.charAt(0) == 'v') {
			
			final String[] split = message.split(" ");
			final int val = Integer.valueOf(split[1]);
			final int channel = Integer.valueOf(split[2]);//0-16
			final int volume = Integer.valueOf(split[3]);//0-128
			// TODO: get when should I left pressed or not from message
			final boolean left_pressed = false;//Boolean.valueOf(split[4]);
			
			try {
				player.play(val, channel, volume, left_pressed);
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
