package net.rijento.clockwork_mechanicals.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class ItemWindingKey extends Item 
{
	public ItemWindingKey()
	{
		this.setMaxStackSize(1);
	}
	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, net.minecraft.entity.player.EntityPlayer player, EntityLivingBase entity, net.minecraft.util.EnumHand hand)
	{
		if (entity instanceof EntityMechanicalWorker && player.isSneaking())
		{
			((EntityMechanicalWorker) entity).wind(5.0F);
			return true;
		}
		return false;
	}
	
}
