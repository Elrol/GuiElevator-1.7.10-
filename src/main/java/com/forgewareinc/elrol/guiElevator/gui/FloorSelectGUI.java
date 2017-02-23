package com.forgewareinc.elrol.guiElevator.gui;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.forgewareinc.elrol.guiElevator.Elevator;
import com.forgewareinc.elrol.guiElevator.GeneralConfig;
import com.forgewareinc.elrol.guiElevator.Methods;
import com.forgewareinc.elrol.guiElevator.ModInfo;
import com.forgewareinc.elrol.guiElevator.TileEntityElevator;
import com.forgewareinc.elrol.guiElevator.network.TeleportPacket;

public class FloorSelectGUI extends GuiScreen{

	public static ResourceLocation gui = new ResourceLocation(ModInfo.MODID, "textures/gui/floor_select.png");
	public static int guiWidth = 248;
	public static int guiHeight = 166;
	
	GuiButton floorButton;
	
	public EntityPlayer player;
	
	public int X;
	public int Y;
	public int Z;
	
	public World world;
	
	public FloorSelectGUI (World world, int x, int y, int z, EntityPlayer player){
		this.X = x;
		this.Y = y;
		this.Z = z;
		
		this.player = player;
		this.world = world;
	}
	
	@Override
	public void drawScreen(int x, int y,float tick){
		int guiX = (width-guiWidth)/2;
		int guiY = (height-guiHeight)/2;
		
		GL11.glColor4f(1, 1, 1, 1);
		mc.renderEngine.bindTexture(gui);
		drawTexturedModalRect(guiX, guiY, 0, 0, guiWidth, guiHeight);
		
		drawStrings(guiX, guiY);
		
		super.drawScreen(x, y, tick);
		
	}
	
	public void initGui(){
		int guiX = (width-guiWidth)/2;
		int guiY = (height-guiHeight)/2;
		int floor = 0;
		int col = 0;
		buttonList.clear();
		
		for(int blockY = 256; blockY > 0; blockY--){
			if(world.getBlock(X, blockY, Z) instanceof Elevator){
				if(Elevator.canUse(player, world, X, blockY, Z) && Methods.isFloorValid(world, X, blockY, Z, world.getBlockMetadata(X, blockY,Z))){
					if(floor < 24){
						int f = countFloors(world, X, Z) - floor;
						if(((TileEntityElevator)world.getTileEntity(X, blockY, Z)).getName() != ""){
							buttonList.add(floorButton = new GuiButton(blockY, guiX + 9 + (83*col) , guiY + 20 + (16 * floor)-(16 * (8 * col)), 64, 16, ((TileEntityElevator)world.getTileEntity(X, blockY, Z)).getName()));
						}else{
							buttonList.add(floorButton = new GuiButton(blockY, guiX + 9 + (83*col) , guiY + 20 + (16 * floor)-(16 * (8 * col)), 64, 16, "Floor " + (f)));	
						}
						floor++;
						if(floor > 7 && floor < 16){
							col = 1;
						}
						if(floor > 15){
							col = 2;
						}
						//System.out.println("elevator found at Y:" + blockY + "(" + floor + "[" + ((TileEntityElevator)world.getTileEntity(X, blockY, Z)).name +"]" +")");	
					}
				}
			}
		}
		
		super.initGui();
	}
	
	public void drawStrings(int guiX, int guiY){
		fontRendererObj.drawString("Select a floor", guiX+8,guiY+8, 0x000000);
		
	}
	
	protected void actionPerformed(GuiButton button){
		if(button.id > 0 && button.id < 256){
			double coordX = X;
			double coordY = button.id;
			double coordZ = Z;
			Methods.teleport(player, world, X, button.id, Z, world.getBlockMetadata(X, button.id, Z));

		player.setGameType(WorldSettings.GameType.SURVIVAL);
		mc.displayGuiScreen(null);
		}
	}
	
	protected void keyTyped(char c, int key){
		switch(key){
		case Keyboard.KEY_E:
			mc.displayGuiScreen(null);

		case Keyboard.KEY_ESCAPE:
			mc.displayGuiScreen(null);
		}
	}
	
	private int countFloors(World world, int X, int Z){
		int floor = 0;
		for(int y = 256; y > 0; y--){
			if(world.getBlock(X, y, Z) instanceof Elevator && Methods.isFloorValid(world, X, y, Z, world.getBlockMetadata(X, y, Z))){
				floor++;
			}
		}
		if(floor > 24){
			floor = 24;
		}
		return floor;
	}
	
	public boolean doesGuiPauseGame()
    {
        return false;
    }
}
