package com.mime.pacman.graphics;

public class Sprite {
	
	public double x=0.0,y=0.0,z=0.0;
	
	public Sprite(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void setNewPosition(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public double getX(){
		return x;
	}
	public double getZ(){
		return z;
	}
	
	
}
