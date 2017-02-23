package com.forgewareinc.elrol.guiElevator.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.forgewareinc.elrol.guiElevator.ElevatorMain;
import com.forgewareinc.elrol.guiElevator.ModInfo;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class OpenInventoryPacket implements IMessage {

	static int x;
	static int y;
	static int z;
	
	public OpenInventoryPacket(){}
	
	public OpenInventoryPacket(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void sendToServer(IMessage packet){
		ElevatorMain.network.sendToServer(packet);
	}
	
	public static void send(int X, int Y, int Z){
		sendToServer(new OpenInventoryPacket(X, Y, Z));
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

	public static class Handler implements IMessageHandler<OpenInventoryPacket, IMessage>{

		@Override
		public IMessage onMessage(OpenInventoryPacket message, MessageContext ctx) {
			ctx.getServerHandler().playerEntity.openGui(ElevatorMain.instance, ModInfo.CAMO_GUI, ctx.getServerHandler().playerEntity.worldObj, message.x, message.y, message.z);
			return null;
		}
		
		
		
	}

}
