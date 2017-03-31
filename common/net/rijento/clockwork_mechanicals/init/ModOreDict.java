package net.rijento.clockwork_mechanicals.init;

import net.minecraftforge.oredict.OreDictionary;

public class ModOreDict 
{
	public static void init()
	{
		OreDictionary.registerOre("ingotBrass", ModItems.brass_ingot);
		OreDictionary.registerOre("ingotZinc", ModItems.zinc_ingot);
		OreDictionary.registerOre("ingotCopper", ModItems.copper_ingot);
	}
}
