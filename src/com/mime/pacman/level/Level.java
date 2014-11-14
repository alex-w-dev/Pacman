package com.mime.pacman.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mime.pacman.Game;
import com.mime.pacman.entity.Entity;
import com.mime.pacman.entity.mob.Ghost;
import com.mime.pacman.graphics.Sprite;

public class Level {
	public Block[] blocks;
	public Block[] bonusBlocks;
	public final int width;
	public final int height;
	final Random random = new Random();
	public static Map map;
	public static Map coinMap;
	public static int delBlockX = -1;
	public static int delBlockZ = -1;
	public static String myMap;

	private List<Entity> entities = new ArrayList<Entity>();

	public Level(int level) {
		if (myMap == null) {
			map = new Map();
			map.loadImage("/levels/level_" + level + ".png");
			coinMap = new Map();
			coinMap.loadImage("/levels/level_" + level + ".png");
		} else {
			map = new Map();
			map.loadImage("/userLevels/" + myMap + ".png");
			coinMap = new Map();
			coinMap.loadImage("/userLevels/" + myMap + ".png");
		}
		this.width = map.getHeight();
		this.height = map.getHeight();
		blocks = new Block[width * height];
		bonusBlocks = new Block[width * height];
		generateLevel();
		map.clearThing(0xFFFF00);
		map.clearThing(0x00FF00);
		map.clearThing(0x00FFFF);
	}

	public void tick() {
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).tick();
		}
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void generateLevel() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Block block = null;
				if (map.getColour(x, y) == 0x000000) {
					block = new SolidBlock();
				} else {
					block = new Block();
					if (map.getColour(x, y) == 0x0000ff) {
						block.addGhost(new Ghost(x, 0.5, y));
					}
					if (map.getColour(x, y) == 0xfff200 || map.getColour(x, y) == 0xffff00) {
						block.addSprite(new Sprite(x, 0.5, y));
					}
					if (map.getColour(x, y) == 0x00ff00) {
						block.addFast(new Sprite(x, 0.5, y));
					}
					if (map.getColour(x, y) == 0x00ffff) {
						block.addIce(new Sprite(x, 0.5, y));
					}
					if (map.getColour(x, y) == 0xff0000) {
						Game.startX = x * 16;
						Game.startZ = y * 16;
					}
				}
				blocks[x + y * width] = block;
				bonusBlocks[x + y * width] = block;
			}
		}

	}

	public Block create(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return Block.solidWall;
		}

		return blocks[x + y * width];

	}

	public Block createSprites(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return Block.solidWall;
		}
		if (delBlockX != (-1) && delBlockZ != (-1)) {
			bonusBlocks[delBlockX + delBlockZ * width] = new Block();
			delBlockX = -1;
			delBlockZ = -1;
		}

		return bonusBlocks[x + y * width];

	}

}
