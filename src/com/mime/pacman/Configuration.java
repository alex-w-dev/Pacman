package com.mime.pacman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.ImageIO;

public class Configuration {

	private String path = "src/settingus/config.xml";

	Properties properties = new Properties();

	public void saveConfiguration(String key, int value) {
		try {
			File file = new File(path);
			boolean exist = file.exists();
			if (!exist) {
				file.createNewFile();
			}
			OutputStream write = new FileOutputStream(path);
			properties.setProperty(key, Integer.toString(value));
			properties.storeToXML(write, "Resolution");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadConfiguration() {
		try {
			InputStream read = new FileInputStream(path);
			properties.loadFromXML(read);
			String width = properties.getProperty("width");
			String height = properties.getProperty("height");
			String mobSpeed = properties.getProperty("mobSpeed");
			String fora = properties.getProperty("fora");
			setResolurion(Integer.parseInt(width), Integer.parseInt(height), Integer.parseInt(mobSpeed), Integer.parseInt(fora));
		} catch (FileNotFoundException e) {
			saveConfiguration("width", 800);
			saveConfiguration("height", 600);
			saveConfiguration("mobSpeed", 1);
			saveConfiguration("fora", 1);
			loadConfiguration();
		} catch (NumberFormatException e) {
			saveConfiguration("width", 800);
			saveConfiguration("height", 600);
			saveConfiguration("mobSpeed", 1);
			saveConfiguration("fora", 1);
			loadConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setResolurion(int width, int height, int mobSpeed, int fora) {
		Display.width = width;
		Display.height = height;
		Display.mobSpeed = mobSpeed;
		Display.fora = fora;
	}
}
