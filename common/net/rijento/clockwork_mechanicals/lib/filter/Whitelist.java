package net.rijento.clockwork_mechanicals.lib.filter;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Whitelist extends FilterBase 
{
	public Item item;
	public static String type = "Whitelist";
	
	public Whitelist(Item itemIn)
	{
		this.item = itemIn;
	}
	
	public boolean filterStatified(Item itemIn)
	{
		if (this.item == itemIn)
            {
            	return true;
            }
		else{return false;}
	}
	
	@Override
	public NBTTagCompound getFilterNBT()
	{
		NBTTagCompound Tag = new NBTTagCompound();
		Tag.setString("type", "Whitelist");
		Tag.setTag("item", (new ItemStack(this.item)).writeToNBT(new NBTTagCompound()));
		
		return Tag;
	}
	
	public Whitelist(NBTTagCompound filterNBT)
	{
	ItemStack itemstack = new ItemStack(filterNBT.getCompoundTag("item"));
		this.item = itemstack.getItem();
	}
}
