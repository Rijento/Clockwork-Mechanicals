package net.rijento.clockwork_mechanicals.lib.filter;

import net.minecraft.nbt.NBTTagCompound;

public class FilterBase 
{
	public FilterBase(){}
	
	public boolean filterStatified(FilterBase filter){ return true;}
	
	public NBTTagCompound getFilterNBT()
	{
		NBTTagCompound Tag = new NBTTagCompound();
		return Tag;
	}
	protected FilterBase(NBTTagCompound filterNBT){}
	
	public String getNBTType(NBTTagCompound filterNBT)
	{
		return filterNBT.getString("type");
	}

}
