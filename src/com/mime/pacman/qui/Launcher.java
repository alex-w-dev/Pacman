package com.mime.pacman.qui;

import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.mime.pacman.Configuration;
import com.mime.pacman.Game;
import com.mime.pacman.RunGame;
import com.mime.pacman.input.InputHandler;
import com.mime.pacman.level.Level;
import com.mime.pacman.mapCreator.DisplayCreator;

public class Launcher extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	protected JPanel window = new JPanel();

	// сущность кнопки
	private JButton playMy, play, options, help, quit;
	// прямоугольник кнопки
	private Rectangle rplayMy, rplay, roptions, rhelp, rquit;
	Configuration config = new Configuration();

	private int width = 600;
	private int height = 300;
	protected int buttonWidth = 80;
	protected int buttonHeight = 40;
	protected DisplayCreator creator;
	boolean running = false;
	Thread thread;
	JFrame frame = new JFrame();

	public Launcher(int id) {
		try {
			// стиль кнопок Windows
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// убрали стандартное оформление окна
		frame.setUndecorated(true);
		// title окна
		frame.setTitle("Pacman Launcher");
		// задаем размеры
		frame.setSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// getContentPane().add(window);
		frame.add(this);
		// расположение окна center
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		window.setLayout(null);
		if (id == 0) {
			drawButtons();
		}
		InputHandler input = new InputHandler();
		addKeyListener(input);
		addFocusListener(input);
		addMouseListener(input);
		addMouseMotionListener(input);
		startMenu();
		// display.start();
		frame.repaint();
	}

	public void updateFrame() {
		if (InputHandler.dragged && DisplayCreator.running == false) {
			Point p = frame.getLocation();
			frame.setLocation(p.x + InputHandler.MouseDX - InputHandler.MousePX, p.y + InputHandler.MouseDY - InputHandler.MousePY);

			System.out.println(p.x);
		}
	}

	public void startMenu() {
		running = true;
		thread = new Thread(this, "menu");
		thread.start();
	}

	public void stopMenu() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		requestFocus();
		while (running) {
			try {
				renderMenu();
			} catch (IllegalStateException e) {
				// e.printStackTrace();
				System.out.println("Handled...");
			}
			updateFrame();

		}
	}

	private void renderMenu() throws IllegalStateException {
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 600, 300);
		try {

			g.drawImage(ImageIO.read(Launcher.class.getResource("/textures/menu_image.jpg")), 0, 0, 600, 300, null);

			if (DisplayCreator.running == false) {
				// аркада
				if (InputHandler.MouseX > 300 && InputHandler.MouseX < 300 + 250 && InputHandler.MouseY > 30 && InputHandler.MouseY < 30 + 30) {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/play_on.png")), 330, 30, 220, 30, null);
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 560, 32, 26, 22, null);
					if (InputHandler.MouseButton == 1) {
						InputHandler.MouseButton = 0;
						int levelCount = 25;
						String[] levels = new String[levelCount];
						for (int i = 0; i < levelCount; i++) {
							levels[i] = "" + (i + 1);
						}
						int n = JOptionPane.showOptionDialog(new JFrame(), "Уровни:", "Выберите уровень", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, levels, levels[0]);
						if (n >= 0 && n < levelCount) {
							Game.levelNumber = n + 1;
							config.loadConfiguration();
							frame.dispose(); // закрыли окно меню
							new RunGame();
						}
					}
				} else {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/play_off.png")), 330, 30, 220, 30, null);
				}

				// свои карты
				if (InputHandler.MouseX > 300 && InputHandler.MouseX < 300 + 250 && InputHandler.MouseY > 70 && InputHandler.MouseY < 70 + 30) {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/my_game_on.png")), 330, 70, 220, 30, null);
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 560, 72, 26, 22, null);

					if (InputHandler.MouseButton == 1) {
						InputHandler.MouseButton = 0;
						String frwe = JOptionPane.showInputDialog("Введите название вашей карты");
						System.out.println(frwe);
						if(frwe!=null){
							Level.myMap = frwe;
							config.loadConfiguration();
							frame.dispose(); // закрыли окно меню
							new RunGame();
						}
					}
				} else {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/my_game_off.png")), 330, 70, 220, 30, null);
				}

				// настройки
				if (InputHandler.MouseX > 300 && InputHandler.MouseX < 300 + 250 && InputHandler.MouseY > 110 && InputHandler.MouseY < 110 + 30) {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/options_on.png")), 330, 110, 220, 30, null);
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 560, 112, 26, 22, null);
					if (InputHandler.MouseButton == 1) {
						config.loadConfiguration();
						new Options();
					}
				} else {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/options_off.png")), 330, 110, 220, 30, null);
				}

				if (InputHandler.MouseX > 300 && InputHandler.MouseX < 300 + 250 && InputHandler.MouseY > 150 && InputHandler.MouseY < 150 + 30) {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/help_on.png")), 330, 150, 220, 30, null);
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 560, 152, 26, 22, null);
					if (InputHandler.MouseButton == 1) {
						InputHandler.MouseButton = 0;
						Object[] options = { "10x10", "20x20", "30x30", "50x50", "Отмена" };
						int n = JOptionPane.showOptionDialog(new JFrame(), "Возможеные размеры:", "Размер будущей карты", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

						switch (n) {
						case 0:
							creator = new DisplayCreator(10, 10, frame);
							frame.setVisible(false);
							break;

						case 1:
							creator = new DisplayCreator(20, 20, frame);
							frame.setVisible(false);
							break;

						case 2:
							creator = new DisplayCreator(30, 30, frame);
							frame.setVisible(false);
							break;

						case 3:
							creator = new DisplayCreator(50, 50, frame);
							frame.setVisible(false);
							break;
						}
						// frame.dispose();
					}
				} else {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/help_off.png")), 330, 150, 220, 30, null);
				}

				if (InputHandler.MouseX > 300 && InputHandler.MouseX < 300 + 250 && InputHandler.MouseY > 190 && InputHandler.MouseY < 190 + 30) {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/quit_on.png")), 330, 190, 220, 30, null);
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/arrow.png")), 560, 192, 26, 22, null);
					if (InputHandler.MouseButton == 1) {
						System.exit(0);
					}
				} else {
					g.drawImage(ImageIO.read(Launcher.class.getResource("/menu/quit_off.png")), 330, 190, 220, 30, null);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.dispose();
		bs.show();

	}

	private void drawButtons() {
		play = new JButton("Play!");
		rplay = new Rectangle((width / 2) - (buttonWidth / 2), 90, buttonWidth, buttonHeight);
		// применяем визуальную часть к сущности кнопки
		play.setBounds(rplay);
		window.add(play);

		options = new JButton("Options");
		roptions = new Rectangle((width / 2) - (buttonWidth / 2), 140, buttonWidth, buttonHeight);
		options.setBounds(roptions);
		window.add(options);

		help = new JButton("Help");
		rhelp = new Rectangle((width / 2) - (buttonWidth / 2), 190, buttonWidth, buttonHeight);
		help.setBounds(rhelp);
		window.add(help);

		quit = new JButton("Quit");
		rquit = new Rectangle((width / 2) - (buttonWidth / 2), 240, buttonWidth, buttonHeight);
		quit.setBounds(rquit);
		window.add(quit);

		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.loadConfiguration();
				frame.dispose(); // закрыли окно меню
				new RunGame();
			}
		});

		options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				new Options();
			}
		});

		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

}
