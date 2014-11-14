package com.mime.pacman;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.mime.pacman.entity.mob.Ghost;
import com.mime.pacman.entity.mob.Player;
import com.mime.pacman.graphics.Screen;
import com.mime.pacman.input.InputHandler;
import com.mime.pacman.level.Level;
import com.mime.pacman.mapCreator.DisplayCreator;
import com.mime.pacman.qui.Launcher;

public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	// properties:
	public static int width = 800;
	public static int height = 600;
	public static int mobSpeed = 1;
	public static int fora = 0;

	public static final String TITLE = "PacMan";

	private AffineTransform afineTransform;
	private Graphics2D g2d;
	private Thread thread;
	private Screen screen;
	private Game game;
	private BufferedImage img;
	private boolean running = false;
	private int[] pixels;
	private InputHandler input;
	private int newX = 0;
	private int oldX = 0;
	private int fps;
	public static int selection = 0;
	public static int MouseSpeed;
	static Launcher launcher;

	private Robot robot;
	private RunGame oldGame;

	public Display(RunGame oldGame) {
		this.oldGame = oldGame;
		Dimension size = new Dimension(getGameWidth(), getGameHeight());
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		// настройки для миникарты
		afineTransform = new AffineTransform();
		// расположение картинки
		afineTransform.translate(110, getGameHeight() - 140);
		// поворот картинки
		afineTransform.rotate(Math.PI * 2);
		// размер картинки
		afineTransform.scale(-6.4, 6.4);

		screen = new Screen(getGameWidth(), getGameHeight());
		input = new InputHandler();
		game = new Game(input);
		img = new BufferedImage(getGameWidth(), getGameHeight(), BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
	}

	public static Launcher getLauncherInstance() {
		if (launcher == null) {
			launcher = new Launcher(0);
		}
		return launcher;
	}

	public static int getGameWidth() {

		return width;
	}

	public static int getGameHeight() {

		return height;
	}

	public synchronized void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this, "game");
		thread.start();
	}

	public synchronized void stop() {
		if (!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// new
	// public void run() {
	// long previousTime = System.nanoTime();
	// double ns = 1000000000.0 / 60.0;
	// double delta = 0;
	// int frames = 0;
	// int updates = 0;
	// long timer = System.currentTimeMillis();
	//
	// requestFocus();
	// while (running) {
	// long currentTime = System.nanoTime();
	// delta += (currentTime - previousTime) / ns;
	// previousTime = currentTime;
	//
	// if (delta >= 1) {
	// tick();
	// updates++;
	// delta--;
	// }
	//
	// render();
	// frames++;
	//
	// while (System.currentTimeMillis() - timer > 1000) {
	// timer += 1000;
	// System.out.println(updates + " updates, " + frames + " fps");
	// fps = frames;
	// frames = 0;
	// updates = 0;
	// }
	//
	// }
	// }

	// old
	public void run() {
		int frames = 0;
		double unprocessedSeconds = 0;
		long previousTime = System.nanoTime();
		double secondsPerTick = 1 / 60.0;
		int tickCount = 0;
		boolean ticked = false;
		while (running) {
			// исход игры:
			if (Player.cached) {
				Object[] options = { "Пробовать снова", "Выход" };
				int n = JOptionPane.showOptionDialog(new JFrame(), "Что дальше?", "Вы проиграли - пойман привидением :(", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				switch (n) {
				case 0:
					Player.cached = false;
					new RunGame(oldGame);
					break;
				case 1:
					System.exit(0);
					break;
				}
			}

			if (Display.fora >= Level.coinMap.getCount(0xFFFF00)) {

				Object[] options = { "Следующий уровень", "Пройти снова", "Выход" };
				int n = JOptionPane.showOptionDialog(new JFrame(), "Что дальше?", "Победа! - уровень пройден!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				switch (n) {
				case 0:
					Game.levelNumber++;
					if (Game.levelNumber > 25)
						Game.levelNumber = 25;
					Player.cached = false;
					new RunGame(oldGame);
					break;
				case 1:
					Player.cached = false;
					new RunGame(oldGame);
					break;
				case 2:
					System.exit(0);
					break;
				}
			}

			long currentTime = System.nanoTime();
			long passedTime = currentTime - previousTime;
			previousTime = currentTime;
			unprocessedSeconds += passedTime / 1000000000.0;
			// launcher.updateFrame();
			requestFocus();

			while (unprocessedSeconds > secondsPerTick) {
				tick();
				unprocessedSeconds -= secondsPerTick;
				ticked = true;
				tickCount++;
				if (tickCount % 60 == 0) {
					System.out.println(frames + "fpsdd");
					fps = frames;
					previousTime += 1000;
					frames = 0;
					if (InputHandler.timeRun > 0) {
						InputHandler.timeRun--;
					} else {
						InputHandler.run = false;
					}
					if (Ghost.timeFreez > 0) {
						Ghost.timeFreez--;
					} else {
						Ghost.freez = false;
					}

				}
			}
			if (ticked) {
				frames++;
				render();
			}

		}
	}

	// private void tick() {
	// input.tick();
	// game.tick();
	// // новая позиция мыши считана
	// newX = InputHandler.MouseX;
	//
	// if (newX > oldX) {
	// Player.turnRight = true;
	// }
	// if (newX < oldX) {
	// Player.turnLeft = true;
	// }
	// if (newX == oldX) {
	// Player.turnRight = false;
	// Player.turnLeft = false;
	// }
	// MouseSpeed = Math.abs(newX - oldX);
	// oldX = newX;
	// }

	private void tick() {
		input.tick();
		game.tick();
		// новая позиция мыши считана
		newX = RunGame.getX() + InputHandler.MouseX + 3; // фиг занет счего
		// смещение на 3,
		// надо посмотреть
		// позже!!!!
		// мышь поставлена на место (в центр экрана)
		int centerX = RunGame.getX() + (width / 2);
		int centerY = RunGame.getY() + (height / 2);
		robot.mouseMove(centerX, centerY);

		if (newX > oldX) {
			Player.turnRight = true;
		}
		if (newX < oldX) {
			Player.turnLeft = true;
		}
		if (newX == oldX) {
			Player.turnRight = false;
			Player.turnLeft = false;
		}
		MouseSpeed = Math.abs(newX - oldX);
		oldX = centerX;
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		screen.render(game);

		for (int i = 0; i < getGameWidth() * getGameHeight(); i++) {
			pixels[i] = screen.pixels[i];
		}

		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, getGameWidth(), getGameHeight(), null);
		// монетки
		g.setFont(new Font("Veranda", 2, 15));
		g.setColor(Color.yellow);
		g.drawString("Монет осталось:", 10, 25);
		g.setFont(new Font("Veranda", 2, 40));
		g.drawString(Level.coinMap.getCount(0xFFFF00) + "", 40, 65);

		if (InputHandler.timeRun > 0) {
			g.setColor(Color.green);
			g.drawString(InputHandler.timeRun + "", getGameWidth() / 2 - 80, 40);
		}

		if (Ghost.timeFreez > 0) {
			g.setColor(Color.cyan);
			g.drawString(Ghost.timeFreez + "", getGameWidth() / 2 + 80, 40);
		}

		g.setFont(new Font("Veranda", 2, 15));
		g.setColor(Color.green);
		g.drawString("Фора: " + Display.fora, 40, 85);
		// карта
		g2d = (Graphics2D) g;
		// g2d.drawImage(Level.map.image, afineTransform, null);
		g.drawImage(Level.map.image, 110, getGameHeight() - 140, -100, 100, null);

		// g.dispose();
		bs.show();

	}

	public static void main(String[] args) {
		getLauncherInstance();

	}
}
