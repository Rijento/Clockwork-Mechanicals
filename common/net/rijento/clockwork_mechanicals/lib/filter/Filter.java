package net.rijento.clockwork_mechanicals.lib.filter;

import java.util.Collections;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;

public class Filter {
	public final InventoryBasic filterInventory;
	public boolean Whitelist = false;

	public final NonNullList<Boolean> advancedSlots;
	
	public Filter(InventoryBasic inventoryIn)
	{
		this.filterInventory = inventoryIn;
		advancedSlots = NonNullList.<Boolean>withSize(9, Boolean.FALSE);
	}

	public Filter() {
		this.filterInventory = new InventoryBasic("Filter", false, 9);
		this.Whitelist = true;
		advancedSlots = NonNullList.<Boolean>withSize(9, Boolean.FALSE);
	}

	public Filter(NBTTagCompound compound) {
		advancedSlots = NonNullList.<Boolean>withSize(9, Boolean.FALSE);
		NBTTagList nbttaglist = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		NBTTagList nbttaglist2 = compound.getTagList("AdvancedSlots", Constants.NBT.TAG_BYTE);
		this.filterInventory = new InventoryBasic("Filter", false, 9);

		for (int i = 0; i < nbttaglist.tagCount() && i < 9; ++i) {
			ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));

			if (!itemstack.isEmpty()) {
				this.filterInventory.setInventorySlotContents(i, itemstack);
			}
		}
		for (int i = 0; i < nbttaglist2.tagCount() && i < 9; ++i) {
			this.advancedSlots.set(i, ((NBTTagByte) nbttaglist2.get(i)).getByte() == 1 ? true : false);
		}
		this.Whitelist = compound.getBoolean("Whitelist");
	}

	public boolean filterStatisfied(ItemStack TestStack, IInventory TestInventory1, IInventory TestInventory2) {
		if (this.Whitelist) {
			if (this.filterInventory.isEmpty()) {
				return false;
			}
			for (int i = 0; i < 9 && i < this.filterInventory.getSizeInventory(); i++) {
				if (ItemStack.areItemsEqualIgnoreDurability(TestStack, this.filterInventory.getStackInSlot(i))){
//						&& ItemStack.areItemStackTagsEqual(TestStack, this.filterInventory.getStackInSlot(i))) {
					if (advancedSlots.get(i)) {
						int amountAllowed = filterInventory.getStackInSlot(i).getCount();
						if (getInventoryCount(TestStack, TestInventory1) < amountAllowed) {
							return true;
						} else {
							return false;
						}
					}
					return true;
				}
			}
			return false;
		} else {
			if (this.filterInventory.isEmpty()) {
				return true;
			}
			for (int i = 0; i < 9 && i < this.filterInventory.getSizeInventory(); i++) {
				if (ItemStack.areItemsEqualIgnoreDurability(TestStack, this.filterInventory.getStackInSlot(i))){
//						&& ItemStack.areItemStackTagsEqual(TestStack, this.filterInventory.getStackInSlot(i))) {
					if (advancedSlots.get(i)) {
						int amountAllowed = filterInventory.getStackInSlot(i).getCount();
						if (getInventoryCount(TestStack, TestInventory2) > amountAllowed) {
							return true;
						} else {
							return false;
						}
					}
					return false;
				}
			}
			return true;
		}
	}

	private int getInventoryCount(ItemStack stack, IInventory inventory) {
		int count = 0;
		for (int i = 0; i < inventory.getSizeInventory(); ++i) {

			ItemStack itemstack = inventory.getStackInSlot(i);

			if (!itemstack.isEmpty()) {

				if (ItemStack.areItemsEqual(itemstack, stack)) {
					count += itemstack.getCount();
				} else if (ItemStack.areItemsEqualIgnoreDurability(itemstack, stack)) {
					count += itemstack.getCount();
				}
			}
		}
		return count;
	}

	public NBTTagCompound getFilterNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.filterInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = this.filterInventory.getStackInSlot(i);
			nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
		}

		NBTTagList nbttaglist2 = new NBTTagList();

		for (int i = 0; i < this.advancedSlots.size(); i++) {
			nbttaglist2.appendTag(new NBTTagByte((byte) (advancedSlots.get(i) == Boolean.TRUE ? 1 : 0)));
		}
		compound.setTag("Items", nbttaglist);
		compound.setTag("AdvancedSlots", nbttaglist2);
		compound.setBoolean("Whitelist", this.Whitelist);

		return compound;
	}
}
