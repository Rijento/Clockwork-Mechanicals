package net.rijento.clockwork_mechanicals.init;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;

public class ModCrafting 
{
	public static void initCrafting()
	{
		GameRegistry.addRecipe(new ItemStack(ModItems.mechanical_configurator), new Object[] {"PPP","MLW","PPP", 'P', Items.PAPER, 'M', Items.MAP, 'L', new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()), 'W', ModItems.waypoint_compass});
	}
}
