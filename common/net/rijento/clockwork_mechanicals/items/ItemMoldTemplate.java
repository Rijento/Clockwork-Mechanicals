package net.rijento.clockwork_mechanicals.items;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.rijento.clockwork_mechanicals.init.ModItems;

public class ItemMoldTemplate extends Item
{
	public ItemMoldTemplate()
	{
		this.setMaxStackSize(1);
	    this.setMaxDamage(100);
	    this.setNoRepair();
	}
	@Override
    public ItemStack getContainerItem(ItemStack itemstack){
        ItemStack stack = itemstack.copy();

        stack.setItemDamage(stack.getItemDamage() + 1);
        stack.setCount(1);

        return stack;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack){
        return true;
    }
}
