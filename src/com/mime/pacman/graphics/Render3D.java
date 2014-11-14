package com.mime.pacman.graphics;

import com.mime.pacman.Game;
import com.mime.pacman.entity.mob.Ghost;
import com.mime.pacman.entity.mob.Player;
import com.mime.pacman.level.Block;
import com.mime.pacman.level.Level;

public class Render3D extends Render {

	// видимость
	public double[] zBuffer; // дистанция
	public double[] zBufferWall; // стен
	public double renderDistance = 5000;

	public double forward, right, cosine, sine, up, walking;
	private int spriteSheetWidth = Texture.block.width; // 160;

	int c = 0;

	public Render3D(int width, int height) {
		super(width, height);
		zBuffer = new double[width * height];
		zBufferWall = new double[width];
	}

	public void floor(Game game) {

		for (int x = 0; x < width; x++) {
			zBufferWall[x] = 0;
		}

		double floorPosition = 8;
		double cellingPosition = 8;
		forward = game.player.z;
		right = game.player.x;
		up = game.player.y;
		walking = 0;
		double rotation = game.player.rotation;
		cosine = Math.cos(rotation);
		sine = Math.sin(rotation);

		for (int y = 0; y < height; y++) {
			double celling = (y + -height / 2.0) / height;
			double z = (floorPosition + up) / celling;
			c = 0;

			if (Player.walk) {
				walking = Math.sin(game.time / 6.0) * 0.5;
				z = (floorPosition + up + walking) / celling;
			}
			if (Player.crouchWalk && Player.walk) {
				walking = Math.sin(game.time / 6.0) * 0.25;
				z = (floorPosition + up + walking) / celling;
			}
			if (Player.runWalk && Player.walk) {
				walking = Math.sin(game.time / 6.0) * 0.8;
				z = (floorPosition + up + walking) / celling;
			}

			if (celling < 0) {
				z = (cellingPosition - up) / -celling;
				c = 1;
				if (Player.walk) {
					z = (cellingPosition - up - walking) / -celling;
				}
			}

			for (int x = 0; x < width; x++) {
				double depth = (x - width / 2.0) / height;
				depth *= z;
				double xx = depth * cosine + z * sine;
				double yy = z * cosine - depth * sine;
				int xPix = (int) ((xx + right) * 4);
				int yPix = (int) ((yy + forward) * 4);
				zBuffer[x + y * width] = z;
				if (c == 0) {
					pixels[x + y * width] = Texture.block.pixels[(xPix & 31) + 32 + (yPix & 31) * spriteSheetWidth];

				} else {
					pixels[x + y * width] = Texture.block.pixels[(xPix & 31) + 64 + (yPix & 31) * spriteSheetWidth];

				}

				if (z > 500) {
					pixels[x + y * width] = 0;
				}
			}

		}

		Level level = game.level;
		int size = Level.map.getHeight() - 1;
		for (int xBlock = -size; xBlock <= size; xBlock++) {
			for (int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);
				Block east = level.create(xBlock + 1, zBlock);
				Block south = level.create(xBlock, zBlock + 1);

				if (block.solid) {
					if (!east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0);
						renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0.5);
					}
					if (!south.solid) {
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
						renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0.5);
					}
				} else {
					if (east.solid) {
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
						renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0.5);
					}
					if (south.solid) {
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
						renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0.5);
					}

				}

			}

		}

		for (int xBlock = -size; xBlock <= size; xBlock++) {
			for (int zBlock = -size; zBlock <= size; zBlock++) {
				Block block = level.create(xBlock, zBlock);

				// gosts:
				for (int s = 0; s < block.ghosts.size(); s++) {
					Ghost ghost = block.ghosts.get(s);
					// движение к пользователю
					if (!Ghost.freez) {
						ghost.moveTo((double) right, (double) forward);
					}
					renderObject(ghost.x, ghost.y, ghost.z, 128);
				}

				// coins:
				Block coinBlock = level.createSprites(xBlock, zBlock);
				for (int s = 0; s < coinBlock.sprites.size(); s++) {
					Sprite sprite = coinBlock.sprites.get(s);
					renderObject(sprite.x, sprite.y, sprite.z, 96);
				}

				// fastes:
				Block fastBlock = level.createSprites(xBlock, zBlock);
				for (int s = 0; s < fastBlock.fastes.size(); s++) {
					Sprite fast = fastBlock.fastes.get(s);
					renderObject(fast.x, fast.y, fast.z, 160);
				}

				// ice:
				Block iceBlock = level.createSprites(xBlock, zBlock);
				for (int s = 0; s < iceBlock.ices.size(); s++) {
					Sprite ice = iceBlock.ices.get(s);
					renderObject(ice.x, ice.y, ice.z, 192);
				}
			}
		}
	}

	private void renderObject(double x, double y, double z, int texturePosition) {
		double upCorrect = -0.125;
		double rightCorrect = 0.0625;
		double forwardCorrect = 0.0625;
		double walkCorrect = 0.0625;

		// position
		double xc = ((x) - (right * rightCorrect)) * 2 + 1;
		double yc = ((y) - (up * upCorrect)) + (walking * walkCorrect) * 2;
		double zc = ((z) - (forward * forwardCorrect)) * 2 + 1;

		// rotation
		double rotX = xc * cosine - zc * sine;
		double rotY = yc;
		double rotZ = zc * cosine + xc * sine;

		double xCentre = width / 2.0;
		double yCentre = height / 2.0;

		// pixels
		double xPixel = rotX / rotZ * height + xCentre;
		double yPixel = rotY / rotZ * height + yCentre;

		// Углы спрайта
		double xPixelL = xPixel - height / 2 / rotZ;
		double xPixelR = xPixel + height / 2 / rotZ;

		double yPixelL = yPixel - height / 2 / rotZ;
		double yPixelR = yPixel + height / 2 / rotZ;

		// переводим в целое
		int xpl = (int) xPixelL;
		int xpr = (int) xPixelR;
		int ypl = (int) yPixelL;
		int ypr = (int) yPixelR;

		if (xpl < 0)
			xpl = 0;
		if (xpr > width)
			xpr = width;
		if (ypl < 0)
			ypl = 0;
		if (ypr > height)
			ypr = height;

		rotZ *= 8;

		for (int yp = ypl; yp < ypr; yp++) {
			double pixelRotationY = (yp - yPixelR) / (yPixelL - yPixelR);
			int yTexture = (int) (pixelRotationY * 8 * 4);
			// int yTexture = (int) (8 * pixelRotationY);

			for (int xp = xpl; xp < xpr; xp++) {
				double pixelRotationX = (xp - xPixelR) / (xPixelL - xPixelR);
				int xTexture = (int) (pixelRotationX * 8 * 4);
				if (zBuffer[xp + yp * width] > rotZ) {
					int col = Texture.block.pixels[((xTexture & 31) + texturePosition) + (yTexture & 31) * spriteSheetWidth];
					if (col != 0xffff00ff) {
						pixels[xp + yp * width] = col;
						zBuffer[xp + yp * width] = rotZ;
					}
				}
			}

		}

	}

	public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
		double upCorrect = 0.0625;
		double rightCorrect = 0.0625;
		double forwardCorrect = 0.0625;
		double walkCorrect = -0.0625;

		double xcLeft = ((xLeft) - (right * rightCorrect)) * 2;
		double zcLeft = ((zDistanceLeft) - (forward * forwardCorrect)) * 2;

		double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
		double yCornerTL = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double yCornerBL = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

		// double xcRight = ((xRight / 2) - (right * rightCorrect)) * 2;
		double xcRight = ((xRight) - (right * rightCorrect)) * 2;
		double zcRight = ((zDistanceRight) - (forward * forwardCorrect)) * 2;

		double rotRightSideX = xcRight * cosine - zcRight * sine;
		double yCornerTR = ((-yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double yCornerBR = ((+0.5 - yHeight) - (-up * upCorrect + (walking * walkCorrect))) * 2;
		double rotRightSideZ = zcRight * cosine + xcRight * sine;

		double tex30 = 0;
		double tex40 = 8;
		double clip = 0.5;

		if (rotLeftSideZ < clip && rotRightSideZ < clip) {
			return;
		}

		// алгоритм гоха на левую сторону текстуры
		if (rotLeftSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex30 = tex30 + (tex40 - tex30) * clip0;
		}

		// алгоритм гоха на правую сторону текстуры
		if (rotRightSideZ < clip) {
			double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
			rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
			rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
			tex40 = tex30 + (tex40 - tex30) * clip0;
		}

		double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
		double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

		if (xPixelLeft >= xPixelRight) {
			return;
		}

		int xPixelLeftInt = (int) (xPixelLeft);
		int xPixelRightInt = (int) (xPixelRight);

		if (xPixelLeftInt < 0) {
			xPixelLeftInt = 0;
		}
		if (xPixelRightInt > width) {
			xPixelRightInt = width;
		}
		double yPixelLeftTop = (yCornerTL / rotLeftSideZ * height + height / 2.0);
		double yPixelLeftBottom = (yCornerBL / rotLeftSideZ * height + height / 2.0);
		double yPixelRightTop = (yCornerTR / rotRightSideZ * height + height / 2.0);
		double yPixelRightBottom = (yCornerBR / rotRightSideZ * height + height / 2.0);

		double tex1 = 1 / rotLeftSideZ;
		double tex2 = 1 / rotRightSideZ;
		double tex3 = tex30 / rotLeftSideZ;
		double tex4 = tex40 / rotRightSideZ - tex3;

		for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
			double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
			double zWall = (tex1 + (tex2 - tex1) * pixelRotation);

			if (zBufferWall[x] > zWall) {
				continue;
			}
			zBufferWall[x] = zWall;

			int xTexture = (int) ((tex3 + tex4 * pixelRotation) / zWall * 4);

			double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
			double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;
			int yPixelTopInt = (int) (yPixelTop);
			int yPixelBottomInt = (int) (yPixelBottom);

			if (yPixelTopInt < 0) {
				yPixelTopInt = 0;
			}
			if (yPixelBottomInt > height) {
				yPixelBottomInt = height;
			}

			for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
				double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
				int yTexture = (int) (8 * pixelRotationY * 4);
				// pixels[x + y * width] = xTexture * 100 + yTexture * 100 *
				// 256;
				pixels[x + y * width] = Texture.block.pixels[((xTexture & 31)) + (yTexture & 31) * spriteSheetWidth];
				zBuffer[x + y * width] = 1 / (tex1 + (tex2 - tex1) * pixelRotation) * 8;
			}
		}

	}

	public void renderDistanceLimiter() {
		for (int i = 0; i < width * height; i++) {
			int colour = pixels[i];
			int brightness = (int) (renderDistance / (zBuffer[i]));

			if (brightness < 0) {
				brightness = 0;
			}

			if (brightness > 255) {
				brightness = 255;
			}

			int r = (colour >> 16) & 0xff;
			int g = (colour >> 8) & 0xff;
			int b = (colour) & 0xff;

			r = r * brightness / 255;
			g = g * brightness / 255;
			b = b * brightness / 255;

			pixels[i] = r << 16 | g << 8 | b;
		}
	}
}
