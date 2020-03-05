package net.rijento.clockwork_mechanicals.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;

//Registers fluid, block, and item models

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ClockworkMechanicals.MOD_ID)
public class ModModels 
{
	public static final ModModels INSTANCE = new ModModels();
	
	private ModModels() {}
	
	@SubscribeEvent
	public static void registerModels(final ModelRegistryEvent event)
	{
		INSTANCE.registerBlockModels();
		INSTANCE.registerItemModels();
	}
	
	private void registerBlockModels()
	{
		
	}
	
	private void registerItemModels()
	{
		registerItemModel(ModItems.mainspring, 0, "mainsprings/mainspring_brass");
		registerItemModel(ModItems.mainspring, 1, "mainsprings/mainspring_iron");
		registerItemModel(ModItems.mainspring, 2, "mainsprings/mainspring_copper");
		registerItemModel(ModItems.mainspring, 3, "mainsprings/mainspring_zinc");
		registerItemModel(ModItems.mainspring, 4, "mainsprings/mainspring_gold");
		
		registerItemModel(ModItems.winding_key, 0, "winding_key");
		
		registerItemModel(ModItems.mechanical_configurator, 0, "mechanical_configurator");
		
		registerItemModel(ModItems.waypoint_compass, 0, "waypointcompass/waypointcompass");
		
		registerItemModel(ModItems.brass_ingot, 0, "brass_ingot");
		registerItemModel(ModItems.zinc_ingot, 0, "zinc_ingot");
		registerItemModel(ModItems.copper_ingot, 0, "copper_ingot");
		
//		registerItemModel(ModItems.mold_template, 0, "mold_template");
//		registerItemModel(ModItems.blank_mold, 0, "mold/blank_mold");
		
		registerItemModel(ModItems.small_gear, 0, "small_gear");
		registerItemModel(ModItems.small_plate, 0, "small_plate");
	}
	
	private static void registerItemModel(Item item, int meta, String file)
	{
		ModelResourceLocation model = new ModelResourceLocation(ClockworkMechanicals.RESOURCE_PREFIX + file, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, meta, model);
	}
}
