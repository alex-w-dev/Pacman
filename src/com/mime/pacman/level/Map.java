package com.mime.pacman.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Map {

	public BufferedImage image = null;
	private int w, h;

	// loading image
	public BufferedImage loadImage(String path) {
		try {
			image = ImageIO.read(Map.class.getResource(path));
			this.w = image.getWidth();
			this.h = image.getHeight();
			return image;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// creating image
	public BufferedImage createImage(int w, int h, int canvasColour) throws IOException {
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		this.w = image.getWidth();
		this.h = image.getHeight();

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				image.setRGB(x, y, canvasColour);
			}
		}

		return image;
	}

	public int getWidth() {
		return w;
	}

	public int getHeight() {
		return h;
	}

	public BufferedImage getImage() {
		return image;
	}

	public int getColour(int x, int y) {
		if (image == null) {
			return 0;
		}
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (x > getWidth() - 1)
			x = getWidth() - 1;
		if (y > getHeight() - 1)
			y = getHeight() - 1;
		// get int colour
		int col = image.getRGB(x, y);
		// вычленяем цвета поотдельности
		int red = (col & 0xFF0000) >> 16;
		int green = (col & 0x00FF00) >> 8;
		int blue = (col & 0x0000FF);
		// преобразуем цвет в формат RGB (FF FF FF)
		int color = red << 16 | green << 8 | blue;
		return color;
	}

	public void makeBorder(int size, int colour) {
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				if ((y - size) < 0 || (x - size) < 0 || (y + size) >= image.getHeight()|| (x + size) >= image.getWidth()) {
					image.setRGB(x, y, colour);
				}
			}
		}
	}

	public void setRGB(int x, int y, int rgb) {
		// System.out.println(image.getHeight()+"/"+image.getWidth());;

		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (x > getWidth() - 1)
			x = getWidth() - 1;
		if (y > getHeight() - 1)
			y = getHeight() - 1;
		image.setRGB(x, y, rgb);
	}

	public int getCount(int colour) {
		int count = 0;
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				if (getColour(x, y) == colour) {
					count++;
				}
			}
		}
		return count;
	}

	public void clearThing(int colour) {
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				if (getColour(x, y) == colour) {
					image.setRGB(x, y, (-1));
				}
			}
		}
	}
}