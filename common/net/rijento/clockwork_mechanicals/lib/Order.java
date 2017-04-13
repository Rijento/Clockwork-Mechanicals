package net.rijento.clockwork_mechanicals.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.rijento.clockwork_mechanicals.lib.filter.Filter;

public class Order 
{
	public final BlockPos pos;
	public final String command;
	public Filter filter;
	public EnumFacing facing;

	public Order(BlockPos pos, String command)
	{
		this.pos = pos;
		this.command = command;
	}
	public Order(BlockPos pos, String command, Filter filterIn)
	{
		this.pos = pos;
		this.command = command;
		this.filter = filterIn;
	}
	public void setFacing(EnumFacing dirIn)
	{
		this.facing = dirIn;
	}
	
	public NBTTagCompound getOrderNBT(Order this)
	{
		NBTTagCompound Tag = new NBTTagCompound();
		Tag.setInteger("x",this.pos.getX());
		Tag.setInteger("y",this.pos.getY());
		Tag.setInteger("z",this.pos.getZ());
		
		Tag.setString("Command", command);
		Tag.setBoolean("hasfilters", false);
		Tag.setBoolean("hasfacing", false);
		
		if (this.filter != null)
		{
			Tag.setTag("filter", this.filter.getFilterNBT());
			Tag.setBoolean("hasfilter", true);
		}
		if (!(this.facing == null))
		{
			Tag.setString("facing", this.facing.getName());
			Tag.setBoolean("hasfacing", true);
		}
		
		return Tag;
	}
	public Order(NBTTagCompound orderNBT)
	{
		this.pos = new BlockPos(orderNBT.getInteger("x"),orderNBT.getInteger("y"),orderNBT.getInteger("z"));
		this.command = orderNBT.getString("Command");
		if (orderNBT.getBoolean("hasfilter"))
		{
			this.filter = new Filter(orderNBT.getCompoundTag("filter"));
		}
		if (orderNBT.getBoolean("hasfacing"))
		{
			this.facing = EnumFacing.byName(orderNBT.getString("facing"));
		}
	}
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {return false;}
	    if (!Order.class.isAssignableFrom(obj.getClass())){return false;}
	    final Order other = (Order) obj;
	    if ((this.pos == null) ? (other.pos != null) : !this.pos.equals(other.pos)){return false;}
	    return true;
	}
	@Override
	public int hashCode() {
	    int hash = 3;
	    hash = 66 * hash + (this.pos != null ? this.pos.hashCode() : 0);
	    return hash;
	}
}