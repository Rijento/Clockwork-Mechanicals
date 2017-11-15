package net.rijento.clockwork_mechanicals.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDict 
{
	public static void init()
	{
		OreDictionary.registerOre("ingotBrass", ModItems.brass_ingot);
		OreDictionary.registerOre("ingotZinc", ModItems.zinc_ingot);
		OreDictionary.registerOre("ingotCopper", ModItems.copper_ingot);
		OreDictionary.registerOre("hardenedclay", Blocks.HARDENED_CLAY);
		OreDictionary.registerOre("hardenedclay", new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("mold_template_wildcard", new ItemStack(ModItems.mold_template, 1, OreDictionary.WILDCARD_VALUE));
	}
}
