package com.forgewareinc.elrol.guiElevator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyPressHandler
{
	@SubscribeEvent
	public void onKeyPress(KeyInputEvent event){
		if(Minecraft.getMinecraft().gameSettings.keyBindSneak.getIsKeyPressed() || Minecraft.getMinecraft().gameSettings.keyBindJump.getIsKeyPressed()){
			
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			World world = player.worldObj;
			    int blockX = MathHelper.floor_double(player.posX);
			    int blockY = MathHelper.floor_double(player.posY-0.2D - (double)player.yOffset)+2;
			    int blockZ = MathHelper.floor_double(player.posZ);
			    if(Minecraft.getMinecraft().gameSettings.keyBindJump.getIsKeyPressed()){
			    	Methods.changeFloor(world, player, blockX, blockY, blockZ, true);
			    }else{
			    	Methods.changeFloor(world, player, blockX, blockY, blockZ, false);
			    }
		}	
	}
}