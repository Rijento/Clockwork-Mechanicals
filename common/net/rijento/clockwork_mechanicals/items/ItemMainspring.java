package net.rijento.clockwork_mechanicals.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;

public class ItemMainspring extends Item
{
	private static final ItemMainspringType[] ITEM_VARIANTS = new ItemMainspringType[]
			{
					ItemMainspringType.BRASS,
					ItemMainspringType.IRON,
					ItemMainspringType.COPPER,
					ItemMainspringType.ZINC,
					ItemMainspringType.GOLD
			};
	public ItemMainspring()
	{
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
		ItemMainspringType type = ITEM_VARIANTS[stack.getItemDamage()];
		float resistance = type.getResistance();
		float flexability = type.getFlexability();
		float winding_cost = type.getWindingCost();
		float max_tension = type.getMaxTension();
		
		tooltip.add(TextFormatting.WHITE + "Resistance: " + String.valueOf(resistance*100)+"%");
		tooltip.add(TextFormatting.WHITE + "Flexability: " + String.valueOf(flexability*100)+"%");
		tooltip.add(TextFormatting.WHITE + "Winding Cost: " + String.valueOf(winding_cost));
		tooltip.add(TextFormatting.WHITE + "Maximum Tension: " + String.valueOf(max_tension));
    }
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (ItemMainspringType type : ITEM_VARIANTS)
		{
			subItems.add(new ItemStack(item, 1, type.ordinal()));
		}
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, net.minecraft.entity.player.EntityPlayer player, EntityLivingBase entity, net.minecraft.util.EnumHand hand)
	{
		if (entity instanceof EntityMechanicalWorker)
		{
			((EntityMechanicalWorker) entity).setMainspring((ItemMainspring) itemstack.getItem());
			itemstack.setCount(0);
			
			return true;
		}
		return false;
	}
	
	public float getResistance()
	{
		ItemMainspringType type = ITEM_VARIANTS[new ItemStack(this).getItemDamage()];
		return type.getResistance();
	}
	public float getFlexability()
	{
		ItemMainspringType type = ITEM_VARIANTS[new ItemStack(this).getItemDamage()];
		return type.getFlexability();
	}
	public float getWindingCost()
	{
		ItemMainspringType type = ITEM_VARIANTS[new ItemStack(this).getItemDamage()];
		return type.getWindingCost();
	}
	public float getMaxTension()
	{
		ItemMainspringType type = ITEM_VARIANTS[new ItemStack(this).getItemDamage()];
		return type.getMaxTension();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + ClockworkMechanicals.MOD_ID + ":" + ItemMainspringType.fromOrdinal(stack.getMetadata()).getID();
	}
	
	
	

}
