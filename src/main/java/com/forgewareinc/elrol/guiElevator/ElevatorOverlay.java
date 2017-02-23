package com.forgewareinc.elrol.guiElevator;

import java.util.ArrayList;

import com.forgewareinc.elrol.guiElevator.network.ColoringPacket;
import com.forgewareinc.elrol.guiElevator.network.PacketNaming;
import com.forgewareinc.elrol.guiElevator.proxy.ClientProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ElevatorOverlay extends Block {

	public static int elevatorRenderType;
	public static int renderPass;
	
	public static String name;
	
	public IIcon side;
	protected IIcon field_150164_N;
	
	public ElevatorOverlay() {
		super(Material.iron);
		name = "eleOverlay";
		this.setBlockName(name);
		this.setBlockTextureName(ModInfo.MODID + ":" + name);
		GameRegistry.registerBlock(this, name);
	}

	public boolean isOpaqueCube(){
			return false;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon){
		this.blockIcon = icon.registerIcon(this.textureName);
		this.side = icon.registerIcon(this.textureName + "_face");
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side){
		int meta = world.getBlockMetadata(x, y, z);
		if(side == meta){
			return this.side;
		}else{
			return this.blockIcon;
		}	
	
	}
}
