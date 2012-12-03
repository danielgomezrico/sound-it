/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.makingiants.model.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.makingiants.model.managers.MessageManager;

/**
 *
 * @author danielgomezrico
 */
public class UDPHandler extends IoHandlerAdapter {
	
	// ---------------------------------------------
	// Attributes
	// ---------------------------------------------
	
	private final MessageManager manager;
	private IoSession session;
	
	// ---------------------------------------------
	// Constructor
	// ---------------------------------------------
	
	public UDPHandler() {
		manager = MessageManager.getInstance();
	}
	
	// ---------------------------------------------
	// Methods
	// ---------------------------------------------
	
	public void close() throws NullPointerException {
		try {
			session.close(true);
		} catch (final NullPointerException e) {
			new NullPointerException("There's no session to close");
		}
	}
	
	@Override
	public void sessionOpened(final IoSession session) throws Exception {
		super.sessionOpened(session);
		this.session = session;
	}
	
	@Override
	public void exceptionCaught(final IoSession session, final Throwable cause) throws Exception {
		session.close(true);//true - avoid queued messages and close now
	}
	
	@Override
	public void messageReceived(final IoSession session, final Object message) throws Exception {
		
		if (message != null) {
			final String text = new String(((IoBuffer) message).array());
			manager.manage(text);
		}
		
	}
}
