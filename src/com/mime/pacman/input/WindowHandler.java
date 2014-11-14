package com.mime.pacman.input;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.mime.pacman.mapCreator.DisplayCreator;

public class WindowHandler implements WindowListener{

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Автоматически созданная заглушка метода
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Автоматически созданная заглушка метода
		
	}

	@Override
	public void windowClosing(WindowEvent event) {
		event.getWindow().setVisible(false);
		DisplayCreator.running = false;
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Автоматически созданная заглушка метода
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Автоматически созданная заглушка метода
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Автоматически созданная заглушка метода
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Автоматически созданная заглушка метода
		
	}

}
