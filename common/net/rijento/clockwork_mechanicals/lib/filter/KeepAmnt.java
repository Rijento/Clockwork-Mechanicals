package net.rijento.clockwork_mechanicals.lib.filter;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.rijento.clockwork_mechanicals.lib.Order;

public class KeepAmnt extends FilterBase 
{
	public ItemStack item;
	public int amount;
	public static String type = "KeepAmnt";
	
	public KeepAmnt(ItemStack itemIn, int amountIn)
	{
		this.item = itemIn;
		this.amount = amountIn;
	}
	
	public int filterStatified(ItemStack itemIn, IInventory inventory)
	{
		if (!ItemStack.areItemsEqualIgnoreDurability(itemIn, this.item)){return -1;}
		int count = 0;
		for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
			
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                
                if (ItemStack.areItemsEqual(itemstack, this.item))
                {
                	count += itemstack.getCount();
                }
                else if (ItemStack.areItemsEqualIgnoreDurability(itemstack, this.item))
                {
                	count += itemstack.getCount();
                }
            }
        }
		if (count > this.amount){return 1;}
		else{return 0;}
	}
	
	@Override
	public NBTTagCompound getFilterNBT()
	{
		NBTTagCompound Tag = new NBTTagCompound();
		Tag.setString("type", "KeepAmnt");
		Tag.setTag("item", this.item.writeToNBT(new NBTTagCompound()));
		Tag.setInteger("amount", this.amount);
		
		return Tag;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {return false;}
	    if (!KeepAmnt.class.isAssignableFrom(obj.getClass())){return false;}
	    final KeepAmnt other = (KeepAmnt) obj;
	    if ((this.item == null) ? (other.item != null) : !this.item.equals(other.item)){return false;}
	    return true;
	}
	
	public KeepAmnt(NBTTagCompound filterNBT)
	{
		ItemStack itemstack = new ItemStack(filterNBT.getCompoundTag("item"));
		this.item = itemstack;
		this.amount = filterNBT.getInteger("amount");
	}
	
}
