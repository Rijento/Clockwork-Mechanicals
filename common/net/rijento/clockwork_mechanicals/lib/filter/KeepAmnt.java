package net.rijento.clockwork_mechanicals.lib.filter;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class KeepAmnt extends FilterBase 
{
	public Item item;
	public int amount;
	public String target;
	
	public KeepAmnt(Item itemIn, int amountIn, String targetIn)
	{
		this.item = itemIn;
		this.amount = amountIn;
		this.target = targetIn;
	}
	
	public boolean filterStatified(Item itemIn, IInventory inventory)
	{
		if (this.item != itemIn){return true;}
		int count = 0;
		for (int i = 0; i < inventory.getSizeInventory(); ++i)
        {
			
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                if (this.item == itemstack.getItem())
                {
                	count += itemstack.getCount();
                }
            }
        }
		if (count > this.amount){return true;}
		else{return false;}
	}
	
	@Override
	public NBTTagCompound getFilterNBT()
	{
		NBTTagCompound Tag = new NBTTagCompound();
		Tag.setString("type", "KeepAmnt");
		Tag.setString("target", this.target);
		Tag.setTag("item", (new ItemStack(this.item)).writeToNBT(new NBTTagCompound()));
		Tag.setInteger("amount", this.amount);
		
		return Tag;
	}
	
	public KeepAmnt(NBTTagCompound filterNBT)
	{
		ItemStack itemstack = new ItemStack(filterNBT.getCompoundTag("item"));
		this.item = itemstack.getItem();
		this.amount = filterNBT.getInteger("amount");
		this.target = filterNBT.getString("target");
	}
	
}
