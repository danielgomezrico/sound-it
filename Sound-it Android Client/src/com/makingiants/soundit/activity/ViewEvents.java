package com.makingiants.soundit.activity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.makingiants.soundit.R;
import com.makingiants.soundit.model.MessageSender;

public class ViewEvents extends Activity implements SensorEventListener, OnSeekBarChangeListener,
        OnKeyListener {
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	
	private MessageSender messageSender;
	
	private int channel;
	private int instrument;
	
	private int lastAccX;
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_events);
		
		// Set values
		channel = 0;
		instrument = 0;
		lastAccX = 0;
		
		// Init accelerometer sensor
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		messageSender = MessageSender.getInstance();
		
		// Set listeners
		final EditText textInstrument = (EditText) findViewById(R.id.view_events_text_instrument);
		textInstrument.setText(Integer.toString(instrument));
		textInstrument.setOnKeyListener(this);
		
		((TextView) findViewById(R.id.view_events_text_channel)).setText("Channel: " + channel);
		((SeekBar) findViewById(R.id.view_events_seekbar_channel)).setOnSeekBarChangeListener(this);
		
	}
	
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	}
	
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		messageSender.sendMidiInstrumentMessage(46, 0);
	}
	
	// ---------------------------------------------
	// SensorEventListener overrides
	// ---------------------------------------------
	
	public void onAccuracyChanged(final Sensor sensor, final int accuracy) {
	}
	
	public void onSensorChanged(final SensorEvent event) {
		
		final float ax = event.values[0];
		float ay = event.values[1];
		//float az = event.values[2];
		
		if (ax > 0.7 || ax < -0.7) {
			final int axInt = (int) ax;
			
			if (ay < 0) {
				ay *= -1;
			}
			
			final int volume = MessageSender.MAX_VOLUME - (int) ay * 10;
			
			if (lastAccX != axInt) {
				messageSender.sendMidiValueMessage(axInt, 0, volume, true);
			}
			
			lastAccX = axInt;
		}
		
	}
	
	// ---------------------------------------------
	// SensorEventListener overrides
	// ---------------------------------------------
	
	@Override
	public void onProgressChanged(final SeekBar seekBar, final int arg1, final boolean arg2) {
		
		channel = arg1;
		((TextView) findViewById(R.id.view_events_text_channel)).setText("Channel: " + channel);
		
	}
	
	@Override
	public void onStartTrackingTouch(final SeekBar arg0) {
		
	}
	
	@Override
	public void onStopTrackingTouch(final SeekBar arg0) {
		messageSender.sendMidiInstrumentMessage(instrument, channel);
	}
	
	// ---------------------------------------------
	// OnKeyListener overrides
	// ---------------------------------------------
	
	@Override
	public boolean onKey(final View v, final int keyCode, final KeyEvent event) {
		final EditText text = (EditText) v;
		try {
			final String value = text.getText().toString();
			
			instrument = Integer.parseInt(value);
			
			messageSender.sendMidiInstrumentMessage(instrument, channel);
			
		} catch (final NumberFormatException ex) {
			Log.d(getString(R.string.app_name), "NumberFormatException in ViewEvents 1", ex);
		}
		
		return false;
	}
}
