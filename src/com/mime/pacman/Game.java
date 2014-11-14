package com.mime.pacman;

import com.mime.pacman.entity.mob.Player;
import com.mime.pacman.input.InputHandler;
import com.mime.pacman.level.Level;

public class Game {
	public int time;
	public Player player;
	public Player[] players;
	public Level level;
	public static int levelNumber = 1;
	public static int startX = 0;
	public static int startZ = 0;

	public Game(InputHandler input) {
		level = new Level(levelNumber);
		player = new Player(input, (double)(startX + 8.0), (double)(startZ + 8.0));
		level.addEntity(player);
	}

	public void tick() {
		time++;
		level.tick();
	}
}
