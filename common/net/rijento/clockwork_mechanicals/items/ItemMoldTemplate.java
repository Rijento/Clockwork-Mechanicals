// Legacy code, for reference
//package net.rijento.clockwork_mechanicals.items;
//
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//
//
//// damage shelved untill I feel up to creating a way to handle the recipe IE untill I feel like playing with recipe factories... works but unbreakable
//public class ItemMoldTemplate extends Item
//{
//	public ItemMoldTemplate()
//	{
//		this.setMaxStackSize(1);
//	    this.setMaxDamage(100);
//	    this.setNoRepair();
//	}
//	@Override
//    public ItemStack getContainerItem(ItemStack itemstack){
//        ItemStack stack = itemstack.copy();
//        stack.attemptDamageItem(1, itemRand, null);
//        return stack;
//    }
//
//    @Override
//    public boolean hasContainerItem(ItemStack stack){
//        return true;
//    }
//}
