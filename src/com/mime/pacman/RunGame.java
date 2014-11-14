package com.mime.pacman;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class RunGame {
	static JFrame frame;
	
	public RunGame(){
		BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
		Display game = new Display(this);
		frame = new JFrame();
		frame.add(game);
		frame.setSize(Display.getGameWidth(), Display.getGameHeight());
		frame.getContentPane().setCursor(blank);
		frame.setTitle(Display.TITLE);
		frame.setBackground(Color.WHITE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		game.start();
		stopMenuThread();
	}

	public RunGame(RunGame oldGame) {
		oldGame.frame.setVisible(false);
		new RunGame();
	}

	public static int getX(){
		return frame.getX();
	}
	public static int getY(){
		return frame.getY();
	}
	
	private void stopMenuThread(){
		Display.getLauncherInstance().stopMenu();
	}
}
