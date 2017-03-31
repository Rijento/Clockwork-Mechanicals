package net.rijento.clockwork_mechanicals.lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.rijento.clockwork_mechanicals.lib.filter.Blacklist;
import net.rijento.clockwork_mechanicals.lib.filter.FilterBase;
import net.rijento.clockwork_mechanicals.lib.filter.KeepAmnt;
import net.rijento.clockwork_mechanicals.lib.filter.Whitelist;

public class Order 
{
	public final BlockPos pos;
	public final String command;
	public final List<FilterBase> filters = new ArrayList<FilterBase>();

	public Order(BlockPos pos, String command)
	{
		this.pos = pos;
		this.command = command;
	}
	public Order(BlockPos pos, String command, FilterBase ...filtersIn)
	{
		this.pos = pos;
		this.command = command;
		for (FilterBase filter : filtersIn)
		{
			this.filters.add(filter);
		}
	}
	
	public NBTTagCompound getOrderNBT(Order this)
	{
		NBTTagCompound Tag = new NBTTagCompound();
		Tag.setInteger("x",this.pos.getX());
		Tag.setInteger("y",this.pos.getY());
		Tag.setInteger("z",this.pos.getZ());
		
		Tag.setString("Command", command);
		Tag.setBoolean("hasfilters", false);
		
		if (!this.filters.isEmpty())
		{
			NBTTagList list = new NBTTagList();
			for (FilterBase filter : this.filters)
			{
				list.appendTag(filter.getFilterNBT());
			}
			Tag.setTag("filters", list);
			Tag.setBoolean("hasfilters", true);
		}
		
		return Tag;
	}
	public Order(NBTTagCompound orderNBT)
	{
		this.pos = new BlockPos(orderNBT.getInteger("x"),orderNBT.getInteger("y"),orderNBT.getInteger("z"));
		this.command = orderNBT.getString("Command");
		this.filters.clear();
		if (orderNBT.getBoolean("hasfilters"))
		{
			NBTTagList nbttaglist2 = orderNBT.getTagList("filters", Constants.NBT.TAG_COMPOUND);
			for (int i = 0; i < nbttaglist2.tagCount(); ++i)
	        {
				switch (FilterBase.getNBTType(nbttaglist2.getCompoundTagAt(i)))
				{
				case("KeepAmnt"):
					this.filters.add(new KeepAmnt(nbttaglist2.getCompoundTagAt(i)));
					continue;
				case("Blacklist"):
					this.filters.add(new Blacklist(nbttaglist2.getCompoundTagAt(i)));
					continue;
				case("Whitelist"):
					this.filters.add(new Whitelist(nbttaglist2.getCompoundTagAt(i)));
					continue;
				}
	        }
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