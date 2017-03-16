package net.rijento.clockwork_mechanicals;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.rijento.clockwork_mechanicals.init.ModItems;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event){
		
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event){
		
		super.init(event);
		
		ModItems.initClient(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event){
		
		super.postInit(event);
	}

}
