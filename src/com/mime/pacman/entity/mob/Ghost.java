package com.mime.pacman.entity.mob;

import com.mime.pacman.Display;
import com.mime.pacman.graphics.ColoursConstants;
import com.mime.pacman.level.Level;

public class Ghost extends Mob {

	public static boolean freez = false;
	public static int timeFreez = 0;
	public double y = 0.0;
	public double moveSpeed;

	public Ghost(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.moveSpeed = 0.02 * (double) Display.mobSpeed;
	}

	public double getX() {
		return x;
	}

	public double getZ() {
		return z;
	}

	// движение к игроку
	public void moveTo(double xP, double zP) {
		double correct = 16.0;
		double oldX = this.x;
		double oldZ = this.z;
		int wallColour = 0x000000;
		int ghostColour = 0x0000ff;
		int playerColour = 0xff0000;
		double mobPositionX = oldX * correct + correct / 2.0;
		double mobPositionZ = oldZ * correct + correct / 2.0;

		// длина шага по z ( y=1/(a/b+1); x=1-y )
		double stepZ = 1.0 / (Math.abs(xP - mobPositionX) / Math.abs(zP - mobPositionZ) + 1.0);
		// длина шага по x
		double stepX = 1.0 - stepZ;

		// добавляем скорость
		double nx = stepX * moveSpeed;
		double nz = stepZ * moveSpeed;

		// направление
		if (xP < mobPositionX)
			nx *= (-1);
		if (zP < mobPositionZ)
			nz *= (-1);

		// points in block:
		double radius = 0.19;

		double tnx = nx + oldX;
		double tnz = nz + oldZ;
		boolean canX = true;
		boolean canZ = true;

		if ((Math.round(oldX) != Math.round(tnx + radius)) || (Math.round(oldX) != Math.round(tnx - radius)) || (Math.round(oldZ) != Math.round(tnz - radius)) || (Math.round(oldZ) != Math.round(tnz + radius))) {

			// System.out.println(oldX + "/" + oldZ);
			// 4 dotes around person:
			int d1 = Level.map.getColour((int) Math.round(tnx + radius), (int) Math.round(tnz + radius));
			int d2 = Level.map.getColour((int) Math.round(tnx + radius), (int) Math.round(tnz - radius));
			int d3 = Level.map.getColour((int) Math.round(tnx - radius), (int) Math.round(tnz + radius));
			int d4 = Level.map.getColour((int) Math.round(tnx - radius), (int) Math.round(tnz - radius));
			// System.out.println(d1 + "/" + d2 + "/" + d3 + "/" + d4);

			// если хоть одна точка зашла за текстуру
			if ((d1 == wallColour) || (d2 == wallColour) || (d3 == wallColour) || (d4 == wallColour)) {
				canX = false;
				canZ = false;

				// проверяем шаг по Х
				double tnx_x = nx + oldX;
				double tnz_x = oldZ;
				int d1_x = Level.map.getColour((int) Math.round(tnx_x + radius), (int) Math.round(tnz_x + radius));
				int d2_x = Level.map.getColour((int) Math.round(tnx_x + radius), (int) Math.round(tnz_x - radius));
				int d3_x = Level.map.getColour((int) Math.round(tnx_x - radius), (int) Math.round(tnz_x + radius));
				int d4_x = Level.map.getColour((int) Math.round(tnx_x - radius), (int) Math.round(tnz_x - radius));

				if ((d1_x != wallColour) && (d2_x != wallColour) && (d3_x != wallColour) && (d4_x != wallColour)) {
					canX = true;
				}

				// проверяем шаг по Х
				double tnx_z = oldX;
				double tnz_z = nz+ oldZ;
				int d1_z = Level.map.getColour((int) Math.round(tnx_z + radius), (int) Math.round(tnz_z + radius));
				int d2_z = Level.map.getColour((int) Math.round(tnx_z + radius), (int) Math.round(tnz_z - radius));
				int d3_z = Level.map.getColour((int) Math.round(tnx_z - radius), (int) Math.round(tnz_z + radius));
				int d4_z = Level.map.getColour((int) Math.round(tnx_z - radius), (int) Math.round(tnz_z - radius));

				if ((d1_z != wallColour) && (d2_z != wallColour) && (d3_z != wallColour) && (d4_z != wallColour)) {
					canZ = true;
				}
			}
		}

		// обходить ругих духов
		if ((Math.round(oldX) != Math.round(tnx)) || (Math.round(oldZ) != Math.round(tnz))) {

			// one dote - center
			int center = Level.map.getColour((int) Math.round(tnx), (int) Math.round(tnz));

			// если хоть одна точка зашла за текстуру
			if (center == ghostColour) {

				canX = false;
				canZ = false;

			}

			// если дух зашел на игрока
			if (center == playerColour) {
				Player.cached  = true;

			}
		}

		// сменили цвет текущей ячейки духа на белый
		Level.map.setRGB((int) Math.round(this.x), (int) Math.round(this.z), ColoursConstants.WHITE);
		// осуществляем движение:
		if (canX) {
			if (tnx > radius && tnx < ((Level.map.getHeight()) * correct - 5.0)) {
				this.x = tnx;
				if (!canZ) {
					if (xP < mobPositionX)
						this.x -= Math.abs(nz);
					else
						this.x += Math.abs(nz);
				}
			}
		}
		if (canZ) {
			if (tnz > radius && tnz < ((Level.map.getWidth()) * correct - 5.0)) {
				this.z = tnz;
				if (!canX) {
					if (zP < mobPositionZ)
						this.z -= Math.abs(nx);
					else
						this.z += Math.abs(nx);
				}
			}
		}

		Level.map.setRGB((int) Math.round(this.x), (int) Math.round(this.z), ColoursConstants.BLUE);
		// this.x += nx;
		// this.z += nz;

	}

}
