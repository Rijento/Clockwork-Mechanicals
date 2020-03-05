package net.rijento.clockwork_mechanicals;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.init.ModItems;

public class CreativeTabCM extends CreativeTabs
{
	private final ItemStack spring;
	
	public CreativeTabCM()
	{
		super(ClockworkMechanicals.MOD_ID);
		spring = new ItemStack(ModItems.mainspring, 1, 0);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public ItemStack getTabIconItem()
	{
		return spring;
	}
}
