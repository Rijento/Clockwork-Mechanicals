package net.rijento.clockwork_mechanicals.lib;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.rijento.clockwork_mechanicals.lib.filter.Filter;

public class Order 
{
	public final BlockPos pos;
	public final String command;
	public InventoryCrafting recipe;
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
		Tag.setBoolean("hasrecipe", false);
		
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
		if (this.recipe != null)
		{
			Tag.setBoolean("hasrecipe", true);
			NBTTagList nbttaglist = new NBTTagList();

			for (int i = 0; i < this.recipe.getSizeInventory(); ++i)
			{
				ItemStack itemstack = this.recipe.getStackInSlot(i);
				nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
			}
			Tag.setTag("recipe", nbttaglist);
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
		if (orderNBT.getBoolean("hasrecipe"))
		{
			NBTTagList nbttaglist = orderNBT.getTagList("recipe", Constants.NBT.TAG_COMPOUND);
			this.recipe = new InventoryCrafting(new ContainerBasic(), 3, 3);

			for (int i = 0; i < nbttaglist.tagCount() && i < 9; ++i) {
				ItemStack itemstack = new ItemStack(nbttaglist.getCompoundTagAt(i));
				if (!itemstack.isEmpty()) {
					this.recipe.setInventorySlotContents(i, itemstack);
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