package com.forgewareinc.elrol.guiElevator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityElevator extends TileEntity {
	
	public String name;
	public boolean isNamed = false;
	
	public TileEntityElevator(){
	
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		this.name = tag.getString("name");
		this.isNamed = tag.getBoolean("named");
		super.readFromNBT(tag);	
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag){
		if(this.name == null){
			tag.setBoolean("named", false);	
		}else{
			//System.out.println(name + ", " + isNamed);
			tag.setString("name", this.name);
			tag.setBoolean("named", true);
			
		}
		
		super.writeToNBT(tag);
	}
	
	
	public String getName(){
		if(this.name != null){
			return this.name;
		}else{
			return null;
		}
	}
	
	public boolean isNamed(){
		if(this.name == null){
			return false;
		}else{
			return true;
		}
	}
	
	public Packet getDescriptionPacket(){
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	}
	
	public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet){
		readFromNBT(packet.func_148857_g());
	}
		
	
}
