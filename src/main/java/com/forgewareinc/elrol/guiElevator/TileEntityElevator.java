package com.forgewareinc.elrol.guiElevator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

public class TileEntityElevator extends TileEntity implements IInventory, IUpdatePlayerListBox{
	
	public String name;
	public boolean isNamed = false;
	public boolean isAdv = false;
	public boolean isLit = false;
	
	private ArrayList<String> userWhitelist = new ArrayList<String>();
	
	private ItemStack[] inventory;
	
	public TileEntityElevator(){
		this.inventory = new ItemStack[this.getSizeInventory()];
		this.canUpdate();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag){
		this.name = tag.getString("name");
		this.isNamed = tag.getBoolean("named");
		this.isAdv = tag.getBoolean("adv");
		NBTTagList list = tag.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound stackTag = list.getCompoundTagAt(i);
			int slot = stackTag.getByte("Slot") & 255;
			this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
		}
		
		NBTTagList whitelist = tag.getTagList("Whitelist", 10);
		ArrayList<String> newList = new ArrayList<String>();
		for(int j = 0; j < whitelist.tagCount(); j++){
			NBTTagCompound userTag = whitelist.getCompoundTagAt(j);
			newList.add(userTag.getString("UUID"));
		}
		this.userWhitelist = newList;

		super.readFromNBT(tag);	
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag){
		if(this.name == null){
			tag.setBoolean("named", false);	
		}else{
			//System.out.println(name + ", " + isNamed);
			tag.setString("name", this.name);
			tag.setBoolean("named", true);
			
		}
		
		NBTTagList list = new NBTTagList();
	    for (int i = 0; i < this.getSizeInventory(); ++i) {
	        if (this.getStackInSlot(i) != null) {
	            NBTTagCompound stackTag = new NBTTagCompound();
	            stackTag.setByte("Slot", (byte) i);
	            this.getStackInSlot(i).writeToNBT(stackTag);
	            list.appendTag(stackTag);
	        }
	    }
	    tag.setTag("Items", list);
		tag.setBoolean("adv", isAdv);
		NBTTagList whitelist = new NBTTagList();
		for(int j = 0; j < this.userWhitelist.size(); j++){
			NBTTagCompound userTag = new NBTTagCompound();
			String username = userWhitelist.get(j);
			userTag.setString("UUID", username);
			whitelist.appendTag(userTag);
		}
		tag.setTag("Whitelist", whitelist);
		super.writeToNBT(tag);
	}
	
	public String getName(){
		if(this.name != null){
			return this.name;
		}else{
			return null;
		}
	}

	public boolean isAdv(){
		return this.isAdv;
	}
	
	public Block getBlock(){
		Block block = null;
		if(this.getStackInSlot(0) != null){
			Item item = getStackInSlot(0).getItem();
			block = Block.getBlockFromItem(this.getStackInSlot(0).getItem());
		}		
		return block;
	}
	
	public boolean isNamed(){
		if(this.name == null){
			return false;
		}else{
			return true;
		}
	}
	
	public Packet getDescriptionPacket(){
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	}
	
	public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet){
		readFromNBT(packet.func_148857_g());
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		 if (index < 0 || index >= this.getSizeInventory())
		        return null;
		    return this.inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		if (this.getStackInSlot(index) != null) {
        ItemStack itemstack;

        if (this.getStackInSlot(index).stackSize <= count) {
            itemstack = this.getStackInSlot(index);
            this.setInventorySlotContents(index, null);
            this.markDirty();
            return itemstack;
        } else {
            itemstack = this.getStackInSlot(index).splitStack(count);

            if (this.getStackInSlot(index).stackSize <= 0) {
                this.setInventorySlotContents(index, null);
            } else {
                //Just to show that changes happened
                this.setInventorySlotContents(index, this.getStackInSlot(index));
            }

            this.markDirty();
            return itemstack;
        }
    } else {
        return null;
    }
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		ItemStack stack = this.getStackInSlot(index);
		this.setInventorySlotContents(index, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if(index < 0 || index >= this.getSizeInventory())
			return;
		if(stack != null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		
		if(stack != null && stack.stackSize == 0)
			stack = null;
		this.inventory[index] = stack;
		this.markDirty();
	}

	@Override
	public String getInventoryName() {
		
		return "container.elevator_inventory";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		 return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64;
	}

	@Override
	public void openInventory() {
		
	}

	@Override
	public void closeInventory() {
		
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index == 0){
			Block block = Block.getBlockFromItem(stack.getItem());
			if(block != null && block.isBlockNormalCube()){
				return true;
			}
		}
		return false;
	}

	@Override
	public void update() {
		if(worldObj.isRemote){
		System.out.println("client side");
		}
	}
	
	public void addUsername(String name){
		this.userWhitelist.add(name);
	}
	
	public void removeUsername(int id){
		this.userWhitelist.remove(id);
	}
	
	public ArrayList<String> getUsernames(){
		return this.userWhitelist;
	}
	
	public void isLit(boolean lit){
		this.isLit = lit;
	}
	
	public boolean getLit(){
		return this.isLit;
	}
}
