package com.forgewareinc.elrol.guiElevator;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;



@Mod(modid = ModInfo.MODID, name = ModInfo.NAME,version = ModInfo.VERSION)
public class ElevatorMain
{
	public static Block elevatorBlack;
	public static Block elevatorRed;
	public static Block elevatorGreen;
	public static Block elevatorBrown;
	public static Block elevatorBlue;
	public static Block elevatorPurple;
	public static Block elevatorCyan;
	public static Block elevatorSilver;
	public static Block elevatorGray;
	public static Block elevatorPink;
	public static Block elevatorLime;
	public static Block elevatorYellow;
	public static Block elevatorLBlue;
	public static Block elevatorMagenta;
	public static Block elevatorOrange;
	public static Block elevatorWhite;
	
	//public static final ElevatorPacketSystem packetSystem = new ElevatorPacketSystem(ModInfo.MODID);
	public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel("guielevator");
	
	@Instance
	public static ElevatorMain instance = new ElevatorMain();
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	//packetSystem.registerPacket(NamingPacket.TeleportPacketHandler.class, NamingPacket.class, Side.SERVER);
    	//packetSystem.registerPacket(TeleportPacket.TeleportPacketHandler.class, TeleportPacket.class, Side.SERVER);
    	network.registerMessage(PacketNaming.Handler.class, PacketNaming.class, 0, Side.SERVER);
    	network.registerMessage(TeleportPacket.Handler.class, TeleportPacket.class, 1, Side.SERVER);
    	GeneralConfig.registerConfig(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	NetworkRegistry.INSTANCE.registerGuiHandler(ElevatorMain.instance, new ModGUIHandler());
    	elevatorBlack = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 0);
    	elevatorRed = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 1);
    	elevatorGreen = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 2);
    	elevatorBrown = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 3);
    	elevatorBlue = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 4);
    	elevatorPurple = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 5);
    	elevatorCyan = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 6);
    	elevatorSilver = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 7);
    	elevatorGray = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 8);
    	elevatorPink = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 9);
    	elevatorLime = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 10);
    	elevatorYellow = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 11);
    	elevatorLBlue = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 12);
    	elevatorMagenta = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 13);
    	elevatorOrange = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 14);
    	elevatorWhite = new Elevator("guielevator", CreativeTabs.tabTransport, 3.0F, 6.0F, Block.soundTypeMetal, 15);
    	GameRegistry.registerTileEntity(TileEntityElevator.class, "elevatorBlock");
    	TileEntity.addMapping(TileEntityElevator.class, ModInfo.MODID + "TileEntityElevator");
    	oreDict();
    	recipes();
	}
    
    public void recipes(){
    	System.out.println("Register Recipe");
    	GameRegistry.addRecipe(new ItemStack(elevatorWhite, 1, 0), "xyx", "yzy", "xyx", 'x', Items.iron_ingot, 'y', Blocks.wool, 'z', Items.ender_pearl);
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorBlack, "blockElevator", "dyeBlack"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorRed, "blockElevator", "dyeRed"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorGreen, "blockElevator", "dyeGreen"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorBrown, "blockElevator", "dyeBrown"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorBlue, "blockElevator", "dyeBlue"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorPurple, "blockElevator", "dyePurple"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorCyan, "blockElevator", "dyeCyan"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorSilver, "blockElevator", "dyeLightGray"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorGray, "blockElevator", "dyeGray"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorPink, "blockElevator", "dyePink"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorLime, "blockElevator", "dyeLime"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorYellow, "blockElevator", "dyeYellow"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorLBlue, "blockElevator", "dyeLightBlue"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorMagenta, "blockElevator", "dyeMagenta"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorOrange, "blockElevator", "dyeOrange"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(elevatorWhite, "blockElevator", "dyeWhite"));
    }
    
    public void oreDict(){
    	OreDictionary.registerOre("blockElevator", elevatorBlack);
    	OreDictionary.registerOre("blockElevator", elevatorRed);
    	OreDictionary.registerOre("blockElevator", elevatorGreen);
    	OreDictionary.registerOre("blockElevator", elevatorBrown);
    	OreDictionary.registerOre("blockElevator", elevatorBlue);
    	OreDictionary.registerOre("blockElevator", elevatorPurple);
    	OreDictionary.registerOre("blockElevator", elevatorCyan);
    	OreDictionary.registerOre("blockElevator", elevatorSilver);
    	OreDictionary.registerOre("blockElevator", elevatorGray);
    	OreDictionary.registerOre("blockElevator", elevatorPink);
    	OreDictionary.registerOre("blockElevator", elevatorLime);
    	OreDictionary.registerOre("blockElevator", elevatorYellow);
    	OreDictionary.registerOre("blockElevator", elevatorLBlue);
    	OreDictionary.registerOre("blockElevator", elevatorMagenta);
    	OreDictionary.registerOre("blockElevator", elevatorOrange);
    	OreDictionary.registerOre("blockElevator", elevatorWhite);
    }
}
