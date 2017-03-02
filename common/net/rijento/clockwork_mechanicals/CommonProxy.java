package net.rijento.clockwork_mechanicals;

import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.rijento.clockwork_mechanicals.init.ModEntities;
import net.rijento.clockwork_mechanicals.init.ModItems;

public class CommonProxy 
{	
	public void preInit(FMLPreInitializationEvent event)
	{
		ModItems.preInit();
		ModEntities.preInit();
	}
	
	public void init(FMLInitializationEvent event)
	{	
		
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
}
