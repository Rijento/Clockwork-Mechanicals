package net.rijento.clockwork_mechanicals.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.entities.EntityMechanicalWorker;
import net.rijento.clockwork_mechanicals.init.ModItems;

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
		this.setMaxDamage(0);	
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
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
	
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		if (this.isInCreativeTab(tab))
        {
			for (ItemMainspringType type : ITEM_VARIANTS)
			{
				subItems.add(new ItemStack(this, 1, type.ordinal()));
			}
		}
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack itemstack, net.minecraft.entity.player.EntityPlayer player, EntityLivingBase entity, net.minecraft.util.EnumHand hand)
	{
		if (entity instanceof EntityMechanicalWorker)
		{
			((EntityMechanicalWorker) entity).setMainspring(player.getHeldItem(hand));
			itemstack.setCount(0);
			
			return true;
		}
		return false;
	}
	
	public static float getResistance(int typeIn)
	{
		ItemMainspringType type = ITEM_VARIANTS[typeIn];
		return type.getResistance();
	}
	public static float getFlexability(int typeIn)
	{
		ItemMainspringType type = ITEM_VARIANTS[typeIn];
		return type.getFlexability();
	}
	public static float getWindingCost(int typeIn)
	{
		ItemMainspringType type = ITEM_VARIANTS[typeIn];
		return type.getWindingCost();
	}
	public static float getMaxTension(int typeIn)
	{
		ItemMainspringType type = ITEM_VARIANTS[typeIn];
		return type.getMaxTension();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + ClockworkMechanicals.RESOURCE_PREFIX + "mainspring_" + ItemMainspringType.fromOrdinal(stack.getMetadata()).getID();
	}
}
