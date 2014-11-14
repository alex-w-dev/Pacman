package com.mime.pacman.entity.mob;

import com.mime.pacman.Display;
import com.mime.pacman.input.InputHandler;

public class Player extends Mob{

	public double y, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	public static boolean walk = false;
	public static boolean crouchWalk = false;
	public static boolean runWalk = false;
	
	public static boolean cached = false;
	private InputHandler input;

	public Player(InputHandler input){
		this.input = input;
	}
	public Player(InputHandler input, double x, double z){
		this.x = x;
		this.z = z;
		this.input = input;
	}
	
	public void tick(){
		double rotationSpeed = 0.001 * Display.MouseSpeed;
		double walkSpeed = 0.5;
		double jumpHeight = 0.5;
		double crouchHeight = 0.33;
		int xa =0;
		int za =0;
		// движение вперед
		if (input.forward) {
			za++;
			walk = true;
		}
		// нзад
		if (input.back) {
			za--;
			walk = true;
		}
		// влево
		if (input.left) {
			xa--;
			walk = true;
		}
		// вправо
		if (input.right) {
			xa++;
			walk = true;
		}
		
		// board turn
		if(input.rleft){
			rotationSpeed = 0.035;
			rotationa -= rotationSpeed;
		}
		
		if(input.rright){
			rotationSpeed = 0.035;
			rotationa += rotationSpeed;
		}

		// mouse
		if (turnLeft) {
			rotationa -= rotationSpeed;
			turnLeft = false;
		}

		if (turnRight) {
			rotationa += rotationSpeed;
			turnRight = false;
		}

		rotation += rotationa;
		rotationa *= 0.5;
		if (input.jump) {
			y += jumpHeight;
			input.run = false;
		}

		if (input.crouch) {
			y -= crouchHeight;
			input.run = false;
			crouchWalk = true;
			walkSpeed = 0.2;
		}

		if (input.run) {
			walkSpeed = 1;
			runWalk = true;
		}

		if(!input.forward && !input.back && !input.left && !input.right){
			walk = false;
		}
		if(!input.crouch){
			crouchWalk = false;
		}
		if (!input.run) {
			runWalk = false;
		}
		/*
		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;
		*/

		// move action
		move(xa, za, rotation, walkSpeed);
		
		y *= 0.9;
	}

}
