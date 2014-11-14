package com.mime.pacman.entity.mob;

import com.mime.pacman.entity.Entity;
import com.mime.pacman.graphics.ColoursConstants;
import com.mime.pacman.input.InputHandler;
import com.mime.pacman.level.Level;

public class Mob extends Entity {

	public void move(int xa, int za, double rot, double walkSpeed) {
		if (xa != 0 && za != 0) {
			move(xa, 0, rot, walkSpeed);
			move(0, za, rot, walkSpeed);
			return;
		}

		double nx = (xa * Math.cos(rot) + za * Math.sin(rot)) * walkSpeed;
		double nz = (za * Math.cos(rot) - xa * Math.sin(rot)) * walkSpeed;

		// points in block:
		double correct = 16.0;
		double radius = 5.0;
		int wallColour = 0x000000;
		int coinColour = 0xffff00;
		int fastColour = 0x00ff00;
		int iceColour = 0x00ffff;
		double tnx = nx + x;
		double tnz = nz + z;
		boolean canX = true;
		boolean canZ = true;

		// center of person
		int centerPX = (int) Math.round((tnx - correct / 2.0) / correct);
		int centerPZ = (int) Math.round((tnz - correct / 2.0) / correct);
		int centerPCol = Level.coinMap.getColour(centerPX, centerPZ);
		// touching the coin:
		if (centerPCol == coinColour) {
			// если подобрал монетку
			Level.coinMap.setRGB(centerPX, centerPZ, (-1));
			Level.delBlockX = centerPX;
			Level.delBlockZ = centerPZ;
		}
		// touching the fast:
		if (centerPCol == fastColour) {
			// если подобрал монетку
			Level.coinMap.setRGB(centerPX, centerPZ, (-1));
			Level.delBlockX = centerPX;
			Level.delBlockZ = centerPZ;
			InputHandler.run = true;
			InputHandler.timeRun = 5;
		}
		// touching the ice:
		if (centerPCol == iceColour) {
			// если подобрал монетку
			Level.coinMap.setRGB(centerPX, centerPZ, (-1));
			Level.delBlockX = centerPX;
			Level.delBlockZ = centerPZ;
			Ghost.freez = true;
			Ghost.timeFreez = 5;
		}
		
		// 4 dotes around person:
		int d1 = Level.map.getColour((int) Math.round((tnx - correct / 2.0 + radius) / correct), (int) Math.round((tnz - correct / 2.0 + radius) / correct));
		int d2 = Level.map.getColour((int) Math.round((tnx - correct / 2.0 + radius) / correct), (int) Math.round((tnz - correct / 2.0 - radius) / correct));
		int d3 = Level.map.getColour((int) Math.round((tnx - correct / 2.0 - radius) / correct), (int) Math.round((tnz - correct / 2.0 + radius) / correct));
		int d4 = Level.map.getColour((int) Math.round((tnx - correct / 2.0 - radius) / correct), (int) Math.round((tnz - correct / 2.0 - radius) / correct));
		

		// если хоть одна точка зашла за текстуру
		if ((d1 == wallColour) || (d2 == wallColour) || (d3 == wallColour) || (d4 == wallColour)) {
			canX = false;
			canZ = false;

			// проверяем шаг по Х
			double tnx_x = nx + x;
			double tnz_x = z;
			int d1_x = Level.map.getColour((int) Math.round((tnx_x - correct / 2.0 + radius) / correct), (int) Math.round((tnz_x - correct / 2.0 + radius) / correct));
			int d2_x = Level.map.getColour((int) Math.round((tnx_x - correct / 2.0 + radius) / correct), (int) Math.round((tnz_x - correct / 2.0 - radius) / correct));
			int d3_x = Level.map.getColour((int) Math.round((tnx_x - correct / 2.0 - radius) / correct), (int) Math.round((tnz_x - correct / 2.0 + radius) / correct));
			int d4_x = Level.map.getColour((int) Math.round((tnx_x - correct / 2.0 - radius) / correct), (int) Math.round((tnz_x - correct / 2.0 - radius) / correct));

			if ((d1_x != wallColour) && (d2_x != wallColour) && (d3_x != wallColour) && (d4_x != wallColour)) {
				canX = true;
			} 
			
			// проверяем шаг по Z
			double tnx_z = x;
			double tnz_z = nz + z;
			int d1_z = Level.map.getColour((int) Math.round((tnx_z - correct / 2.0 + radius) / correct), (int) Math.round((tnz_z - correct / 2.0 + radius) / correct));
			int d2_z = Level.map.getColour((int) Math.round((tnx_z - correct / 2.0 + radius) / correct), (int) Math.round((tnz_z - correct / 2.0 - radius) / correct));
			int d3_z = Level.map.getColour((int) Math.round((tnx_z - correct / 2.0 - radius) / correct), (int) Math.round((tnz_z - correct / 2.0 + radius) / correct));
			int d4_z = Level.map.getColour((int) Math.round((tnx_z - correct / 2.0 - radius) / correct), (int) Math.round((tnz_z - correct / 2.0 - radius) / correct));

			if ((d1_z != wallColour) && (d2_z != wallColour) && (d3_z != wallColour) && (d4_z != wallColour)) {
				canZ = true;
			}
		}
		Level.map.setRGB((int) Math.round((x - correct / 2.0) / correct), (int) Math.round((z - correct / 2.0) / correct), ColoursConstants.WHITE);
		// осуществляем движение:
		if (canX) {
			if (tnx > radius && tnx < ((Level.map.getHeight()) * correct - 5.0)) {
				x = tnx;
			}
		}
		if (canZ) {
			if (tnz > radius && tnz < ((Level.map.getWidth()) * correct - 5.0)) {
				z = tnz;
			}
		}

		Level.map.setRGB((int) Math.round((x - correct / 2.0) / correct), (int) Math.round((z - correct / 2.0) / correct), ColoursConstants.RED);

	}

}
