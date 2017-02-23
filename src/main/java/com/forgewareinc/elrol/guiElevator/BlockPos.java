package com.forgewareinc.elrol.guiElevator;

public class BlockPos {
	private int x;
	private int y;
	private int z;
	private int meta;
	
	public BlockPos(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.meta = -1;
	}
	
	public BlockPos(int x, int y, int z, int meta){
		this.x = x;
		this.y = y;
		this.z = z;
		this.meta = meta;
	}
	public int xPos(){
		return this.x;
	}
	
	public int yPos(){
		return this.y;
	}
	
	public int zPos(){
		return this.z;
	}
	public int meta(){
		return this.meta;
	}
}
