package com.makingiants.model.managers;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

public class SoundPlayer {
	private final static int MIN_NOTE = 40;
	private final static int MAX_NOTE = 90;
	public final static int MAX_VOLUME = 120;
	private final static int MIN_VOLUME = 0;
	
	private int sound_frecuency = 60;
	
	//Synth parameters
	private Synthesizer synth;
	private ShortMessage message;
	private Receiver receiver;
	
	public SoundPlayer() {
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
			
			message = new ShortMessage();
			
			receiver = synth.getReceiver();
		} catch (final MidiUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Make a sound using frecuency_offset param and sound_frecuency attribute.
	 * 
	 * TODO:Play in other thread
	 * 
	 * @param frecuency_offset
	 * @param channel
	 * @param volume
	 * @param pressed
	 * @throws InvalidMidiDataException
	 */
	public void play(final int frecuency_offset, final int channel, int volume, boolean pressed)
	        throws InvalidMidiDataException {
		// Calculate new sound
		sound_frecuency = 60 + frecuency_offset * 2;
		
		// Check Limits
		if (sound_frecuency > MAX_NOTE) {
			sound_frecuency = MAX_NOTE;
		} else if (sound_frecuency < MIN_NOTE) {
			sound_frecuency = MIN_NOTE;
		}
		
		if (volume > MAX_VOLUME) {
			volume = MAX_VOLUME;
		} else if (volume < MIN_VOLUME) {
			volume = MIN_VOLUME;
		}
		
		//channel, pitch, volume
		message.setMessage(ShortMessage.NOTE_ON, channel, sound_frecuency, volume);
		receiver.send(message, -1); // -1 means no time stamp
		
		if (pressed) {
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		message.setMessage(ShortMessage.NOTE_OFF, channel, sound_frecuency, volume);
		receiver.send(message, -1); // -1 means no time stamp
		
	}
	
	public void setInstrument(final int instrument, final int channel) throws InvalidMidiDataException {
		final MidiChannel[] channels = synth.getChannels();
		final Instrument[] instruments = synth.getAvailableInstruments();
		channels[channel].programChange(instruments[instrument].getPatch().getProgram());
		
		//message.setMessage(ShortMessage.PROGRAM_CHANGE, channel, instrument, 0);
		//receiver.send(message, -1);
	}
	
	public String[] getAvaliableInstruments() {
		
		final Instrument[] k = synth.getAvailableInstruments();
		final String[] result = new String[k.length];
		
		for (int i = 0; i < k.length; i++) {
			result[i] = i + " " + k[i].getName();
		}
		
		return result;
	}
}
