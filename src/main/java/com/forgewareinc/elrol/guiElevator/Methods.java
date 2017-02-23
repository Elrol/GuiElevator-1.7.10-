package com.forgewareinc.elrol.guiElevator;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import com.forgewareinc.elrol.guiElevator.network.TeleportPacket;

public class Methods {
	
	public static void dropItem(World world, int x, int y, int z, ItemStack itemstack){
		Random rand = new Random();
		if (itemstack != null){
	        float f = rand.nextFloat() * 0.8F + 0.1F;
	        float f1 = rand.nextFloat() * 0.8F + 0.1F;
	        float f2 = rand.nextFloat() * 0.8F + 0.1F;
	
	        while (itemstack.stackSize > 0)
	        {
	            int j1 = rand.nextInt(21) + 10;
	
	            if (j1 > itemstack.stackSize)
	            {
	                j1 = itemstack.stackSize;
	            }
	
	            itemstack.stackSize -= j1;
	            EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
	
	            if (itemstack.hasTagCompound())
	            {
	                entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
	            }
	
	            float f3 = 0.05F;
	            entityitem.motionX = (double)((float)rand.nextGaussian() * f3);
	            entityitem.motionY = (double)((float)rand.nextGaussian() * f3 + 0.2F);
	            entityitem.motionZ = (double)((float)rand.nextGaussian() * f3);
	            world.spawnEntityInWorld(entityitem);
	        }
	    }
	}
	
	public static void dropItems(World world, int x, int y, int z){
		Random rand = new Random();
	    TileEntityElevator entity = (TileEntityElevator)world.getTileEntity(x, y, z);
	
	    if (entity != null)
	    {
	        for (int i1 = 0; i1 < entity.getSizeInventory(); ++i1)
	        {
	            ItemStack itemstack = entity.getStackInSlot(i1);
	
	            if (itemstack != null)
	            {
	                float f = rand.nextFloat() * 0.8F + 0.1F;
	                float f1 = rand.nextFloat() * 0.8F + 0.1F;
	                float f2 = rand.nextFloat() * 0.8F + 0.1F;
	
	                while (itemstack.stackSize > 0)
	                {
	                    int j1 = rand.nextInt(21) + 10;
	
	                    if (j1 > itemstack.stackSize)
	                    {
	                        j1 = itemstack.stackSize;
	                    }
	
	                    itemstack.stackSize -= j1;
	                    EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
	
	                    if (itemstack.hasTagCompound())
	                    {
	                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
	                    }
	
	                    float f3 = 0.05F;
	                    entityitem.motionX = (double)((float)rand.nextGaussian() * f3);
	                    entityitem.motionY = (double)((float)rand.nextGaussian() * f3 + 0.2F);
	                    entityitem.motionZ = (double)((float)rand.nextGaussian() * f3);
	                    world.spawnEntityInWorld(entityitem);
	                }
	            }
	        }
	    }
	}
	
	public static boolean checkForRedstone(World world, int x, int y, int z){
		if(world.getBlockPowerInput(x, y, z) != 0)
			return true;
		if(world.getBlockPowerInput(x+1, y, z) != 0)
			return true;
		if(world.getBlockPowerInput(x-1, y, z) != 0)
			return true;
		if(world.getBlockPowerInput(x, y+1, z) != 0)
			return true;
		if(world.getBlockPowerInput(x, y-1, z) != 0)
			return true;
		if(world.getBlockPowerInput(x, y, z+1) != 0)
			return true;
		if(world.getBlockPowerInput(x, y, z-1) != 0)
			return true;
		return false;
	}
	
	
	public static BlockPos findElevatorMeta(World world, EntityPlayer player, int x, int y, int z){
		if(world.getBlock(x, y+1, z) instanceof Elevator && world.getBlockMetadata(x, y+1, z) == 0)
			return new BlockPos(x, y+1, z);
		if(world.getBlock(x, y-2, z) instanceof Elevator && world.getBlockMetadata(x, y-2, z) == 1)
			return new BlockPos(x, y-2, z);
		if(world.getBlock(x, y, z+1) instanceof Elevator && world.getBlockMetadata(x, y, z+1) == 2)
			return new BlockPos(x, y, z+1);
		if(world.getBlock(x, y, z-1) instanceof Elevator && world.getBlockMetadata(x, y, z-1) == 3)
			return new BlockPos(x, y, z-1);
		if(world.getBlock(x+1, y, z) instanceof Elevator && world.getBlockMetadata(x+1, y, z) == 4)
			return new BlockPos(x+1, y, z);
		if(world.getBlock(x-1, y, z) instanceof Elevator && world.getBlockMetadata(x-1, y, z) == 5)
			return new BlockPos(x-1, y, z);
		return null;
	}
	
	public static void changeFloor(World world, EntityPlayer player,int x, int y, int z, boolean up){
		if(findElevatorMeta(world, player, x, y, z) == null){
			return;
		}
		BlockPos pos = findElevatorMeta(world, player, x, y, z);
		int blockX = pos.xPos();
		int blockY = pos.yPos();
		int blockZ = pos.zPos();
		Elevator elevator = (Elevator)world.getBlock(blockX, blockY, blockZ);
		if(up){
			for(int a = blockY +1; a < world.getActualHeight(); a++){
				if(world.getBlock(blockX, a, blockZ) instanceof Elevator){
					if(Elevator.canUse(player, world, blockX, a, blockZ) && isFloorValid(world, blockX, a, blockZ, world.getBlockMetadata(blockX, a, blockZ))){
						teleport(player, world, blockX, a, blockZ, world.getBlockMetadata(blockX, a, blockZ));
						break;
					}
				}
			}
		}else{
			for(int a = blockY -1; a > 0; a--){
				if(world.getBlock(blockX, a, blockZ) instanceof Elevator){
					if(Elevator.canUse(player, world, blockX, a, blockZ) && isFloorValid(world, blockX, a, blockZ, world.getBlockMetadata(blockX, a, blockZ))){
						teleport(player, world, blockX, a, blockZ, world.getBlockMetadata(blockX, a, blockZ));
						break;
					}
				}
			}
		}
	}
	
	public static boolean isFloorValid(World world, int x, int y, int z, int meta){
		if(!Methods.checkForRedstone(world, x, y, z)){
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
	private static boolean checkTraps(Block block){
		for(int i = 0; i < GeneralConfig.trapBlockList.length; i++){
			if(block.getUnlocalizedName().substring(5).equals(GeneralConfig.trapBlockList[i])){
				return true;
			}
		}
		return false;
	}
	
	public static void teleport(EntityPlayer player, World world, int x, int y, int z, int meta){
		if(isFloorValid(world, x, y, z, meta)){
			switch(meta){
				case 0:
					TeleportPacket.send(x, y - 2, z);
					break;
				case 1:
					TeleportPacket.send(x, y + 1, z);
					break;
				case 2:
					TeleportPacket.send(x, y - 1, z - 1);
					break;
				case 3:
					TeleportPacket.send(x, y - 1, z + 1);	
					break;
				case 4:
					TeleportPacket.send(x- 1, y - 1, z);
					break;
				case 5:
					TeleportPacket.send(x + 1, y - 1, z);
					break;
				default: 
					return;
			}
			player.addChatComponentMessage(new ChatComponentText("Current Floor: " + ((TileEntityElevator)world.getTileEntity(x, y, z)).name));
			
		}
	}
	
}
