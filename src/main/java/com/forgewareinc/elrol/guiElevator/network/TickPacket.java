package com.forgewareinc.elrol.guiElevator.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.forgewareinc.elrol.guiElevator.ElevatorMain;
import com.forgewareinc.elrol.guiElevator.TileEntityElevator;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class TickPacket implements IMessage {

	static int x;
	static int y;
	static int z;
	
	public TickPacket(){}
	
	public TickPacket(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void sendToServer(IMessage packet){
		ElevatorMain.network.sendToServer(packet);
	}
	
	public static void send(int X, int Y, int Z){
		sendToServer(new TickPacket(X, Y, Z));
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

	public static class Handler implements IMessageHandler<TickPacket, IMessage>{

		@Override
		public IMessage onMessage(TickPacket message, MessageContext ctx) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			Block block = world.getBlock(message.x, message.y, message.z);
			world.scheduleBlockUpdate(message.x, message.y, message.z, block, block.tickRate(world));
			return null;
		}
		
		
		
	}

}
