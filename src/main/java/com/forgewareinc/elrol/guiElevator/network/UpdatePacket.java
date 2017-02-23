package com.forgewareinc.elrol.guiElevator.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.forgewareinc.elrol.guiElevator.ElevatorMain;
import com.forgewareinc.elrol.guiElevator.TileEntityElevator;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class UpdatePacket implements IMessage {

	static int x;
	static int y;
	static int z;
	
	public UpdatePacket(){}
	
	public UpdatePacket(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void sendToServer(IMessage packet){
		ElevatorMain.network.sendToServer(packet);
	}
	
	public static void send(int X, int Y, int Z){
		sendToServer(new UpdatePacket(X, Y, Z));
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

	public static class Handler implements IMessageHandler<UpdatePacket, IMessage>{

		@Override
		public IMessage onMessage(UpdatePacket message, MessageContext ctx) {
			World world = ctx.getServerHandler().playerEntity.worldObj;
			String name = ((TileEntityElevator)world.getTileEntity(x, y, z)).getName();
			int meta = world.getBlockMetadata(message.x, message.y, message.z);
			world.setBlock(message.x, message.y, message.z, ElevatorMain.elevatorAdv, meta, 2);
			TileEntityElevator tile = (TileEntityElevator)world.getTileEntity(x, y, z);
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			if(!player.capabilities.isCreativeMode){
				--player.getCurrentEquippedItem().stackSize; 	
			}
			tile.isAdv = true;
			tile.name = name;
			tile.markDirty();
			return null;
		}
		
		
		
	}

}
