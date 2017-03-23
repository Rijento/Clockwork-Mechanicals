package net.rijento.clockwork_mechanicals.init;

import java.util.ArrayList;
import java.util.Collection;

import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;
import net.rijento.clockwork_mechanicals.items.ItemMainspring;
import net.rijento.clockwork_mechanicals.items.ItemMechanicalConfigurator;
import net.rijento.clockwork_mechanicals.items.ItemWaypointCompass;
import net.rijento.clockwork_mechanicals.items.ItemWindingKey;

public class ModItems
{
	public static final Collection<Item> registeredItems = new ArrayList<>();
	
	public static final ItemMainspring mainspring = new ItemMainspring();
	public static final ItemWindingKey winding_key = new ItemWindingKey();
	public static final ItemMechanicalConfigurator mechanical_configurator = new ItemMechanicalConfigurator();
	public static final ItemWaypointCompass waypoint_compass = new ItemWaypointCompass();
	
	public static void preInit()
	{
		registerItem("ItemMainspring", mainspring);
		registerItem("ItemWindingKey", winding_key);
		registerItem("ItemMechanicalConfigurator", mechanical_configurator);
		registerItem("ItemWaypointCompass", waypoint_compass);
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

