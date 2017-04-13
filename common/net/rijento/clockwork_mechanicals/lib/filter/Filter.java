package net.rijento.clockwork_mechanicals.lib.filter;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class Filter 
{
	public final InventoryBasic filterInventory;
	public boolean Whitelist = true;
	
	public Filter(InventoryBasic inventoryIn)
	{
		this.filterInventory = inventoryIn;
	}
	
	public Filter()
	{
		this.filterInventory = new InventoryBasic("Filter", false, 9);
		this.Whitelist = true;
	}
	
	public Filter(NBTTagCompound compound)
	{
		NBTTagList nbttaglist = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.filterInventory = new InventoryBasic("Filter", false, 9);

        for (int i = 0; i < nbttaglist.tagCount() && i < 9; ++i)
        {
            ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

            if (!itemstack.isEmpty())
            {
                this.filterInventory.addItem(itemstack);
            }
        }
        this.Whitelist = compound.getBoolean("Whitelist");
	}
	
	public boolean filterStatisfied(ItemStack TestStack)
	{
		if (this.Whitelist)
		{
			for (int i = 0; i < 9 && i < this.filterInventory.getSizeInventory(); i++)
			{
				if (ItemStack.areItemsEqualIgnoreDurability(TestStack, this.filterInventory.getStackInSlot(i)) && ItemStack.areItemStackTagsEqual(TestStack, this.filterInventory.getStackInSlot(i)))
				{
					return true;
				}
			}
			return false;
		}
		else
		{
			for (int i = 0; i < 9 && i < this.filterInventory.getSizeInventory(); i++)
			{
				if (ItemStack.areItemsEqualIgnoreDurability(TestStack, this.filterInventory.getStackInSlot(i)) && ItemStack.areItemStackTagsEqual(TestStack, this.filterInventory.getStackInSlot(i)))
				{
					return false;
				}
			}
			return true;
		}
	}
	
	
	public NBTTagCompound getFilterNBT()
	{
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.filterInventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = this.filterInventory.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
            }
        }
        compound.setTag("Items", nbttaglist);
        compound.setBoolean("Whitelist", this.Whitelist);
        
        return compound;
	}
}
