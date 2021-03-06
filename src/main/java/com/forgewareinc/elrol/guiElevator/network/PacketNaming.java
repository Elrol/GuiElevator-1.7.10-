package com.forgewareinc.elrol.guiElevator.network;

import com.forgewareinc.elrol.guiElevator.ElevatorMain;
import com.forgewareinc.elrol.guiElevator.TileEntityElevator;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketNaming implements IMessage{

	private int x;
	private int y;
	private int z;
	private String name;
	
	public PacketNaming(){
		
	}
	
	public PacketNaming(String name, int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
	}
	
	public static void sendToServer(IMessage packet){
		ElevatorMain.network.sendToServer(packet);
	}
	
	public static void send(String name, int X, int Y, int Z){
		sendToServer(new PacketNaming(name, X, Y, Z));
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		name = ByteBufUtils.readUTF8String(buf);
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		ByteBufUtils.writeUTF8String(buf, name);
	}

	public static class Handler implements IMessageHandler<PacketNaming, IMessage>{

		@Override
		public IMessage onMessage(PacketNaming message, MessageContext ctx) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			if(world.getTileEntity(message.x, message.y, message.z) instanceof TileEntityElevator){
				TileEntityElevator tile = (TileEntityElevator)world.getTileEntity(message.x, message.y, message.z);
				tile.name = message.name;
				tile.markDirty();
			}
			return null;
		}
		
	}
}
