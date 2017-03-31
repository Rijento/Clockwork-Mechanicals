package net.rijento.clockwork_mechanicals;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.rijento.clockwork_mechanicals.init.ModCrafting;
import net.rijento.clockwork_mechanicals.init.ModEntities;
import net.rijento.clockwork_mechanicals.init.ModItems;
import net.rijento.clockwork_mechanicals.init.ModOreDict;

public class CommonProxy 
{	
	
	public void preInit(FMLPreInitializationEvent event)
	{
		ModItems.preInit();
		ModEntities.preInit();
	}
	
	public void init(FMLInitializationEvent event)
	{	
		ModOreDict.init();
		ModCrafting.initCrafting();
		
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
