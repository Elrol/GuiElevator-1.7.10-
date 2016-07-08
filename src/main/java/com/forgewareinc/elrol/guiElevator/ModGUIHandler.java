package com.forgewareinc.elrol.guiElevator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class ModGUIHandler implements IGuiHandler{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID){
		case 0: return new FloorSelectGUI(world, x, y, z, player);
		case 1: return new ElevatorNamingGUI(world, x, y, z, player);
		default: return null;
		}
	}

}
