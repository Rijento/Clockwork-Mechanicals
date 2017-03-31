package net.rijento.clockwork_mechanicals.init;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.items.ItemMechanicalConfigurator;
import net.rijento.clockwork_mechanicals.items.ItemMoldTemplate;
import net.rijento.clockwork_mechanicals.items.ItemWaypointCompass;
import net.rijento.clockwork_mechanicals.items.ItemWindingKey;

public class ModItems
{
	public static final Collection<Item> registeredItems = new ArrayList<>();
	
	public static final CreativeTabs modTab = new CreativeTabs("clockwork_mechanicals") {
		
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(mainspring, 1, 0);
		}
	};
	
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
	
	public static void preInit()
	{
		registerItem("ItemMainspring", mainspring);
		registerItem("ItemWindingKey", winding_key).setCreativeTab(modTab);
		registerItem("ItemMechanicalConfigurator", mechanical_configurator).setCreativeTab(modTab);
		registerItem("ItemWaypointCompass", waypoint_compass).setCreativeTab(modTab);
		registerItem("ingotBrass", brass_ingot).setCreativeTab(modTab);
		registerItem("ingotZinc", zinc_ingot).setCreativeTab(modTab);
		registerItem("ingotCopper", copper_ingot).setCreativeTab(modTab);
		registerItem("ItemMoldTemplate", mold_template).setCreativeTab(modTab);
		registerItem("ItemBlankMold", blank_mold).setCreativeTab(modTab);
		
		registerItem("ItemGearSmall", small_gear).setCreativeTab(modTab);
		registerItem("ItemPlateSmall", small_plate).setCreativeTab(modTab);
		
	}
	@SideOnly(Side.CLIENT)
	public static void initClient(ItemModelMesher mesher)
	{
		regModel(mesher, mainspring, 0, "mainsprings/mainspring_brass");
		regModel(mesher, mainspring, 1, "mainsprings/mainspring_iron");
		regModel(mesher, mainspring, 2, "mainsprings/mainspring_copper");
		regModel(mesher, mainspring, 3, "mainsprings/mainspring_zinc");
		regModel(mesher, mainspring, 4, "mainsprings/mainspring_gold");
		
		regModel(mesher, winding_key, 0, "winding_key");
		
		regModel(mesher, mechanical_configurator, 0, "mechanical_configurator");
		
		regModel(mesher, waypoint_compass, 0, "waypointcompass/waypointcompass");
		
		regModel(mesher, brass_ingot, 0, "brass_ingot");
		regModel(mesher, zinc_ingot, 0, "zinc_ingot");
		regModel(mesher, copper_ingot, 0, "copper_ingot");
		
		regModel(mesher, mold_template, 0, "mold_template");
		regModel(mesher, blank_mold, 0, "mold/blank_mold");
		
		regModel(mesher, small_gear, 0, "small_gear");
		regModel(mesher, small_plate, 0, "small_plate");
		
	}
	private static <I extends Item> I registerItem(String name, I item)
	{
		item.setUnlocalizedName(ClockworkMechanicals.RESOURCE_PREFIX + name);
		item.setRegistryName(ClockworkMechanicals.MOD_ID, name);
		GameRegistry.register(item);
		
		registeredItems.add(item);
		
		return item;
	}
	private static void regModel(ItemModelMesher mesher, Item item, int meta, String file)
	{
		ModelResourceLocation model = new ModelResourceLocation(ClockworkMechanicals.RESOURCE_PREFIX + file, "inventory");
		ModelLoader.registerItemVariants(item, model);
		mesher.register(item, meta, model);
	}

}

