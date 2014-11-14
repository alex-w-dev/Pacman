package com.mime.pacman.input;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, FocusListener, MouseListener, MouseMotionListener {

	public boolean[] key = new boolean[68836];
	public static int MouseX;
	public static int MouseY;
	public static int MouseDX;
	public static int MouseDY;
	public static int MousePX;
	public static int MousePY;
	public static boolean dragged = false;
	public static int MouseButton =0;

	public boolean forward, back, left, right, rleft, rright, jump, crouch;
	public static boolean run = false;
	public static int timeRun = 0;
	
	
	public void tick(){
		forward = key[KeyEvent.VK_W];
		back = key[KeyEvent.VK_S];
		left = key[KeyEvent.VK_A];
		right = key[KeyEvent.VK_D];
		rright = key[KeyEvent.VK_RIGHT];
		rleft = key[KeyEvent.VK_LEFT];
		jump = key[KeyEvent.VK_SPACE];
		crouch = key[KeyEvent.VK_CONTROL];
		//run = key[KeyEvent.VK_SHIFT];
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		MouseDX = e.getX();
		MouseDY = e.getY();
		dragged = true;

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		MouseX = e.getX();
		MouseY = e.getY();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Автоматически созданная заглушка метода

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Автоматически созданная заглушка метода

	}

	@Override
	public void mousePressed(MouseEvent e) {
		MousePX = e.getX();
		MousePY = e.getY();
		MouseButton = e.getButton(); //если клик получаем код кнопки мыши (Int)


	}

	@Override
	public void mouseReleased(MouseEvent e) {
		dragged = false;
		MouseButton = 0;

	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Автоматически созданная заглушка метода

	}

	@Override
	public void focusLost(FocusEvent e) {
		for (int i = 0; i < key.length; i++) {
			key[i] = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode > 0 && keyCode < key.length) {
			key[keyCode] = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode > 0 && keyCode < key.length) {
			key[keyCode] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Автоматически созданная заглушка метода

	}

}
