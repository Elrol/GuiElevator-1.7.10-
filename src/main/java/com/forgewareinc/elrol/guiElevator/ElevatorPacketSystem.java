package com.forgewareinc.elrol.guiElevator;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class ElevatorPacketSystem {
	private int count = 0;
	private SimpleNetworkWrapper INSTANCE;
	
	public ElevatorPacketSystem(String name){
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(name);
	}
	
	public <REQ extends IMessage, REPLY extends IMessage> void registerPacket(Class <? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side){
		INSTANCE.registerMessage(messageHandler, requestMessageType, count, side);
		count++;
	}
	
	public SimpleNetworkWrapper instance(){
		return INSTANCE;
	}
	
	public void inc(){
		count++;
	}
}
