package net.rijento.clockwork_mechanicals.lib.filter;

import net.minecraft.nbt.NBTTagCompound;

public class FilterBase 
{
	public static String type = "Base";
	public FilterBase(){}
	
	public boolean filterStatified(){ return true;}
	
	public NBTTagCompound getFilterNBT()
	{
		NBTTagCompound Tag = new NBTTagCompound();
		return Tag;
	}
	protected FilterBase(NBTTagCompound filterNBT){}
	
	public static String getNBTType(NBTTagCompound filterNBT)
	{
		return filterNBT.getString("type");
	}

}
