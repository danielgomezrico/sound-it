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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.makingiants.soundit.R;
import com.makingiants.soundit.model.MessageSender;

public class ViewEvents extends Activity implements SensorEventListener, OnItemSelectedListener,
        OnKeyListener {
	
	private final static int INSTRUMENTS = 365;
	private final static int CHANNELS = 16;
	// ---------------------------------------------
	// Attributes
	// ---------------------------------------------
	
	private Spinner spinnerChannel, spinnerInstrument;
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	
	private MessageSender messageSender;
	
	private int channel;
	private int instrument;
	
	private int lastAccX;
	private boolean repeatedFilterDisabled;
	
	// ---------------------------------------------
	// Activity overrides
	// ---------------------------------------------
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_events);
		
		// Set values
		channel = 0;
		instrument = 0;
		lastAccX = 0;
		repeatedFilterDisabled = true;
		
		// Init accelerometer sensor
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		messageSender = MessageSender.getInstance();
		
		// Init spinner channel
		String[] channels = new String[CHANNELS];
		for (int i = 0; i < CHANNELS; i++) {
			channels[i] = "" + i;
		}
		//TODO: Change simple_spinner_item for other cool list
		spinnerChannel = ((Spinner) findViewById(R.id.view_events_spinner_channel));
		spinnerChannel.setAdapter(new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_dropdown_item, channels));
		spinnerChannel.setOnItemSelectedListener(this);
		
		// Init spinner instruments
		String[] instruments = new String[INSTRUMENTS];
		for (int i = 0; i < INSTRUMENTS; i++) {
			instruments[i] = "" + i;
		}
		//TODO: Change simple_spinner_item for other cool list
		spinnerInstrument = ((Spinner) findViewById(R.id.view_events_spinner_instrument));
		spinnerInstrument.setAdapter(new ArrayAdapter<String>(this,
		        android.R.layout.simple_spinner_dropdown_item, instruments));
		spinnerInstrument.setOnItemSelectedListener(this);
		
	}
	
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
	}
	
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
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
			
			if (lastAccX != axInt || repeatedFilterDisabled) {
				messageSender.sendMidiValueMessage(axInt, 0, volume, true);
			}
			
			lastAccX = axInt;
		}
		
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
	
	// ---------------------------------------------
	// OnItemSelectedListener overrides
	// ---------------------------------------------
	
	@Override
	public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
		if (view.getId() == R.id.view_events_spinner_channel) {
			channel = Integer.valueOf(adapter.getItemAtPosition(pos).toString());
			
		} else {
			instrument = Integer.valueOf(adapter.getItemAtPosition(pos).toString());
		}
		Log.d("asd", channel + " " + instrument);
		//channel = ?
		messageSender.sendMidiInstrumentMessage(instrument, channel);
		
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	// ---------------------------------------------
	// Checkbox events
	// ---------------------------------------------
	public void onCheckboxClicked(View view) {
		// Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();
		
		// Check which checkbox was clicked
		switch (view.getId()) {
			case R.id.view_events_checkbox_repeater:
				if (checked) {
					repeatedFilterDisabled = true;
				} else {
					repeatedFilterDisabled = false;
				}
				break;
		}
	}
}
