package net.rijento.clockwork_mechanicals.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.items.ItemMechanicalConfigurator;
import net.rijento.clockwork_mechanicals.items.ItemMoldTemplate;
import net.rijento.clockwork_mechanicals.items.ItemWaypointCompass;
import net.rijento.clockwork_mechanicals.items.ItemWindingKey;

@ObjectHolder(ClockworkMechanicals.MOD_ID)
public class ModItems
{
	//public static final Collection<Item> registeredItems = new ArrayList<>();
	
	public static final ItemMainspring mainspring = new ItemMainspring();
	public static final ItemWindingKey winding_key = new ItemWindingKey();
	public static final ItemMechanicalConfigurator mechanical_configurator = new ItemMechanicalConfigurator();
	public static final ItemWaypointCompass waypoint_compass = new ItemWaypointCompass();
	public static final Item brass_ingot = new Item();
	public static final Item zinc_ingot = new Item();
	public static final Item copper_ingot = new Item();
	public static final ItemMoldTemplate mold_template = new ItemMoldTemplate();
	public static final Item blank_mold = new Item();
	public static final Item small_gear = new Item();
	public static final Item small_plate = new Item();
	//public static final ItemBlock stamping_table = new ItemBlock(ModBlocks.stamping_table);
	
	public static void setupItems()
	{
		setupItem("mainspring", mainspring);
		setupItem("winding_key", winding_key);
		setupItem("mechanical_configurator", mechanical_configurator);
		setupItem("waypoint_compass", waypoint_compass);
		setupItem("brass_ingot", brass_ingot);
		setupItem("zinc_ingot", zinc_ingot);
		setupItem("copper_ingot", copper_ingot);
		setupItem("mold_template", mold_template);
		setupItem("blank_mold", blank_mold);
		
		setupItem("gear_small", small_gear);
		setupItem("plate_small", small_plate);
		
		//setupItemBlock(stamping_table);
//		
	}
	private static <I extends Item> I setupItem(String name, I item)
	{
		item.setUnlocalizedName(ClockworkMechanicals.RESOURCE_PREFIX + name);
		item.setRegistryName(ClockworkMechanicals.MOD_ID, name);
		item.setCreativeTab(ClockworkMechanicals.creativeTab);
		return item;
	}
	private static Item setupItemBlock(ItemBlock block)
	{
		block.setUnlocalizedName(block.getBlock().getUnlocalizedName());
		block.setRegistryName(block.getBlock().getRegistryName());
		return block;
	}
	
	@Mod.EventBusSubscriber(modid = ClockworkMechanicals.MOD_ID)
	public static class RegistrationHandler
	{
		public static final Set<Item> ITEMS = new HashSet<>();
		
		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event)
		{
			ModItems.setupItems();
			final Item[] items = 
				{
					mainspring,
					winding_key,
					mechanical_configurator,
					waypoint_compass,
					brass_ingot,
					zinc_ingot,
					copper_ingot,
					mold_template,
					blank_mold,
					small_gear,
					small_plate
//					stamping_table
					
				};
			final IForgeRegistry<Item> registry = event.getRegistry();
			
			for(final Item item : items)
			{
				registry.register(item);
				ITEMS.add(item);
			}
		}
	}
}

