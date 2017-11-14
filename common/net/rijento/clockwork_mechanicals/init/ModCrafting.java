package net.rijento.clockwork_mechanicals.init;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.rijento.clockwork_mechanicals.ClockworkMechanicals;

public class ModCrafting 
{
	public static void initCrafting()
	{
		OreDictionary.registerOre("hardenedclay", Blocks.HARDENED_CLAY);
		OreDictionary.registerOre("hardenedclay", new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, OreDictionary.WILDCARD_VALUE));
//		ForgeRegistries.RECIPES.register(new ShapedOreRecipe("Clockwork Mechanicals", new ItemStack(ModItems.mechanical_configurator), new Object[] {"PPP","MLW","PPP", 'P', Items.PAPER, 'M', Items.MAP, 'L', "dyeBlue", 'W', ModItems.waypoint_compass}));
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.mechanical_configurator), new Object[] {"PPP","WLM","PPP", 'P', Items.PAPER, 'M', Items.MAP, 'L', "dyeBlue", 'W', ModItems.waypoint_compass}));
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.waypoint_compass), new Object[] {"IBI","BCB","IBI", 'I', "ingotIron", 'B', "ingotBrass", 'C', Items.COMPASS}));
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.mold_template), new Object[] {"BBB","B B","BBB", 'B', Items.BRICK}));
//		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.blank_mold), new Object[]{new ItemStack(ModItems.mold_template, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.CLAY_BALL, 1)}));
//		GameRegistry.addShapelessRecipe(new ItemStack(Items.CLAY_BALL), new Object[]{new ItemStack(ModItems.blank_mold, 1)});
//		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.stamping_table), new Object[] {" B ","WCW","WNW", 'B', "ingotBrass", 'W', "plankWood", 'C', "hardenedclay", 'N', "workbench"}));
	}
}
