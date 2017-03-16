package net.rijento.clockwork_mechanicals.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;

public class Order 
{
	public final BlockPos pos;
	public final String command;

	public Order(BlockPos pos, String command)
	{
		this.pos = pos;
		this.command = command;
	}
	
	public NBTTagCompound getOrderNBT(Order this)
	{
		NBTTagCompound Tag = new NBTTagCompound();
		Tag.setInteger("x",this.pos.getX());
		Tag.setInteger("y",this.pos.getY());
		Tag.setInteger("z",this.pos.getZ());
		
		Tag.setString("Command", command);
		
		return Tag;
	}
	public Order(NBTTagCompound orderNBT)
	{
		this.pos = new BlockPos(orderNBT.getInteger("x"),orderNBT.getInteger("y"),orderNBT.getInteger("z"));
		this.command = orderNBT.getString("Command");
	}
}