package com.mime.pacman.graphics;

import java.util.Random;

import com.mime.pacman.Game;
// все  методы и переменные из класса Render
public class Screen extends Render {

	private Render test;
	private Render3D render;

	public Screen(int width, int height) {
		super(width, height);
		Random random = new Random();
		render = new Render3D(width, height);
		test = new Render(256, 256);
		for (int i = 0; i < 256 * 256; i++) {
			test.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
		}
	}
	
    // здесь будем вызывать методы, которые будут рисовать пиксели ,  и затем выведем эти пиксели на экран
	public void render(Game game) {
		// обнуляем пиксели, перед следуйщей картинкой
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0;
		}
		// вызываем рисование
		render.floor(game);
		
		// вызываем метод, ограничивающий наш обзор
		render.renderDistanceLimiter();
		
		// выводим картнку на экран
		draw(render, 0, 0);
	}
}
