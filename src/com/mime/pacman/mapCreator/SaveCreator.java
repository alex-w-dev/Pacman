package com.mime.pacman.mapCreator;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.mime.pacman.input.WindowHandler;

public class SaveCreator {
	JFrame frame;
	JPanel window;
	JTextField textField;
	JButton button;
	JLabel label;
	protected static boolean runnable = false;
	JFrame parentFrame;
	JFrame launcherFrame;
	BufferedImage image;
	
	public SaveCreator(JFrame launcherFrame, JFrame parentFrame, BufferedImage image){
		this.parentFrame = parentFrame;
		this.image = image;
		
		
		frame = new JFrame("Сохранить как");
		frame.setSize(400,100);
		frame.setLocationRelativeTo(null);

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowHandler(){
			public void windowClosing(WindowEvent event) {
				event.getWindow().setVisible(false);
				runnable = false;
			}
		});
		runnable = true;


		window = new JPanel();
		
		window.setLayout(new FlowLayout());
		label = new JLabel("Введите назвние новой карты");
		label.setFont(new Font("Verdana", 1,18));
		window.add(label);
		label = new JLabel("(после сохраниния необходимо презагрузить игру)");
		label.setFont(new Font("Verdana", 0,14));
		window.add(label);
		window.setLayout(new GridLayout(4,1));
		textField = new JTextField(30);
		window.add(textField);
		button = new JButton("Сохранить");
		window.add(button);
		button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					frame.dispose();
					parentFrame.dispose();
					DisplayCreator.running = false;
					File outputfile = new File("src/userLevels/"+textField.getText()+".png");
					ImageIO.write(image, "png", outputfile);
					launcherFrame.setVisible(true);
					runnable = false;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});
		
		frame.setVisible(true);
		frame.add(window);
		frame.pack();
		frame.repaint();
		
	}
}
