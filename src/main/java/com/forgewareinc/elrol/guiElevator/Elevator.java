package com.forgewareinc.elrol.guiElevator;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Elevator extends BlockContainer {

	public int color;
	public IIcon side;
	protected IIcon field_150164_N;
    
	public static String[] colors = new String[]{"black", "red", "green", "brown", "blue", "purple", "cyan", "silver", "gray", "pink", "lime", "yellow", "lblue", "magenta", "orange", "white"};
	public static String[] dyes = new String[]{"dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite"};
	
	public Elevator(String name, CreativeTabs tab, float hardness, float resistance, SoundType stepSound, int color) {
		super(Material.iron);
		this.setBlockName(name + "_" + colors[color]);
		this.setHardness(hardness);
		this.setResistance(resistance);
		this.setBlockTextureName(ModInfo.MODID + ":" + name + "_" +colors[color]);
		this.setCreativeTab(tab);
		this.setStepSound(stepSound);
		this.color = color;
		GameRegistry.registerBlock(this, name + "_" + colors[color]);
	}
	
	public boolean isNormalCube()
    {
        return false;
    }
	
	public String getDye(){
		return dyes[color];
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par1, float par2, float par3, float par4)
    {
		ItemStack stack = player.inventory.getCurrentItem();
		if(player.isSneaking()){
			player.openGui(ElevatorMain.instance, 1, world, x, y, z);
			return true;
		}else{
			if(stack != null){
				if(getOres(stack).contains("dye")){
					for(int i = 0; i < dyes.length; i++){
						if(getOres(stack).contains(dyes[i]) && !((Elevator)world.getBlock(x, y, z)).getDye().equalsIgnoreCase(colors[i]) ){
							
							if(!player.capabilities.isCreativeMode){
								--player.getCurrentEquippedItem().stackSize; 	
							}
							return false;
						}
					}
				}
			}
			player.openGui(ElevatorMain.instance, 0, world, x, y, z);
			return true;
		}
			
    }

	public static ArrayList<String> getOres(ItemStack stack){
		int[] ores = OreDictionary.getOreIDs(stack);
		ArrayList<String> ore = new ArrayList<String>();
		for(int i = 0; i < ores.length; i++){
			ore.add(OreDictionary.getOreName(ores[i]));
		}
		return ore;
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		try{
			return new TileEntityElevator();	
		}catch (Exception var3){
			throw new RuntimeException(var3);
		}
		
	}
	
	public boolean hasTileEntity(int meta){
		return true;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon){
		this.blockIcon = icon.registerIcon(this.textureName);
		this.side = icon.registerIcon(this.textureName + "_face");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		if(side == meta){
			return this.side;
		}else{
			return this.blockIcon;
		}
	}
	
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta)
    {	
		for(int i = y; i < 256; i++){
			if(world.getBlock(x, i, z) instanceof Elevator){
				TileEntityElevator tile = (TileEntityElevator)world.getTileEntity(x, i, z);
				if(tile.getName() != null && tile.getName().equals("Floor 1")){
					if(world.isRemote){
						ElevatorMain.network.sendToServer(new PacketNaming("Basement 1", x, y, z));
					}
					world.setBlockMetadataWithNotify(x, y, z, side, 2);
					return side;
				}else if(tile.getName() != null && tile.getName().startsWith("Basement")){
					try{
						String test1 = tile.getName().substring(9);
						int a = (Integer.parseInt(test1))+1;
						if(world.isRemote){
							ElevatorMain.network.sendToServer(new PacketNaming("Basement " + a, x, y, z));
						}
						world.setBlockMetadataWithNotify(x, y, z, side, 2);
						return side;
					}catch(Exception e){
						if(world.isRemote){
							ElevatorMain.network.sendToServer(new PacketNaming("Basement", x, y, z));
						}
						world.setBlockMetadataWithNotify(x, y, z, side, 2);
						return side;
					}
				}
			}
		}
			for(int a = y; a > 0; a--){
				if(world.getBlock(x, a, z) instanceof Elevator){
					TileEntityElevator tile1 = (TileEntityElevator)world.getTileEntity(x, a, z);
					if(tile1.getName() != null && tile1.getName().startsWith("Floor")){
						
						try{
							String test = tile1.getName().substring(6);
							int id = (Integer.parseInt(test)) + 1;
							if(world.isRemote){
								ElevatorMain.network.sendToServer(new PacketNaming("Floor " + (id++), x, y, z));
							}
							world.setBlockMetadataWithNotify(x, y, z, side, 2);
							return side;
						}catch(Exception e){
							if(world.isRemote){
								ElevatorMain.network.sendToServer(new PacketNaming("Floor", x, y, z));
							}
							world.setBlockMetadataWithNotify(x, y, z, side, 2);
							return side;
						}	
					}
				}
			}
			if(world.isRemote){
				ElevatorMain.network.sendToServer(new PacketNaming("Floor 1", x, y, z));
			}
					
		world.setBlockMetadataWithNotify(x, y, z, side, 2);
		return side;
    }
	
	public void HandlePacket(EntityPlayer player, int x, int y, int z, String value, byte extra){
		TileEntity te = player.worldObj.getTileEntity(x, y, z);
		if(te != null && te instanceof TileEntityElevator){
			((TileEntityElevator)te).name = value;
			((TileEntityElevator)te).markDirty();;
		}
	}
	
	public void dyeBlock(World world, int x, int y, int z, int dye){
		TileEntityElevator tile = (TileEntityElevator)world.getTileEntity(x, y, z);
		
		Block block = world.getBlock(x, y, z);
		String name = tile.getName();
		int meta = world.getBlockMetadata(x, y, z);
		
		switch(dye){
		case 0: block = ElevatorMain.elevatorBlack; break;
		case 1: block = ElevatorMain.elevatorRed; break;
		case 2: block = ElevatorMain.elevatorGreen; break;
		case 3: block = ElevatorMain.elevatorBrown; break;
		case 4: block = ElevatorMain.elevatorBlue; break;
		case 5: block = ElevatorMain.elevatorPurple; break;
		case 6: block = ElevatorMain.elevatorCyan; break;
		case 7: block = ElevatorMain.elevatorSilver; break;
		case 8: block = ElevatorMain.elevatorGray; break;
		case 9: block = ElevatorMain.elevatorPink; break;
		case 10: block = ElevatorMain.elevatorLime; break;
		case 11: block = ElevatorMain.elevatorYellow; break;
		case 12: block = ElevatorMain.elevatorLBlue; break;
		case 13: block = ElevatorMain.elevatorMagenta; break;
		case 14: block = ElevatorMain.elevatorOrange; break;
		case 15: block = ElevatorMain.elevatorWhite; break;
		default: break;
		}
		world.setBlock(x, y, z, block);
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
		if(world.isRemote){
			ElevatorMain.network.sendToServer(new PacketNaming(name, x, y, z));
		}
	}
}
