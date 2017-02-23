package com.forgewareinc.elrol.guiElevator.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.world.World;

import com.forgewareinc.elrol.guiElevator.Elevator;
import com.forgewareinc.elrol.guiElevator.ElevatorMain;
import com.forgewareinc.elrol.guiElevator.TileEntityElevator;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class LightingPacket implements IMessage {

	static int x;
	static int y;
	static int z;
	
	public LightingPacket(){}
	
	public LightingPacket(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void sendToServer(IMessage packet){
		ElevatorMain.network.sendToServer(packet);
	}
	
	public static void send(int X, int Y, int Z){
		sendToServer(new LightingPacket(X, Y, Z));
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public static class Handler implements IMessageHandler<LightingPacket, IMessage>{

		@Override
		public IMessage onMessage(LightingPacket message, MessageContext ctx) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			String name = ((TileEntityElevator)world.getTileEntity(x, y, z)).getName();
			int meta = world.getBlockMetadata(message.x, message.y, message.z);
			Elevator block = (Elevator)world.getBlock(message.x, message.y, message.z);
			block.setLight(1.0F);
			world.setBlock(message.x, message.y, message.z, block, meta, 3);
			TileEntityElevator tile = (TileEntityElevator)world.getTileEntity(x, y, z);
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			if(!player.capabilities.isCreativeMode && player.getCurrentEquippedItem().getItem() == Items.glowstone_dust){
				--player.getCurrentEquippedItem().stackSize; 	
			}
			System.out.println("setting light");
			tile.isAdv = block == ElevatorMain.elevatorAdv;
			tile.name = name;
			tile.markDirty();
			return null;
		}
		
		
		
	}

}
