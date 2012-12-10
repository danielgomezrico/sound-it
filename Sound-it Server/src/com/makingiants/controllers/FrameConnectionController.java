/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.makingiants.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.table.DefaultTableModel;

import com.makingiants.model.managers.MessageManager;
import com.makingiants.model.server.UDPServer;
import com.makingiants.views.FrameConnection;

public class FrameConnectionController implements ActionListener {
	
	// ---------------------------------------------
	// Attributes
	// ---------------------------------------------
	
	private final FrameConnection frame;
	private final UDPServer server;
	
	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------
	
	public FrameConnectionController() {
		
		frame = new FrameConnection(this);
		frame.setVisible(true);
		
		server = new UDPServer();
		frame.textfieldIP.setText(server.getMyIP());
		frame.textfieldPort.setText(String.valueOf(server.getPort()));
		
		frame.setVisible(true);
		
		server.start();
		frame.buttonStart.setText("Stop");
		frame.textfieldPort.setEditable(false);
		
		// Show and send instruments
		final DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Instruments");
		final String[] instruments = MessageManager.getInstance().getSoundPlayer()
		        .getAvaliableInstruments();
		for (int i = 0; i < instruments.length; i++) {
			model.addRow(new String[] { instruments[i] });
		}
		
		frame.table.setModel(model);
		
	}
	
	// ---------------------------------------------
	// Events
	// ---------------------------------------------
	
	@Override
	public void actionPerformed(final ActionEvent ae) {
		if (!server.isConnected()) {
			
			server.start();
			frame.textfieldPort.setEditable(false);
			frame.buttonStart.setText("Stop");
			
		} else {
			
			server.stop();
			frame.textfieldPort.setEditable(true);
			frame.buttonStart.setText("Start");
			
		}
	}
	
}
