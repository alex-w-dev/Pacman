package com.mime.pacman.mapCreator;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.mime.pacman.Configuration;
import com.mime.pacman.RunGame;
import com.mime.pacman.graphics.ColoursConstants;
import com.mime.pacman.input.InputHandler;
import com.mime.pacman.input.WindowHandler;
import com.mime.pacman.level.Level;
import com.mime.pacman.level.Map;
import com.mime.pacman.qui.Launcher;

public class DisplayCreator extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	protected JPanel window = new JPanel();
	Configuration config = new Configuration();

	private AffineTransform afineTransform;
	private Graphics2D g2d;
	private SaveCreator saveMap;
	private int width = 600;
	private int height = 300;
	private int redColour, blueColour, whiteColour, yellowColour, blackColour, greenColour, iceColour;
	private Map coinWord, fastWord, ghostWord, iceWord, playerWord, saveWord, wallWord, whiteWord;
	private Map creatorBG, createrBlackFon;
	private Map newMap, redButton, blueButton, whiteButton, yellowButton, blackButton, greenButton, iceButton, selectedColor;
	private double xRaznMap;
	private double yRaznMap;
	private int xSelectedColor = 296, ySelectedColor = 180;
	protected int buttonWidth = 20;
	protected int buttonHeight = 20;
	protected int wordWidth = 100;
	protected int wordHeight = 20;
	protected DisplayCreator creator;
	public static boolean running = false;
	Thread thread;
	private int brushColour = -1;

	JFrame frame = new JFrame();
	JFrame launcherFrame;

	public DisplayCreator(int w, int h, JFrame launcherFrame) {
		this.launcherFrame = launcherFrame;
		try {
			// стиль кнопок Windows
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		running = true;
		this.xRaznMap = 200.0 / w;
		this.yRaznMap = 200.0 / h;

		newMap = new Map();
		try {
			newMap.createImage(w, h, -1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// // настройки для миникарты
		// afineTransform = new AffineTransform();
		// // расположение картинки
		// afineTransform.translate(110, height-40);
		// // поворот картинки
		// afineTransform.rotate(Math.PI/2);
		// // размер картинки
		// afineTransform.scale(-6.4, 6.4);

		// title окна
		frame.setTitle("Создание карт");
		// задаем размеры
		frame.setSize(new Dimension(width, height));

		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowHandler(){
			public void windowClosing(WindowEvent event) {
				event.getWindow().setVisible(false);
				DisplayCreator.running = false;
				launcherFrame.setVisible(true);
			}
		});

		frame.add(this);
		// расположение окна center
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);

		InputHandler.MouseButton = 0;
		InputHandler input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);

		startWindow();
	}

	public void run() {
		requestFocus();
		while (running) {
			try {
				render();
			} catch (IllegalStateException e) {
				System.out.println("Handled...");
			}

		}

	}

	public void startWindow() {
		drawStatic();
		running = true;
		thread = new Thread(this, "menu");
		thread.start();
	}

	private void drawStatic() {
		try {
			int borderColour = -999;
			creatorBG = new Map();
			creatorBG.loadImage("/textures/menu_image.jpg");
			createrBlackFon = new Map();
			createrBlackFon.loadImage("/menu/createrBlackFon.png");
			
			
			coinWord = new Map();
			coinWord.loadImage("/menu/coin_word.png");
			fastWord = new Map();
			fastWord.loadImage("/menu/fast_word.png");
			ghostWord = new Map();
			ghostWord.loadImage("/menu/ghost_word.png");
			iceWord = new Map();
			iceWord.loadImage("/menu/ice_word.png");
			playerWord = new Map();
			playerWord.loadImage("/menu/player_word.png");
			saveWord = new Map();
			saveWord.loadImage("/menu/save_word.png");
			wallWord = new Map();
			wallWord.loadImage("/menu/wall_word.png");
			whiteWord = new Map();
			whiteWord.loadImage("/menu/white_word.png");
			
			
			selectedColor = new Map();
			selectedColor.loadImage("/menu/selected_color.png");
			redButton = new Map();
			redButton.createImage(buttonWidth, buttonHeight, ColoursConstants.RED);
			redButton.makeBorder(2, borderColour);
			blueButton = new Map();
			blueButton.createImage(buttonWidth, buttonHeight, ColoursConstants.BLUE);
			blueButton.makeBorder(2, borderColour);
			blackButton = new Map();
			blackButton.createImage(buttonWidth, buttonHeight, ColoursConstants.BLACK);
			blackButton.makeBorder(2, borderColour);
			greenButton = new Map();
			greenButton.createImage(buttonWidth, buttonHeight, ColoursConstants.GREEN);
			greenButton.makeBorder(2, borderColour);
			yellowButton = new Map();
			yellowButton.createImage(buttonWidth, buttonHeight, ColoursConstants.YELLOW);
			yellowButton.makeBorder(2, borderColour);
			iceButton = new Map();
			iceButton.createImage(buttonWidth, buttonHeight, ColoursConstants.ICE);
			iceButton.makeBorder(2, borderColour);
			whiteButton = new Map();
			whiteButton.createImage(buttonWidth, buttonHeight, ColoursConstants.WHITE);
			whiteButton.makeBorder(2, borderColour);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void readMouse() {
		// выбор цвета карандаша
		if (InputHandler.MouseX > 300 && InputHandler.MouseX < 300 + buttonWidth + 100 && InputHandler.MouseY > 30 && InputHandler.MouseY < 30 + buttonHeight) {
			if (InputHandler.MouseButton == 1) {
				brushColour = ColoursConstants.RED;
				xSelectedColor = 296;
				ySelectedColor = 30;
			}
		}
		if (InputHandler.MouseX > 300 && InputHandler.MouseX < 300 + buttonWidth + 100 && InputHandler.MouseY > 80 && InputHandler.MouseY < 80 + buttonHeight) {
			if (InputHandler.MouseButton == 1) {
				brushColour = ColoursConstants.BLUE;
				xSelectedColor = 296;
				ySelectedColor = 80;
			}
		}
		if (InputHandler.MouseX > 300 && InputHandler.MouseX < 300 + buttonWidth + 100 && InputHandler.MouseY > 130 && InputHandler.MouseY < 130 + buttonHeight) {
			if (InputHandler.MouseButton == 1) {
				brushColour = ColoursConstants.BLACK;
				xSelectedColor = 296;
				ySelectedColor = 130;
			}
		}
		if (InputHandler.MouseX > 300 && InputHandler.MouseX < 300 + buttonWidth + 100 && InputHandler.MouseY > 180 && InputHandler.MouseY < 180 + buttonHeight) {
			if (InputHandler.MouseButton == 1) {
				brushColour = ColoursConstants.WHITE;
				xSelectedColor = 296;
				ySelectedColor = 180;
			}
		}
		if (InputHandler.MouseX > 450 && InputHandler.MouseX < 450 + buttonWidth + 100 && InputHandler.MouseY > 30 && InputHandler.MouseY < 30 + buttonHeight) {
			if (InputHandler.MouseButton == 1) {
				brushColour = ColoursConstants.ICE;
				xSelectedColor = 446;
				ySelectedColor = 30;
			}
		}
		if (InputHandler.MouseX > 450 && InputHandler.MouseX < 450 + buttonWidth + 100 && InputHandler.MouseY > 80 && InputHandler.MouseY < 80 + buttonHeight) {
			if (InputHandler.MouseButton == 1) {
				brushColour = ColoursConstants.GREEN;
				xSelectedColor = 446;
				ySelectedColor = 80;
			}
		}
		if (InputHandler.MouseX > 450 && InputHandler.MouseX < 450 + buttonWidth + 100 && InputHandler.MouseY > 130 && InputHandler.MouseY < 130 + buttonHeight) {
			if (InputHandler.MouseButton == 1) {
				brushColour = ColoursConstants.YELLOW;
				xSelectedColor = 446;
				ySelectedColor = 130;
			}
		}
		if (InputHandler.MouseX > 400 && InputHandler.MouseX < 580 && InputHandler.MouseY > 200 && InputHandler.MouseY < 280) {
			if (InputHandler.MouseButton == 1 && SaveCreator.runnable == false) {
				saveMap = new SaveCreator(launcherFrame ,frame, newMap.image);
				//saveMap.run();
			}
		}

		// рисование на карте
		if (InputHandler.MouseX > 30 && InputHandler.MouseX < 30 + 200 && InputHandler.MouseY > 30 && InputHandler.MouseY < 30 + 200) {
			int x = InputHandler.MouseX - 30;
			int y = InputHandler.MouseY - 30;
			if (InputHandler.MouseButton == 1) {
				int xC = (int) Math.round(x / xRaznMap - 0.5);
				int yC = (int) Math.round(y / yRaznMap - 0.5);
				newMap.setRGB(xC, yC, brushColour);
			}
			if (InputHandler.dragged) {
				x = InputHandler.MouseDX - 30;
				y = InputHandler.MouseDY - 30;
				int xC = (int) Math.round(x / yRaznMap - 0.5);
				int yC = (int) Math.round(y / yRaznMap - 0.5);
				newMap.setRGB(xC, yC, brushColour);
			}
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		readMouse();

		Graphics g = bs.getDrawGraphics();
		// fon
		g.drawImage(creatorBG.image, 0, 0, width, height, null);
		g.drawImage(createrBlackFon.image, 270, 0, 320, 270, null);

		// knopki
		g.drawImage(redButton.image, 300, 30, buttonWidth, buttonHeight, null);
		g.drawImage(blueButton.image, 300, 80, buttonWidth, buttonHeight, null);
		g.drawImage(blackButton.image, 300, 130, buttonWidth, buttonHeight, null);
		g.drawImage(whiteButton.image, 300, 180, buttonWidth, buttonHeight, null);
		g.drawImage(iceButton.image, 450, 30, buttonWidth, buttonHeight, null);
		g.drawImage(greenButton.image, 450, 80, buttonWidth, buttonHeight, null);
		g.drawImage(yellowButton.image, 450, 130, buttonWidth, buttonHeight, null);
		// slova
		g.drawImage(playerWord.image, 327, 30, wordWidth, wordHeight, null);
		g.drawImage(ghostWord.image, 327, 80, wordWidth, wordHeight, null);
		g.drawImage(wallWord.image, 327, 130, wordWidth, wordHeight, null);
		g.drawImage(whiteWord.image, 327, 180, wordWidth, wordHeight, null);
		g.drawImage(iceWord.image, 477, 30, wordWidth, wordHeight, null);
		g.drawImage(fastWord.image, 477, 80, wordWidth, wordHeight, null);
		g.drawImage(coinWord.image, 477, 130, wordWidth, wordHeight, null);
		// selector
		g.drawImage(selectedColor.image, xSelectedColor, ySelectedColor, 100, 24, null);

		// save 
		g.drawImage(saveWord.image, 400, 220, 180, 50, null);
		// holst
		g.drawImage(newMap.image, 30, 30, 200, 200, null);
		bs.show();

	}

}
