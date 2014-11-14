package com.mime.pacman.level;

import java.util.ArrayList;
import java.util.List;

import com.mime.pacman.entity.mob.Ghost;
import com.mime.pacman.graphics.Sprite;

public class Block {
	
	public boolean solid = false;

	public static Block solidWall = new SolidBlock();

	public List<Sprite> sprites = new ArrayList<Sprite>();
	public List<Sprite> fastes = new ArrayList<Sprite>();
	public List<Sprite> ices = new ArrayList<Sprite>();
	public List<Ghost> ghosts = new ArrayList<Ghost>();

	public void addSprite(Sprite sprite){
		sprites.add(sprite);
	}

	public void addFast(Sprite fast){
		fastes.add(fast);
	}
	
	public void addIce(Sprite ice){
		ices.add(ice);
	}

	public void addGhost(Ghost ghost){
		ghosts.add(ghost);
	}
	
	
	
	public void delSprite(int x, int z){
		for (int s = 0; s < sprites.size(); s++) {
			Sprite sprite = sprites.get(s);

			System.out.println(sprites.size());
			if(sprite.getX()==(double)x && sprite.getZ()==(double)z){
				System.out.println(s);
				sprites.remove(s);
			}
		}
	}
	
}
