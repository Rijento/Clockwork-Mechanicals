package net.rijento.clockwork_mechanicals.lib.filter;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Blacklist extends FilterBase 
{
	public Item item;
	public static String type = "Blacklist";

	public Blacklist(Item itemIn)
	{
		this.item = itemIn;
	}
	
	public boolean filterStatified(Item itemIn)
	{
		if (this.item == itemIn)
            {
            	return false;
            }
		else{return true;}
	}
	
	@Override
	public NBTTagCompound getFilterNBT()
	{
		NBTTagCompound Tag = new NBTTagCompound();
		Tag.setString("type", "Blacklist");
		Tag.setTag("item", (new ItemStack(this.item)).writeToNBT(new NBTTagCompound()));
		
		return Tag;
	}
	
	public Blacklist(NBTTagCompound filterNBT)
	{
		ItemStack itemstack = new ItemStack(filterNBT.getCompoundTag("item"));
		this.item = itemstack.getItem();
	}
}
