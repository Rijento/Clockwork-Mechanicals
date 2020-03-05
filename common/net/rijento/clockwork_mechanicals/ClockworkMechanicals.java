package net.rijento.clockwork_mechanicals;

import java.util.Random;

import net.minecraft.util.datafix.DataFixer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.rijento.clockwork_mechanicals.lib.ClockworkDataFixesManager;

@Mod(modid = ClockworkMechanicals.MOD_ID, name = ClockworkMechanicals.MOD_NAME, version = ClockworkMechanicals.VERSION, dependencies = ClockworkMechanicals.DEPENDENCIES)

public class ClockworkMechanicals {
	
	//constansts	
	public static final String MOD_ID = "clockwork_mechanicals";
	public static final String MOD_NAME = "Clockwork Mechanicals";
	public static final String VERSION = "@VERSION@";
	public static final String DEPENDENCIES = "required-after:forge@[13.20.0.2228,)";
	public static final String RESOURCE_PREFIX = MOD_ID+":";
	
	public static final CreativeTabCM creativeTab = new CreativeTabCM();
	
	private final DataFixer dataFixer = ClockworkDataFixesManager.createFixer();
	//variables
	public static Random random = new Random();
	
	@Instance(MOD_ID)
	public static ClockworkMechanicals instance;
	
	@SidedProxy(clientSide = "net.rijento.clockwork_mechanicals.ClientProxy", serverSide = "net.rijento.clockwork_mechanicals.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		
		proxy.init(event);
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		
		proxy.postInit(event);
	}
}
