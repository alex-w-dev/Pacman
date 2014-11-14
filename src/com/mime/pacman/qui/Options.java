package com.mime.pacman.qui;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mime.pacman.Configuration;
import com.mime.pacman.Display;

public class Options extends JFrame {
	private static final long serialVersionUID = 1L;

	private int width = 540;
	private int height = 440;
	private JButton OK;
	private JTextField twidth, theight, tFora;
	private JLabel lwidth, lheight, lresolution, lwinTitle, lresolutionL, lmobSpeedCh, lFora;
	private Choice resolution = new Choice();
	private Choice mobSpeedCh = new Choice();
	int w = 0, h = 0, mobSpeed = 1, fora = 0;
	private int buttonWidth = 80;
	private int buttonHeight = 40;
	JPanel window = new JPanel();
	Configuration config = new Configuration();

	public Options() {
		setTitle("Настройки");
		setSize(new Dimension(width, height));
		add(window);
		setLocationRelativeTo(null); // расположение окна
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		window.setLayout(null);
		drawButtons();
		window.repaint();
	}

	private void drawButtons() {
		lwinTitle = new JLabel("Настройки");
		lwinTitle.setFont(new Font("Verdana", 1, 40));
		lwinTitle.setBounds(140, 5, 300, 40);
		window.add(lwinTitle);

		resolution.setBounds(130, 80, 80, 25);
		resolution.add("640, 480");
		resolution.add("800, 600");
		resolution.add("1024, 768");
		int sel = 1;
		switch (Display.width) {
		case 640:
			sel = 0;
			break;
		case 1024:
			sel = 2;
			break;
		default:
			sel = 1;
		}
		resolution.select(sel);
		window.add(resolution);

		lresolution = new JLabel("Разрешение:");
		lresolution.setBounds(60, 80, 80, 20);
		window.add(lresolution);

		mobSpeedCh.setBounds(370, 80, 80, 25);
		mobSpeedCh.add("x1");
		mobSpeedCh.add("x2");
		mobSpeedCh.add("x3");
		mobSpeedCh.add("x4");
		mobSpeedCh.add("x5");
		mobSpeedCh.add("x6");
		mobSpeedCh.add("x7");
		mobSpeedCh.add("x8");
		mobSpeedCh.add("x9");
		mobSpeedCh.add("x10");
		mobSpeedCh.select(Display.mobSpeed - 1);
		window.add(mobSpeedCh);

		lmobSpeedCh = new JLabel("Скорость духов:");
		lmobSpeedCh.setBounds(260, 80, 80, 20);
		window.add(lmobSpeedCh);

		lFora = new JLabel("Фора монет: ");
		lFora.setBounds(260, 120, 120, 20);
		window.add(lFora);

		tFora = new JTextField();
		tFora.setBounds(370, 120, 80, 20);
		tFora.setText("" + Display.fora);
		window.add(tFora);

		OK = new JButton("OK");
		OK.setBounds((width - 100), (height - 70), buttonWidth, buttonHeight - 10);
		window.add(OK);

		// lresolutionL = new JLabel("Задать вручную:");
		// lresolutionL.setBounds(60,110,120,20);
		// window.add(lresolutionL);
		//
		// lwidth = new JLabel("Высота: ");
		// lwidth.setBounds(60,140,120,20);
		// window.add(lwidth);
		//
		// lheight = new JLabel("Ширина: ");
		// lheight.setBounds(60,170,120,20);
		// window.add(lheight);
		//
		// twidth = new JTextField();
		// twidth.setBounds(130,140,80,20);
		// window.add(twidth);
		//
		// theight = new JTextField();
		// theight.setBounds(130,170,80,20);
		// window.add(theight);

		OK.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
				// new Launcher(0);
				dropResolution();
				dropMobSpeed();
				config.saveConfiguration("width", w);
				config.saveConfiguration("height", h);
				config.saveConfiguration("mobSpeed", mobSpeed);
				config.saveConfiguration("fora", parseFora());

			}

		});
	}

	private void dropResolution() {
		int selection = resolution.getSelectedIndex();

		if (selection == 0) {
			w = 640;
			h = 480;
		}
		if (selection == -1 || selection == 1) {
			w = 800;
			h = 600;
		}
		if (selection == 2) {
			w = 1024;
			h = 768;
		}
	}

	private void dropMobSpeed() {
		int selection = mobSpeedCh.getSelectedIndex();

		if (selection == 0) {
			mobSpeed = 1;
		}
		if (selection == -1 || selection == 1) {
			mobSpeed = 2;
		}
		if (selection == 2) {
			mobSpeed = 3;
		}
		if (selection == 3) {
			mobSpeed = 4;
		}
		if (selection == 4) {
			mobSpeed = 5;
		}
		if (selection == 5) {
			mobSpeed = 6;
		}
		if (selection == 6) {
			mobSpeed = 7;
		}
		if (selection == 7) {
			mobSpeed = 8;
		}
		if (selection == 8) {
			mobSpeed = 9;
		}
		if (selection == 9) {
			mobSpeed = 10;
		}
	}

	private int parseWidth() {
		try {
			int w = Integer.parseInt(twidth.getText());
			return w;
		} catch (NumberFormatException e) {
			dropResolution();
			return w;
		}
	}

	private int parseHeight() {
		try {
			int h = Integer.parseInt(theight.getText());
			return h;
		} catch (NumberFormatException e) {
			dropResolution();
			return h;
		}
	}

	private int parseFora() {
		try {
			int f = Integer.parseInt(tFora.getText());
			return f;
		} catch (NumberFormatException e) {
			dropResolution();
			return fora;
		}
	}
}
