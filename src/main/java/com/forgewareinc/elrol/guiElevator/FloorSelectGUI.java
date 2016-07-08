package com.forgewareinc.elrol.guiElevator;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

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
				if(isFloorValid(world, X, blockY, Z, world.getBlockMetadata(X, blockY,Z))){
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
			switch(world.getBlockMetadata(X, button.id, Z)){
			case 0:
				TeleportPacket.send(X, button.id - 2, Z);
				break;
			case 1:
				TeleportPacket.send(X, button.id + 1, Z);
				break;
			case 2:
				TeleportPacket.send(X, button.id-1, Z - 1);
				break;
			case 3:
				TeleportPacket.send(X, button.id-1, Z + 1);	
				break;
			case 4:
				TeleportPacket.send(X - 1, button.id-1, Z);	
				break;
			case 5:
				TeleportPacket.send(X + 1, button.id-1, Z);	
				break;
			default: System.out.println("ERROR WITH METADATA :" + world.getBlockMetadata(X, button.id, Y));
			}

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
	
	public boolean isFloorValid(World world, int x, int y, int z, int meta){
		if(world.getBlockPowerInput(x, y, z) == 0){
			switch(world.getBlockMetadata(x,y,z)){
			case 0:
				if(world.getBlock(x, y-1, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null && world.getBlock(x, y-2, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null ){
					if(GeneralConfig.preventTraps && world.getBlock(x, y-3, z).isNormalCube()){
						if(checkTraps(world.getBlock(x, y-1, z)) || checkTraps(world.getBlock(x, y-2, z))){
							return false;
						}
					}else{
						return true;
					}
					return true;
				}else{
					return false;}
			case 1:
				if(world.getBlock(x, y+1, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null  && world.getBlock(x, y+2, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null ){
					if(GeneralConfig.preventTraps){
						if(checkTraps(world.getBlock(x, y+1, z)) || checkTraps(world.getBlock(x, y+2, z))){
							return false;
						}
					}else{
						return true;
					}
					return true;
				}else{
					return false;}
			case 2:
				if(world.getBlock(x, y, z-1).getCollisionBoundingBoxFromPool(world, x, y, z) == null  && world.getBlock(x, y-1, z-1).getCollisionBoundingBoxFromPool(world, x, y, z) == null ){
					if(GeneralConfig.preventTraps && world.getBlock(x, y-2, z-1).isNormalCube()){
						if(checkTraps(world.getBlock(x, y, z-1)) || checkTraps(world.getBlock(x, y-1, z-1))){
							return false;
						}
					}else{
						return true;
					}
					return true;
				}else{
					return false;}
			case 3:
				if(world.getBlock(x, y, z+1).getCollisionBoundingBoxFromPool(world, x, y, z) == null  && world.getBlock(x, y-1, z+1).getCollisionBoundingBoxFromPool(world, x, y, z) == null ){
					if(GeneralConfig.preventTraps && world.getBlock(x, y-2, z+1).isNormalCube()){
						if(checkTraps(world.getBlock(x, y, z+1)) || checkTraps(world.getBlock(x, y-1, z+1))){
							return false;
						}
					}else{
						return true;
					}
					return true;
				}else{
					return false;}
			case 4:
				if(world.getBlock(x-1, y, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null  && world.getBlock(x-1, y-1, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null ){
					if(GeneralConfig.preventTraps && world.getBlock(x-1, y-2, z).isNormalCube()){
						if(checkTraps(world.getBlock(x-1, y, z)) || checkTraps(world.getBlock(x-1, y-1, z))){
							return false;
						}
					}else{
						return true;
					}
					return true;
				}else{
					return false;}
			case 5:
				if(world.getBlock(x+1, y, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null  && world.getBlock(x+1, y-1, z).getCollisionBoundingBoxFromPool(world, x, y, z) == null ){
					if(GeneralConfig.preventTraps && world.getBlock(x+1, y-2, z).isNormalCube()){
						if(checkTraps(world.getBlock(x+1, y, z)) || checkTraps(world.getBlock(x+1, y-1, z))){
							return false;
						}
					}else{
						return true;
					}
					return true;
				}else{
					return false;}
			default: 
				System.out.println("ERROR: METADATA > 5");
				return false;
			}
		}else{
			return false;
		}
		
		
	}
	
	//Returns true if traps exist, false if they dont
	private boolean checkTraps(Block block){
		for(int i = 0; i < GeneralConfig.trapBlockList.length; i++){
			if(block.getUnlocalizedName().substring(5).equals(GeneralConfig.trapBlockList[i])){
				return true;
			}
		}
		return false;
	}
	
	private int countFloors(World world, int X, int Z){
		int floor = 0;
		for(int y = 256; y > 0; y--){
			if(world.getBlock(X, y, Z) instanceof Elevator && isFloorValid(world, X, y, Z, world.getBlockMetadata(X, y, Z))){
				floor++;
			}
		}
		if(floor > 24){
			floor = 24;
		}
		return floor;
	}
}