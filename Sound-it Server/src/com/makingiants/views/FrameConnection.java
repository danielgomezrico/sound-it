package com.makingiants.views;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import com.makingiants.controllers.FrameConnectionController;

@SuppressWarnings("serial")
public class FrameConnection extends JFrame {
	
	private final JPanel contentPane;
	public JTextField textfieldIP;
	public JTextField textfieldPort;
	public JButton buttonStart;
	public JTable table;
	private final JScrollPane scrollPane;
	
	/**
	 * Create the frame.
	 */
	public FrameConnection(final FrameConnectionController controller) {
		setResizable(false);
		setTitle("Sound-It Server");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 342, 315);
		contentPane = new JPanel();
		//contentPane.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		contentPane.setBackground(new Color(82, 82, 82));
		contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JLabel lblIp = new JLabel("Local IP:");
		lblIp.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblIp.setForeground(UIManager.getColor("Button.highlight"));
		lblIp.setBounds(42, 17, 61, 16);
		contentPane.add(lblIp);
		
		textfieldIP = new JTextField();
		textfieldIP.setEditable(false);
		textfieldIP.setHorizontalAlignment(SwingConstants.CENTER);
		textfieldIP.setText("192.168.1.1");
		textfieldIP.setBounds(115, 11, 208, 28);
		contentPane.add(textfieldIP);
		textfieldIP.setColumns(10);
		
		textfieldPort = new JTextField();
		textfieldPort.setToolTipText("Change this number only if the application is not working");
		textfieldPort.setText("1111\n");
		textfieldPort.setHorizontalAlignment(SwingConstants.CENTER);
		textfieldPort.setColumns(10);
		textfieldPort.setBounds(115, 45, 208, 28);
		contentPane.add(textfieldPort);
		
		buttonStart = new JButton("Start\n");
		buttonStart.setBounds(6, 241, 330, 46);
		buttonStart.addActionListener(controller);
		
		contentPane.add(buttonStart);
		
		final JLabel label = new JLabel("Port :");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		label.setBounds(62, 51, 41, 16);
		contentPane.add(label);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 79, 306, 156);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		scrollPane.setViewportView(table);
	}
}
