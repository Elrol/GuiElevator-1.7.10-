package com.forgewareinc.elrol.guiElevator;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class GeneralConfig {

	public static Configuration config;
	private static File file = new File("/config/Gui_Elevator.cfg");
	private static final String GENERAL = config.CATEGORY_GENERAL; 
	
	public static boolean preventTraps;
	public static final String preventTrapsComment = "If true, floors will be unavailable if there is no floor or any of the blacklisted blocks are present";
	
	public static String[] trapBlockList;
	public static final String[] defaultTrapBlockList = new String[]{Blocks.lava.getUnlocalizedName().substring(5), Blocks.fire.getUnlocalizedName().substring(5), Blocks.water.getUnlocalizedName().substring(5)};
	public static final String trapBlockListComment = "List of blocks to be checked for as traps";
	
	public static void registerConfig(FMLPreInitializationEvent event){
		config = new Configuration(new File(event.getModConfigurationDirectory() + "/GuiElevator/guiElevator.cfg"));
		FMLCommonHandler.instance().bus().register(ElevatorMain.instance);
		config.addCustomCategoryComment(GENERAL, "General Configuration Options");
		config.load();
		//Boolean - prevent traps
		preventTraps = config.getBoolean("Prevent Traps", GENERAL, false, preventTrapsComment);
		
		//Block[] - trap
		trapBlockList = config.getStringList("Trap Block List", GENERAL, defaultTrapBlockList, trapBlockListComment);
		config.save();
	}
	
	
}
